package com.coderetreat.gol;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.coderetreat.gol.engine.GridEngine;
import com.coderetreat.gol.engine.grid.IGrid;
import com.coderetreat.gol.engine.IGridEngine;
import com.coderetreat.gol.engine.rules.GameOfLifeRules;
import com.coderetreat.gol.engine.rules.IGameOfLifeRules;
import com.coderetreat.gol.views.IGridCanvas;

public class GameOfLifeActivity extends Activity {

    private IGridEngine gridEngine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final IGridCanvas gridCanvas = (IGridCanvas) findViewById(R.id.gameOfLifeCanvas);
        gridCanvas.drawGrid();

        final IGrid grid = gridCanvas.getGrid();
        IGameOfLifeRules rules = new GameOfLifeRules(grid);

        gridEngine = new GridEngine(rules);

        findViewById(R.id.nextStep).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridEngine.processNextGeneration(grid);
                gridCanvas.drawGrid();
            }
        });


    }
}
