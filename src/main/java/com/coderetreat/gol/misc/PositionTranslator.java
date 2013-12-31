package com.coderetreat.gol.misc;

import com.coderetreat.gol.models.Cell;

public class PositionTranslator {

    private final int width;
    private final int height;

    public PositionTranslator(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int translateToSingleDimension(Cell.Position position){
        int posX = position.getX();
        int posY = position.getY();

        int result = posY * width + posX;
        return result;
    }

    public Cell.Position translateToPosition(int position){
        int posX = position < width ? position : position % width;
        int posY = position / height;

        return new Cell.Position(posX,posY);
    }
}
