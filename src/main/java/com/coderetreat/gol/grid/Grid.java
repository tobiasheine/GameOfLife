package com.coderetreat.gol.grid;

import com.coderetreat.gol.grid.cell.Cell;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Grid implements IGrid {
    private final int width;
    private final int height;

    private final Cell[][] internalGrid;

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
                final Cell.Position position = new Cell.Position(x, y);
                final Cell cell = new Cell(position);
                addCell(cell);
            }
        }
    }

    @Override
    public List<Cell> getCellNeighbours(Cell.Position position) {

        List<Cell> neighbours = new ArrayList<Cell>();

        int maxX = Math.min(getWidth(), position.getX() + 2);
        int maxY = Math.min(getHeight(), position.getY() + 2);
        int startX = Math.max(0, position.getX() - 1);
        int startY = Math.max(0, position.getY() - 1);

        for (int x = startX; x < maxX; x++) {
            for (int y = startY; y < maxY; y++) {
                final Cell.Position neighbourPosition = new Cell.Position(x, y);
                if (!neighbourPosition.equals(position)) {
                    neighbours.add(getCellForPosition(new Cell.Position(x, y)));
                }
            }
        }
        return Collections.unmodifiableList(neighbours);

    }

    @Override
    public synchronized Cell getCellForPosition(Cell.Position position) {
        return internalGrid[position.getX()][position.getY()];
    }


    private void addCell(Cell cell) {
        Cell.Position position = cell.getPosition();

        synchronized (this) {
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
