package com.coderetreat.gol.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import com.coderetreat.gol.engine.Grid;
import com.coderetreat.gol.engine.IGrid;
import com.coderetreat.gol.models.Cell;

public class GameOfLifeGrid extends GridView implements IGridCanvas{
    final IGrid grid;
    final BaseAdapter gridAdapter;

    int width = 20;
    int height = 20;

    private final int cellSize;

    public GameOfLifeGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        setNumColumns(width);

        cellSize = getCellSize(context, attrs);

        this.grid = new Grid(width, height);
        this.gridAdapter = new GridAdapter();
        setAdapter(gridAdapter);
    }

    private int getCellSize(Context context, AttributeSet attrs) {
        int[] attrsArray = new int[] {
                android.R.attr.id, // 0
                android.R.attr.background, // 1
                android.R.attr.layout_width, // 2
                android.R.attr.layout_height // 3
        };
        TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);
        final int defWidth = 200;
        int layout_width = ta.getDimensionPixelSize(2, defWidth);
        return layout_width / width;
    }

    @Override
    public void drawGrid() {
        gridAdapter.notifyDataSetChanged();
    }

    @Override
    public IGrid getGrid() {
        return grid;
    }

    @Override
    public void changeManuallyLifeOfCell(Cell.Position position) {
        Cell cellForPosition = grid.getCellForPosition(position);
        cellForPosition.setAlive(!cellForPosition.isAlive());

        drawGrid();
    }

    private class GridAdapter extends BaseAdapter implements OnClickListener {

        @Override
        public int getCount() {
            return width*height;
        }

        @Override
        public Object getItem(int position) {
            int posX = position < grid.getWidth() ? position : position % grid.getWidth();
            int posY = position / grid.getHeight();

            return grid.getCellForPosition(new Cell.Position(posX,posY));
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            final CellView cellView = new CellView(getContext(), (Cell) getItem(position), this, cellSize);
            return cellView;
        }

        @Override
        public void onClick(View view) {
            Cell cell = ((CellView) view).getCell();
            GameOfLifeGrid.this.changeManuallyLifeOfCell(cell.getPosition());
        }
    }
}
