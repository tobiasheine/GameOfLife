package com.coderetreat.gol.engine.concurrent;

import android.os.Handler;
import android.os.Looper;
import com.coderetreat.gol.engine.AbstractGridEngine;
import com.coderetreat.gol.engine.SingleThreadedGridEngine;
import com.coderetreat.gol.grid.Grid;
import com.coderetreat.gol.grid.IGrid;
import com.coderetreat.gol.grid.cell.Cell;
import com.coderetreat.gol.ruleset.IGameOfLifeRules;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 *  nice, but failing approach...
 */
public class BetterBarrierGridEngine extends AbstractGridEngine implements Runnable{
    private final int NUMBER_THREADS = Runtime.getRuntime().availableProcessors();

    private final Set<Thread> threadSet;
    private final CyclicBarrier barrier;

    private final Handler mainLoop;

    public BetterBarrierGridEngine(final IGameOfLifeRules rules, final GridEngineListener engineListener) {
        super(rules, engineListener);
        threadSet = new HashSet<Thread>();
        barrier = new CyclicBarrier(NUMBER_THREADS, this);
        mainLoop = new Handler(Looper.getMainLooper());
    }

    @Override
    public void processNextGeneration(IGrid grid) {
        reset();
        splitUpGridToSubGrids(grid);
        startThreads();
    }

    private void reset() {
        barrier.reset();
        for (Thread thread : threadSet){
            thread.interrupt();
        }
        threadSet.clear();
    }

    private void startThreads() {
        for (Thread thread : threadSet){
            thread.start();
        }
    }

    private void splitUpGridToSubGrids(IGrid parentGrid){
        final int height = parentGrid.getHeight();
        final int width = parentGrid.getWidth();

        final int gridDimensions = width * height;
        final int numberPositionsForSingleThread = gridDimensions / NUMBER_THREADS;

        final int subGridHeight = numberPositionsForSingleThread / width;
        final int lastSubGridHeight = subGridHeight + gridDimensions % NUMBER_THREADS / width;

        for(int processorCount = 0; processorCount < NUMBER_THREADS; processorCount++){
            final IGrid subGrid;
            if (processorCount == NUMBER_THREADS -1){
                subGrid = new SubGrid(parentGrid.getWidth(),lastSubGridHeight,parentGrid, processorCount, subGridHeight);
            }else {
                subGrid = new SubGrid(parentGrid.getWidth(),subGridHeight,parentGrid, processorCount, subGridHeight);
            }

            GridEngineListener gridEngineListener = new GridEngineListener() {
                @Override
                public void gridIsProcessed() {
                    try {
                        barrier.await();
                    } catch (InterruptedException e) {
                        return;
                    } catch (BrokenBarrierException e) {
                        return;
                    }
                }
            };

            threadSet.add(new Thread(new RunnableSingleThreadedGridEngine(rules, gridEngineListener, subGrid)));
        }

    }

    @Override
    public void run() {
        mainLoop.post(new Runnable() {
            @Override
            public void run() {
                engineListener.gridIsProcessed();
            }
        });
    }

    private static class SubGrid extends Grid{
        private final IGrid parentGrid;
        private final int subGridIndex;
        private final int subGridOffset;

        public SubGrid(int width, int height, IGrid parentGrid, int subGridIndex, int subGridOffset) {
            super(width, height);
            this.parentGrid = parentGrid;
            this.subGridIndex = subGridIndex;
            this.subGridOffset = subGridOffset;
        }

        @Override
        public List<Cell> getCellNeighbours(Cell.Position position) {
            return parentGrid.getCellNeighbours(translateSubGridPositionToParentGrid(position));
        }

        @Override
        public synchronized Cell getCellForPosition(Cell.Position position) {
            return parentGrid.getCellForPosition(translateSubGridPositionToParentGrid(position));
        }

        private Cell.Position translateSubGridPositionToParentGrid(Cell.Position subGridPosition){
            Cell.Position positionInParent = new Cell.Position(subGridPosition.getX(),subGridIndex*subGridOffset+subGridPosition.getY());
            return positionInParent;
        }

    }

    private static class RunnableSingleThreadedGridEngine extends SingleThreadedGridEngine implements Runnable{

        private final IGrid subGrid;

        public RunnableSingleThreadedGridEngine(IGameOfLifeRules rules, GridEngineListener engineListener, IGrid subGrid) {
            super(rules, engineListener);
            this.subGrid = subGrid;
        }

        @Override
        public void run() {
            RunnableSingleThreadedGridEngine.this.processNextGeneration(subGrid);
        }
    }
}
