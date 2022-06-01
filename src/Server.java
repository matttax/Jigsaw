import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static int players;
    public static int time;
    public static String firstName;
    public static String secondName;
    private final ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        players = 0;
        time = 0;
        this.serverSocket = serverSocket;
        firstName = null;
        secondName = null;
    }

    public void startServer() {
        try {
            ClientHandler clientHandler;
            do {
                if (players == 2) {
                    continue;
                }
                Socket player = serverSocket.accept();
                players++;
                DataInputStream inp = new DataInputStream(player.getInputStream());
                DataOutputStream out = new DataOutputStream(player.getOutputStream());
                out.writeInt(time);
                out.writeBoolean(inp.readBoolean());
                String name = inp.readUTF();
                if (time == 0)
                    firstName = name;
                else secondName = name;
                time = inp.readInt();
                clientHandler = new ClientHandler(player);
                Thread thread = new Thread(clientHandler);
                thread.start();
            } while (!serverSocket.isClosed());
        } catch (IOException iex) {
            closeServer();
        }
    }

    public void closeServer() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String... args) throws IOException {
        ServerSocket socket = new ServerSocket(5000);
        Server server = new Server(socket);
        server.startServer();
    }
}
