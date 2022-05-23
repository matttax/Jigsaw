import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;

public class GameBox extends JFrame {
    /**
     * Launch Blocks game.
     */
    public GameBox(String name, DataInputStream input, DataOutputStream output, int time) {
        setSize(500, 500);
        Playground pg = new Playground(name, input, output, time);
        add(pg);
        Timer timer = new Timer(1005 * time, e -> {
            this.dispose();
            EndBox gameOver = new EndBox(pg.getInfo(), this.getWidth(), this.getHeight());
            gameOver.setVisible(true);
        });
        timer.start();
    }
}
