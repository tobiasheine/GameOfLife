package com.coderetreat.gol.grid.cell;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.widget.GridView;
import com.coderetreat.gol.grid.cell.Cell;

public class CellView extends View{

    private final Cell cell;

    public CellView(Context context, Cell cell, OnClickListener onClickListener, int cellDimensions) {
        super(context);
        this.cell = cell;

        setOnClickListener(onClickListener);

        GridView.LayoutParams params = new GridView.LayoutParams(cellDimensions,cellDimensions);
        setLayoutParams(params);
    }

    public Cell getCell(){
        return cell;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundColor(cell.isAlive() ? Color.BLUE : Color.WHITE);
    }
}
