package com.coderetreat.gol;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.coderetreat.gol.engine.BarrierGridEngine;
import com.coderetreat.gol.engine.SingleThreadedGridEngine;
import com.coderetreat.gol.grid.IGrid;
import com.coderetreat.gol.engine.IGridEngine;
import com.coderetreat.gol.ruleset.GameOfLifeRules;
import com.coderetreat.gol.ruleset.IGameOfLifeRules;
import com.coderetreat.gol.grid.IGameOfLifeCanvas;

public class GameOfLifeActivity extends Activity implements IGridEngine.GridEngineListener{

    private IGridEngine gridEngine;
    private IGameOfLifeCanvas gridCanvas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        gridCanvas = (IGameOfLifeCanvas) findViewById(R.id.gameOfLifeCanvas);
        gridCanvas.drawGrid();

        final IGrid grid = gridCanvas.getGrid();
        IGameOfLifeRules rules = new GameOfLifeRules(grid);

        //TODO: add menu to switch between engines
        gridEngine = new BarrierGridEngine(rules,this);

        findViewById(R.id.nextStep).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridEngine.processNextGeneration(grid);
            }
        });
    }

    @Override
    public void gridIsProcessed() {
        gridCanvas.drawGrid();
    }
}
