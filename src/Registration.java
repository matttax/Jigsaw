import javax.swing.*;
import java.awt.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class Registration extends JDialog {

    static Font font = new Font("SansSerif", Font.BOLD, 20);
    private final JPanel panel;
    JSpinner numeric;
    JTextField text;
    private String playerName;
    private int time;

    public Registration(boolean isFirst, DataOutputStream dos) {
        time = 0;
        setSize(300, 300);
        setLayout(new BorderLayout());

        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 20));
        initNameForm();
        if (isFirst)
            initTimeForm();

        Button button = new Button("Sign in");
        button.setFont(font);
        button.addActionListener(e -> {
            playerName = text.getText();
            if (isFirst)
                time = (int) Math.round((Double) numeric.getValue());
            try {
                dos.writeBoolean(true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            dispose();
        });
        panel.add(button);
        add(panel);
        setVisible(true);
    }

    private void initNameForm() {
        panel.add(new JLabel("Name:"));
        text = new JTextField();
        text.setFont(font);
        panel.add(text);
    }

    private void initTimeForm() {
        panel.add(new JLabel("Time (seconds):"));
        SpinnerNumberModel model = new SpinnerNumberModel(30, 20, 600.0, 10);
        numeric = new JSpinner(model);
        numeric.setFont(font);
        panel.add(numeric);
    }

    public String getPlayerName() {
        return Objects.equals(playerName, "") ? "player" : playerName;
    }

    public int getTime() {
        return time;
    }
}
