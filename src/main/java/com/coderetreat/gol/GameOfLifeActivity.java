package com.coderetreat.gol;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.coderetreat.gol.engine.IGridEngine;
import com.coderetreat.gol.engine.concurrent.barrier.BarrierGridEngine;
import com.coderetreat.gol.grid.IGameOfLifeCanvas;
import com.coderetreat.gol.grid.IGrid;
import com.coderetreat.gol.ruleset.GameOfLifeRuleSet;
import com.coderetreat.gol.ruleset.IGameOfLifeRuleSet;

public class GameOfLifeActivity extends Activity implements IGridEngine.GridEngineListener{

    private IGridEngine gridEngine;
    private IGameOfLifeCanvas gridCanvas;
    private View nextStep;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        gridCanvas = (IGameOfLifeCanvas) findViewById(R.id.gameOfLifeCanvas);
        gridCanvas.drawGrid();

        final IGrid grid = gridCanvas.getGrid();
        IGameOfLifeRuleSet rules = new GameOfLifeRuleSet(grid);

        //TODO: add menu to switch between engines
        gridEngine = new BarrierGridEngine(rules,this);

        nextStep = findViewById(R.id.nextStep);
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextStep.setEnabled(false);
                gridEngine.processNextGeneration(grid);
            }
        });
    }

    @Override
    public void gridIsProcessed() {
        gridCanvas.drawGrid();
        nextStep.setEnabled(true);
    }
}
