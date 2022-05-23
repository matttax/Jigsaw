import javax.swing.*;
import java.awt.*;

public class EndBox extends JFrame {
    Button exitButton;
    JLabel[] labels;

    EndBox(String[] texts, int width, int height) {
        labels = new JLabel[3];
        setSize(width, height);
        setLayout(new BorderLayout());
        JPanel textsPanel = setTexts(texts);
        JPanel subPanel = setButtons();
        add(subPanel, BorderLayout.SOUTH);
        add(textsPanel, BorderLayout.CENTER);
    }

    private JPanel setButtons() {
        JPanel panel = new JPanel();
        exitButton = new Button("Exit");
        exitButton.addActionListener(e -> this.dispose());
        exitButton.setPreferredSize(new Dimension(getWidth() / 3, getHeight() / 5));
        exitButton.addActionListener(e -> {
            this.dispose();
            System.exit(0);
        });
        panel.add(exitButton);
        return panel;
    }

    private JPanel setTexts(String[] texts) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        Font myFont = new Font(Font.MONOSPACED, Font.PLAIN, 20);
        for (int i = 0; i < 3; i++) {
            labels[i] = new JLabel(texts[i]);
            labels[i].setFont(myFont);
            labels[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(labels[i]);
        }
        return panel;
    }
}
