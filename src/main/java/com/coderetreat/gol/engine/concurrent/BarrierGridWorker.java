package com.coderetreat.gol.engine.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class BarrierGridWorker extends GridWorkerDecorator{

    private final CyclicBarrier barrier;

    public BarrierGridWorker(GridWorker gridWorker, CyclicBarrier barrier) {
        super(gridWorker);
        this.barrier = barrier;
    }

    @Override
    public void run() {
        super.run();

        try {
            barrier.await();
        } catch (InterruptedException e) {
            return;
        } catch (BrokenBarrierException e) {
            return;
        }
    }
}
