package com.coderetreat.gol.engine.concurrent;

import android.os.Handler;
import android.os.Looper;
import com.coderetreat.gol.engine.AbstractGridEngine;
import com.coderetreat.gol.grid.IGrid;
import com.coderetreat.gol.grid.cell.Cell;
import com.coderetreat.gol.ruleset.IGameOfLifeRules;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BarrierGridEngine extends AbstractGridEngine implements Runnable {
    private final static int NUMBER_THREADS = Runtime.getRuntime().availableProcessors();

    private final CyclicBarrier barrier;
    private final Handler mainLoop;
    private final Executor executor;

    public BarrierGridEngine(IGameOfLifeRules rules, GridEngineListener engineListener) {
        super(rules, engineListener);
        barrier = new CyclicBarrier(NUMBER_THREADS, this);
        mainLoop = new Handler(Looper.getMainLooper());
        executor = Executors.newFixedThreadPool(NUMBER_THREADS);
    }

    @Override
    public void processNextGeneration(IGrid grid) {
        reset();
        initAndStartWorkers(grid);
    }

    private void initAndStartWorkers(IGrid grid) {
        int gridDimensions = grid.getWidth() * grid.getHeight();
        int numberPositionsForSingleThread = gridDimensions / NUMBER_THREADS;

        int width = grid.getWidth();
        int height = grid.getHeight();

        int currentPosition = 0;

        BarrierGridWorker worker = null;

        final BarrierGridWorker[] workers = new BarrierGridWorker[NUMBER_THREADS];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (newWorkerNeeded(workers,numberPositionsForSingleThread, currentPosition)) {
                    final int workerIndex = currentPosition / numberPositionsForSingleThread;

                    if (workerIndex > 0){
                        executor.execute(workers[workerIndex-1]);
                    }

                    worker = new BarrierGridWorker(grid, barrier, cellRulesMap, rules);
                    workers[workerIndex] = worker;
                }

                if (worker != null) {
                    worker.addPosition(new Cell.Position(x, y));
                }
                currentPosition++;
            }
        }

        executor.execute(workers[workers.length-1]);
    }

    private boolean newWorkerNeeded(BarrierGridWorker[] workers, int numberPositionsForSingleThread, int currentPosition) {
        return currentPosition % numberPositionsForSingleThread == 0 && stillSpaceForAWorker(workers,numberPositionsForSingleThread, currentPosition);
    }

    private boolean stillSpaceForAWorker(BarrierGridWorker[] workers, int numberPositionsForSingleThread, int currentPosition) {
        return workers.length > currentPosition / numberPositionsForSingleThread;
    }

    private void reset() {
        //TODO: cancel running tasks
        barrier.reset();
        cellRulesMap.clear();
    }

    //executed on one of the barriers worker threads, before they're released
    @Override
    public void run() {
        applyRulesToAllCells();

        mainLoop.post(new Runnable() {
            @Override
            public void run() {
                engineListener.gridIsProcessed();
            }
        });
    }
}
