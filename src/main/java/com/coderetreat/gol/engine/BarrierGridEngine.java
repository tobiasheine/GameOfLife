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

public class BarrierGridEngine extends AbstractGridEngine implements Runnable {
    private final int NUMBER_THREADS = Runtime.getRuntime().availableProcessors();

    private final CyclicBarrier barrier;
    private final GridWorker[] workers;
    private final Handler mainLoop;

    public BarrierGridEngine(IGameOfLifeRules rules, GridEngineListener engineListener) {
        super(rules, engineListener);
        barrier = new CyclicBarrier(NUMBER_THREADS, this);
        workers = new GridWorker[NUMBER_THREADS];
        mainLoop = new Handler(Looper.getMainLooper());
    }

    @Override
    public void processNextGeneration(IGrid grid) {
        reset();
        initWorkersWithPositions(grid);
        startWorkers();
    }

    private void initWorkersWithPositions(IGrid grid) {
        int gridDimensions = grid.getWidth() * grid.getHeight();
        int numberPositionsForSingleThread = gridDimensions / NUMBER_THREADS;

        int width = grid.getWidth();
        int height = grid.getHeight();

        int currentPosition = 0;

        GridWorker worker = null;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (newWorkerNeeded(numberPositionsForSingleThread, currentPosition)) {
                    worker = new GridWorker(grid);
                    workers[currentPosition / numberPositionsForSingleThread] = worker;
                }

                if (worker != null) {
                    worker.addPosition(new Cell.Position(x, y));
                }
                currentPosition++;
            }
        }
    }

    private boolean newWorkerNeeded(int numberPositionsForSingleThread, int currentPosition) {
        return currentPosition % numberPositionsForSingleThread == 0 && stillSpaceForAWorker(numberPositionsForSingleThread, currentPosition);
    }

    private boolean stillSpaceForAWorker(int numberPositionsForSingleThread, int currentPosition) {
        return workers.length > currentPosition / numberPositionsForSingleThread;
    }

    private void startWorkers() {
        for (int i = 0; i < workers.length; i++) {
            new Thread(workers[i]).start();
        }
    }

    private void reset() {
        barrier.reset();
        cellRulesMap.clear();

        for (int i = 0; i < workers.length; i++) {
            workers[i] = null;
        }
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
