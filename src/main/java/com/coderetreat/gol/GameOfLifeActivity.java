package com.coderetreat.gol;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.coderetreat.gol.engine.SingleThreadedGridEngine;
import com.coderetreat.gol.grid.IGrid;
import com.coderetreat.gol.engine.IGridEngine;
import com.coderetreat.gol.ruleset.GameOfLifeRules;
import com.coderetreat.gol.ruleset.IGameOfLifeRules;
import com.coderetreat.gol.grid.IGameOfLifeCanvas;

public class GameOfLifeActivity extends Activity {

    private IGridEngine gridEngine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final IGameOfLifeCanvas gridCanvas = (IGameOfLifeCanvas) findViewById(R.id.gameOfLifeCanvas);
        gridCanvas.drawGrid();

        final IGrid grid = gridCanvas.getGrid();
        IGameOfLifeRules rules = new GameOfLifeRules(grid);

        gridEngine = new SingleThreadedGridEngine(rules);

        findViewById(R.id.nextStep).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridEngine.processNextGeneration(grid);
                gridCanvas.drawGrid();
            }
        });


    }
}
