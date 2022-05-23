import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String... args) throws IOException, InterruptedException {
        Socket socket = new Socket("localhost", 5000);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        boolean isFirst = dis.readBoolean();
        Registration reg = new Registration(isFirst, dos);
        reg.setVisible(true);

        dis.readBoolean();


        String name = reg.getPlayerName();
        int time = reg.getTime();
        dos.writeUTF(name);
        if (isFirst)
            dos.writeInt(time);

        time = dis.readInt();
        int finalTime = time;
        System.out.println(finalTime);
        EventQueue.invokeLater(() -> {
            GameBox blocksGame = new GameBox(name, dis, dos, finalTime);
            blocksGame.setTitle(name);
            blocksGame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            blocksGame.setVisible(true);
        });
    }
}
