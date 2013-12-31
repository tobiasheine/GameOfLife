package com.coderetreat.gol.grid;

import com.coderetreat.gol.grid.cell.Cell;

public class Grid implements IGrid{
    private final int width;
    private final int height;

    private final Cell[][]internalGrid;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        internalGrid = new Cell[width][height];
        init();
    }

    @Override
    public void init() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                final Cell.Position position = new Cell.Position(x,y);
                final Cell cell = new Cell(position);
                addCell(cell);
            }
        }
    }

    @Override
    public synchronized Cell getCellForPosition(Cell.Position position){
        return internalGrid[position.getX()][position.getY()];
    }


    private void addCell(Cell cell) {
        Cell.Position position = cell.getPosition();

        synchronized (this){
            internalGrid[position.getX()][position.getY()] = cell;
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

}
