package com.coderetreat.gol.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.widget.TableRow;
import com.coderetreat.gol.models.Cell;

public class CellView extends View{

    private final Cell cell;

    public CellView(Context context, Cell cell, OnClickListener onClickListener) {
        super(context);
        this.cell = cell;
        setOnClickListener(onClickListener);

        TableRow.LayoutParams params = new TableRow.LayoutParams(50,50);
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
