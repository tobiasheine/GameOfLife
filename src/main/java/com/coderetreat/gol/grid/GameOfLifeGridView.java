package com.coderetreat.gol.grid;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import com.coderetreat.gol.grid.cell.CellView;
import com.coderetreat.gol.misc.PositionTranslator;
import com.coderetreat.gol.grid.cell.Cell;

public class GameOfLifeGridView extends GridView implements IGameOfLifeCanvas {
    private final IGrid grid;
    private final BaseAdapter gridAdapter;
    private final PositionTranslator positionTranslator;
    private final int cellDimension;

    private int width = 20;
    private int height = 20;

    public GameOfLifeGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.cellDimension = getCellDimension(context, attrs);
        this.grid = new Grid(width, height);
        this.gridAdapter = new GridAdapter();
        this.positionTranslator = new PositionTranslator(width,height);

        setNumColumns(width);
        setAdapter(gridAdapter);
    }

    private int getCellDimension(Context context, AttributeSet attrs) {
        int[] attrsArray = new int[] {
                android.R.attr.layout_width, // 0
                android.R.attr.layout_height // 1
        };
        TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);
        final int defWidth = 200;
        int layout_width = ta.getDimensionPixelSize(0, defWidth);
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
    public void changeCellAtPositionManually(Cell.Position position) {
        Cell cellForPosition = grid.getCellForPosition(position);
        cellForPosition.setAlive(!cellForPosition.isAlive());

        getChildAt(positionTranslator.translateToSingleDimension(position)).invalidate();
    }

    private class GridAdapter extends BaseAdapter implements OnClickListener {

        @Override
        public int getCount() {
            return width*height;
        }

        @Override
        public Object getItem(int position) {
            final Cell.Position cellPosition = positionTranslator.translateToPosition(position);
            return grid.getCellForPosition(cellPosition);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            final CellView cellView = new CellView(getContext(), (Cell) getItem(position), this, cellDimension);
            return cellView;
        }

        @Override
        public void onClick(View view) {
            Cell cell = ((CellView) view).getCell();
            GameOfLifeGridView.this.changeCellAtPositionManually(cell.getPosition());
        }
    }
}
