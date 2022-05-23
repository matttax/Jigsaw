import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

class Playground extends JComponent {
    boolean win;
    double width;
    double height;
    double widthIndent;
    double heightIndent;

    DataOutputStream output;
    DataInputStream input;
    String name;
    Blocks blocks;
    private Point previousPoint;
    private double leftX, topY;
    private boolean figureIsCaught;
    private int xClick, yClick;
    JLabel figuresPlacedLabel = new JLabel("Figures placed: 0");
    JLabel fieldCoveredLabel = new JLabel("Field covered: 0%");
    JLabel timerLabel = new JLabel("Time: 00:00");
    Timer timer = new Timer(1000, e -> {
        timerLabel.setText("Time: " + blocks.seconds);
        if (blocks.seconds == 0) {
            try {
                output.writeInt(blocks.getFiguresPlaced());
                output.writeDouble(blocks.getFieldCovered());
                win = input.readBoolean();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    });

    /**
     * Component shows the game process graphically.
     */
    Playground(String name, DataInputStream input, DataOutputStream output, int time) {
        win = false;
        this.output = output;
        this.input = input;
        this.name = name;
        blocks = new Blocks();
        blocks.seconds = time;
        this.addMouseListener(new Playground.ClickListener());
        this.addMouseMotionListener(new Playground.DragListener());
        this.addMouseListener(new Playground.ReleaseListener());

        Font myFont = new Font("Serif", Font.PLAIN, 14);
        figuresPlacedLabel.setFont(myFont);
        fieldCoveredLabel.setFont(myFont);
        timerLabel.setFont(myFont);
        add(figuresPlacedLabel);
        add(fieldCoveredLabel);
        add(timerLabel);

        timer.start();
    }

    public String[] getInfo() {
        String[] info = new String[3];
        info[0] = "Figures placed: " + blocks.getFiguresPlaced();
        info[1] = (int)(blocks.getFieldCovered() * 100) + "% of field covered";
        info[2] = win ? "You win!" : "You lose(";
        return info;
    }

    /**
     * Paint current playground state.
     */
    public void paintComponent(Graphics g) {
        timerLabel.setBounds((int)(widthIndent), (int)(heightIndent + height * 11), 150, 20);
        figuresPlacedLabel.setBounds((int)(widthIndent), (int)(heightIndent + height * 12), 150, 20);
        fieldCoveredLabel.setBounds((int)(widthIndent), (int)(heightIndent + height * 13), 150, 20);

        Graphics2D g2 = (Graphics2D) g;
        width = getWidth() / 11.5;
        height = getHeight() / 17.0;
        widthIndent = getWidth() / 15.0;
        heightIndent = getHeight() / 15.0;
        Rectangle2D currentCell;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                currentCell = new Rectangle2D.Double(widthIndent + j * width, heightIndent + i * height, width, height);
                if (blocks.getCell(i, j) == 1) {
                    g2.fill(currentCell);
                } else {
                    g2.draw(currentCell);
                }
            }
        }
        if (!figureIsCaught) {
            leftX = widthIndent;
            topY = heightIndent;
        }
        for (int i = 11; i < 14; i++) {
            for (int j = 5; j < 8; j++) {
                if (blocks.figureMatrix[i - 11][j - 5] == 1)
                    g2.fill(new Rectangle2D.Double(leftX + j * width, topY + i * height, width, height));
            }
        }
    }

    private class ClickListener extends MouseAdapter {
        /**
         * Check if figure is caught and define which part of it.
         */
        public void mousePressed(MouseEvent e) {
            xClick = -1;
            yClick = -1;
            for (int i = 11; i < 14; i++) {
                for (int j = 5; j < 8; j++) {
                    if (e.getX() >= widthIndent + j * width && e.getX() <= widthIndent + j * width + width &&
                            e.getY() >= heightIndent + i * height && e.getY() <= heightIndent + i * height + height) {
                        xClick = i - 11;
                        yClick = j - 5;
                        previousPoint = e.getPoint();
                        figureIsCaught = true;
                    }
                }
            }
        }
    }

    private class DragListener extends MouseMotionAdapter {
        /**
         * Paint figure where it is currently located.
         */
        public void mouseDragged(MouseEvent e) {
            if (figureIsCaught) {
                leftX += (e.getX() - previousPoint.getX());
                topY += (e.getY() - previousPoint.getY());
                previousPoint = e.getPoint();
                repaint();
            }
        }
    }

    private class ReleaseListener extends MouseAdapter {
        /**
         * Check if figure can be placed in the field.
         */
        public void mouseReleased(MouseEvent e) {
            figureIsCaught = false;
            int x = -1, y = -1;
            detectCell:
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (e.getX() >= widthIndent + j * width && e.getX() <= widthIndent + j * width + width &&
                            e.getY() >= heightIndent + i * height && e.getY() <= heightIndent + i * height + height) {
                        x = i - xClick;
                        y = j - yClick;
                        break detectCell;
                    }
                }
            }
            blocks.placeFigure(x, y);
            leftX = widthIndent;
            topY = heightIndent;
            repaint();
            figuresPlacedLabel.setText("Figures placed: " + blocks.getFiguresPlaced());
            fieldCoveredLabel.setText("Field covered: " + (int)(blocks.getFieldCovered() * 100) + "%");
        }
    }
}
