import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    boolean isFirst;
    public Socket socket;
    DataOutputStream dos;
    DataInputStream dis;
    public String name;
    public int time;

    public Client() {
        try {
            this.socket = new Socket("localhost", 5000);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            int serverTime = dis.readInt();
            isFirst = serverTime == 0;
            Registration reg = new Registration(isFirst, dos);
            reg.setVisible(true);
            dis.readBoolean();
            name = reg.getPlayerName();
            time = isFirst ? reg.getTime() : serverTime;
            dos.writeUTF(name);
            dos.writeInt(time);
        } catch (IOException e) {
            closeEverything();
        }
    }

    public void closeEverything() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String... args) throws IOException {
        Client client = new Client();
        if (!client.isFirst)
            client.dos.writeUTF("start");
        System.out.println(client.dis.readBoolean());
        EventQueue.invokeLater(() -> {
            GameBox blocksGame = new GameBox(client);
            blocksGame.setTitle(client.name);
            blocksGame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            blocksGame.setVisible(true);
        });
    }
}
