package com.coderetreat.gol.engine;

import android.os.Handler;
import android.os.Looper;
import com.coderetreat.gol.grid.IGrid;
import com.coderetreat.gol.grid.cell.Cell;
import com.coderetreat.gol.ruleset.IGameOfLifeRules;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BarrierGridEngine extends AbstractGridEngine implements Runnable {
    private final int NUMBER_THREADS = Runtime.getRuntime().availableProcessors();

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
        initWorkersWithPositions(grid);
    }

    private void initWorkersWithPositions(IGrid grid) {
        int gridDimensions = grid.getWidth() * grid.getHeight();
        int numberPositionsForSingleThread = gridDimensions / NUMBER_THREADS;

        int width = grid.getWidth();
        int height = grid.getHeight();

        int currentPosition = 0;

        GridWorker worker = null;

        GridWorker[] workers = new GridWorker[NUMBER_THREADS];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (newWorkerNeeded(workers,numberPositionsForSingleThread, currentPosition)) {
                    worker = new GridWorker(grid);
                    workers[currentPosition / numberPositionsForSingleThread] = worker;

                    int lastWorkerIndex = currentPosition / numberPositionsForSingleThread - 1;

                    if (lastWorkerIndex >= 0){
                        executor.execute(workers[lastWorkerIndex]);
                    }
                }

                if (worker != null) {
                    worker.addPosition(new Cell.Position(x, y));
                }
                currentPosition++;
            }
        }

        executor.execute(workers[workers.length-1]);
    }

    private boolean newWorkerNeeded(GridWorker[] workers, int numberPositionsForSingleThread, int currentPosition) {
        return currentPosition % numberPositionsForSingleThread == 0 && stillSpaceForAWorker(workers,numberPositionsForSingleThread, currentPosition);
    }

    private boolean stillSpaceForAWorker(GridWorker[] workers, int numberPositionsForSingleThread, int currentPosition) {
        return workers.length > currentPosition / numberPositionsForSingleThread;
    }

    private void reset() {
        barrier.reset();
        cellRulesMap.clear();
    }

    //executed on one of the worker threads, before they're released
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

    private class GridWorker implements Runnable, IGridEngine {

        private final List<Cell.Position> positionList;
        private final IGrid grid;

        private GridWorker(IGrid grid) {
            this.positionList = new ArrayList<Cell.Position>();
            this.grid = grid;
        }

        private void addPosition(Cell.Position position) {
            positionList.add(position);
        }

        @Override
        public void run() {
            processNextGeneration(grid);
        }

        @Override
        public void processNextGeneration(IGrid grid) {
            for (Cell.Position position : positionList) {
                Cell cell = grid.getCellForPosition(position);

                IGameOfLifeRules.Rules ruleForCell = rules.getRuleForCell(cell);
                cellRulesMap.put(cell, ruleForCell);
            }

            try {
                barrier.await();
            } catch (InterruptedException e) {
                return;
            } catch (BrokenBarrierException e) {
                return;
            }
        }
    }
}
