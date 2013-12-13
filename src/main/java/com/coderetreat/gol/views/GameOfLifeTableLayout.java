package com.coderetreat.gol.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import com.coderetreat.gol.R;
import com.coderetreat.gol.engine.Grid;
import com.coderetreat.gol.engine.IGrid;
import com.coderetreat.gol.models.Cell;

public class GameOfLifeTableLayout extends TableLayout implements IGridCanvas, View.OnClickListener {

    private IGrid grid;

    private final int width = 10;
    private final int height = 10;

    private final TableLayout tableLayout;

    public GameOfLifeTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.game_of_life_grid, this);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
    }

    @Override
    public void drawGrid() {
        if (grid == null) {
            grid = new Grid(width, height);
        }
        drawTable();

    }

    @Override
    public IGrid getGrid() {
        return grid;
    }

    @Override
    public void changeManuallyLifeOfCell(Cell.Position position) {
        try {
            Cell cellForPosition = grid.getCellForPosition(position);
            cellForPosition.setAlive(!cellForPosition.isAlive());

            //TODO: why is invalidating the table layout not working?
            TableRow row = (TableRow) tableLayout.getChildAt(position.getY());
            View cell = row.getChildAt(position.getX());
            cell.invalidate();


        } catch (IllegalArgumentException argumentException) {
            Toast.makeText(getContext(), "No cell at this position", Toast.LENGTH_LONG).show();
        }
    }

    private void drawTable() {
        tableLayout.removeAllViews();

        for (int y = 0; y < height; y++) {
            final TableRow row = new TableRow(getContext());
            tableLayout.addView(row);

            for (int x = 0; x < width; x++) {
                Cell cellForView = grid.getCellForPosition(new Cell.Position(x, y));
                final CellView cellView = new CellView(getContext(), cellForView, this);
                row.addView(cellView);
            }
        }
        invalidate();
    }

    @Override
    public void onClick(View view) {
        Cell cell = ((CellView) view).getCell();
        changeManuallyLifeOfCell(cell.getPosition());
    }
}
