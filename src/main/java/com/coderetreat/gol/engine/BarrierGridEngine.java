package com.coderetreat.gol.engine;

import android.os.AsyncTask;
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

    public BarrierGridEngine(IGameOfLifeRules rules, GridEngineListener engineListener) {
        super(rules, engineListener);
        barrier = new CyclicBarrier(NUMBER_THREADS, this);
        workers = new GridWorker[NUMBER_THREADS];
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
        workers[0] = worker;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (currentPosition % numberPositionsForSingleThread == 0 && workers.length > currentPosition / numberPositionsForSingleThread) {
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

    //TODO: this shouldn't be a runnable and get rid of that async task
    @Override
    public void run() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                applyRulesToAllCells();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                engineListener.gridIsProcessed();
            }
        }.execute();
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
