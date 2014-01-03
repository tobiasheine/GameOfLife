package com.coderetreat.gol.engine.concurrent;

import android.os.Handler;
import android.os.Looper;
import com.coderetreat.gol.engine.AbstractGridEngine;
import com.coderetreat.gol.grid.IGrid;
import com.coderetreat.gol.grid.cell.Cell;
import com.coderetreat.gol.ruleset.IGameOfLifeRules;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorOnlyGridEngine extends AbstractGridEngine{
    private final static int NUMBER_THREADS = Runtime.getRuntime().availableProcessors();

    private ExecutorService executor;

    public ExecutorOnlyGridEngine(IGameOfLifeRules rules, GridEngineListener engineListener) {
        super(rules, engineListener);
    }

    @Override
    public void processNextGeneration(final IGrid grid) {
        cellRulesMap.clear();

        //because executor.awaitTermination blocks the following code should be executed on a separate thread
        executor = Executors.newFixedThreadPool(NUMBER_THREADS+1);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                int gridDimensions = grid.getWidth() * grid.getHeight();
                int numberPositionsForSingleThread = gridDimensions / NUMBER_THREADS;
                int width = grid.getWidth();
                int height = grid.getHeight();

                int currentPosition = 0;

                GridWorker worker = null;

                final GridWorker[] workers = new BaseGridWorker[NUMBER_THREADS];

                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        if (newWorkerNeeded(workers,numberPositionsForSingleThread, currentPosition)) {
                            final int workerIndex = currentPosition / numberPositionsForSingleThread;
                            worker = new BaseGridWorker(grid, cellRulesMap, rules);
                            workers[workerIndex] = worker;
                        }

                        if (worker != null) {
                            worker.addPosition(new Cell.Position(x, y));
                        }
                        currentPosition++;
                    }
                }

                //submit all tasks to executor
                for(int workerCount = 0; workerCount < workers.length;workerCount++){
                    executor.submit(workers[workerCount]);
                }

                //performs shutdown and executes all submitted tasks
                executor.shutdown();

                //blocks until all tasks are executed
                try {
                    executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    //TODO: handle it
                    e.printStackTrace();
                } finally {

                    //Callback UI-Thread
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            engineListener.gridIsProcessed();
                        }
                    });
                }
            }
        });
    }

    private boolean newWorkerNeeded(GridWorker[] workers, int numberPositionsForSingleThread, int currentPosition) {
        return currentPosition % numberPositionsForSingleThread == 0 && stillSpaceForAWorker(workers,numberPositionsForSingleThread, currentPosition);
    }

    private boolean stillSpaceForAWorker(GridWorker[] workers, int numberPositionsForSingleThread, int currentPosition) {
        return workers.length > currentPosition / numberPositionsForSingleThread;
    }
}
