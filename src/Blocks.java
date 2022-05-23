import javax.swing.*;
import java.util.Random;

public class Blocks {
    private int figuresPlaced;
    private double fieldCovered;
    public int seconds;
    private final int[][] cells;
    public int[][] figureMatrix;

    public int getFiguresPlaced() {
        return figuresPlaced;
    }

    public double getFieldCovered() {
        return fieldCovered;
    }

    Timer timer = new Timer(1000, e -> {
        seconds--;
    });

    /**
     * Class stores information about field and current figure state.
     */
    public Blocks() {
        seconds = 30;
        cells = new int[9][9];
        figureMatrix = generateMatrix();
        figuresPlaced = 0;
        fieldCovered = 0;
        timer.start();
    }

    /**
     * @return Field cells map.
     */
    public int getCell(int i, int j) {
        if (i > 8 || j > 8 || i < 0 || j < 0)
            return -1;
        return cells[i][j];
    }

    private boolean isInsertable(int x, int y) {
        for (int i = x; i < x + 3; i++)
            for (int j = y; j < y + 3; j++)
                if (Math.abs(getCell(i, j)) == 1 && figureMatrix[i - x][j - y] == 1)
                    return false;
        return true;
    }

    public void placeFigure(int x, int y) {
        if (!isInsertable(x, y))
            return;
        for (int i = x; i < x + 3; i++) {
            for (int j = y; j < y + 3; j++) {
                if (figureMatrix[i - x][j - y] == 1) {
                    cells[i][j] = 1;
                    fieldCovered += 1.0 / 81.0;
                }
            }
        }
        figuresPlaced++;
        updateFigureMatrix();
    }

    /**
     * Clear the field. All cells become open.
     */
    public void resetCells() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j] = 0;
            }
        }
        figuresPlaced = 0;
        fieldCovered = 0;
        seconds = 30;
        timer.restart();
    }

    /**
     * Generates new figure.
     */
    private void updateFigureMatrix() {
        figureMatrix = generateMatrix();
    }

    private int[][] generateMatrix() {
        return switch (Math.abs(new Random().nextInt()) % 31) {
            // All possible figures.
            case 0  -> new int[][]{{1, 0, 0}, {0, 0, 0}, {0, 0, 0}};
            case 1  -> new int[][]{{1, 1, 0}, {1, 0, 0}, {1, 0, 0}};
            case 2  -> new int[][]{{1, 0, 0}, {1, 1, 1}, {0, 0, 0}};
            case 3  -> new int[][]{{0, 1, 0}, {0, 1, 0}, {1, 1, 0}};
            case 4  -> new int[][]{{1, 1, 1}, {0, 0, 1}, {0, 0, 0}};
            case 5  -> new int[][]{{1, 1, 0}, {0, 1, 0}, {0, 1, 0}};
            case 6  -> new int[][]{{0, 0, 1}, {1, 1, 1}, {0, 0, 0}};
            case 7  -> new int[][]{{1, 0, 0}, {1, 0, 0}, {1, 1, 0}};
            case 8  -> new int[][]{{1, 1, 1}, {1, 0, 0}, {0, 0, 0}};
            case 9  -> new int[][]{{1, 0, 0}, {1, 1, 0}, {0, 1, 0}};
            case 10 -> new int[][]{{0, 1, 1}, {1, 1, 0}, {0, 0, 0}};
            case 11 -> new int[][]{{0, 1, 0}, {1, 1, 0}, {1, 0, 0}};
            case 12 -> new int[][]{{1, 1, 0}, {0, 1, 1}, {0, 0, 0}};
            case 13 -> new int[][]{{1, 0, 0}, {1, 0, 0}, {1, 1, 1}};
            case 14 -> new int[][]{{1, 1, 1}, {0, 0, 1}, {0, 0, 1}};
            case 15 -> new int[][]{{1, 1, 1}, {1, 0, 0}, {1, 0, 0}};
            case 16 -> new int[][]{{0, 0, 1}, {0, 0, 1}, {1, 1, 1}};
            case 17 -> new int[][]{{0, 1, 0}, {0, 1, 0}, {1, 1, 1}};
            case 18 -> new int[][]{{1, 1, 1}, {0, 1, 0}, {0, 1, 0}};
            case 19 -> new int[][]{{1, 0, 0}, {1, 1, 1}, {1, 0, 0}};
            case 20 -> new int[][]{{0, 0, 1}, {1, 1, 1}, {0, 0, 1}};
            case 21 -> new int[][]{{1, 1, 1}, {0, 0, 0}, {0, 0, 0}};
            case 22 -> new int[][]{{1, 0, 0}, {1, 0, 0}, {1, 0, 0}};
            case 23 -> new int[][]{{1, 1, 0}, {1, 0, 0}, {0, 0, 0}};
            case 24 -> new int[][]{{1, 1, 0}, {0, 1, 0}, {0, 0, 0}};
            case 25 -> new int[][]{{1, 0, 0}, {1, 1, 0}, {0, 0, 0}};
            case 26 -> new int[][]{{0, 1, 0}, {1, 1, 0}, {1, 0, 0}};
            case 27 -> new int[][]{{0, 1, 0}, {1, 1, 1}, {0, 0, 0}};
            case 28 -> new int[][]{{1, 1, 1}, {0, 1, 0}, {0, 0, 0}};
            case 29 -> new int[][]{{1, 0, 0}, {1, 1, 0}, {1, 0, 0}};
            case 30 -> new int[][]{{0, 1, 0}, {1, 1, 0}, {0, 1, 0}};
            default -> null;
        };
    }
}
