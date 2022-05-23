import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String... args) throws IOException, InterruptedException {
        int time = 0;

        ServerSocket socket = new ServerSocket(5000);

        Socket player1 = socket.accept();
        DataInputStream inp1 = new DataInputStream(player1.getInputStream());
        DataOutputStream out1 = new DataOutputStream(player1.getOutputStream());
        out1.writeBoolean(true);
        out1.writeBoolean(inp1.readBoolean());
        System.out.println(inp1.readUTF() + " connected");
        time = inp1.readInt();
        System.out.println(time);


        Socket player2 = socket.accept();
        DataInputStream inp2 = new DataInputStream(player2.getInputStream());
        DataOutputStream out2 = new DataOutputStream(player2.getOutputStream());
        out2.writeBoolean(false);
        out2.writeBoolean(inp2.readBoolean());
        System.out.println(inp2.readUTF() + " connected");

        out1.writeInt(time);
        out2.writeInt(time);

        int firstFigures = inp1.readInt();
        double firstCovered = inp1.readDouble();
        int secondFigures = inp2.readInt();
        double secondCovered = inp2.readDouble();

        boolean firstWin = firstCovered * firstFigures > secondCovered * secondFigures;
        out1.writeBoolean(firstWin);
        out2.writeBoolean(!firstWin);
    }
}
