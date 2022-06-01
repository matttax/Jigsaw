import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class GameBox extends JFrame {
    /**
     * Launch Blocks game.
     */
    Timer timer;
    public GameBox(Client client) {
        setSize(500, 500);
        Playground pg = new Playground(client);
        add(pg);
         timer = new Timer(1005 * client.time, e -> {
             dispose();
             EndBox gameOver = new EndBox(pg.getInfo(), getWidth(), getHeight());
             gameOver.setVisible(true);
             timer.stop();
         });
        timer.start();
    }
}
