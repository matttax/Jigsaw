import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;

public class Blocks {
    Client client;
    /**
     * Class stores information about field and current figure state.
     */
    public Blocks(Client client) {
        this.client = client;
        seconds = 30;
        cells = new int[9][9];
        figureMatrix = getMatrix();
        figuresPlaced = 0;
        fieldCovered = 0;
        timer.start();
    }

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

    Timer timer = new Timer(1000, e -> seconds--);

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
        figureMatrix = getMatrix();
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

    private int[][] getMatrix() {
        int[][] matrix = new int[3][3];
        try {
            client.dos.writeUTF("fig");
            byte[] byteMatrix = client.dis.readNBytes(9);
            for (int i = 0, b = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++, b++) {
                    matrix[i][j] = byteMatrix[b];
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return matrix;
    }
}
