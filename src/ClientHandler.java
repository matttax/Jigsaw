import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClientHandler implements Runnable {
    double score;
    public static List<DataOutputStream> outputStreams = new ArrayList<>();
    public static List<Double> scores = new ArrayList<>();
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.input = new DataInputStream(new DataInputStream(socket.getInputStream()));
            this.output = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            outputStreams.add(output);
        } catch (IOException e) {
            closeEverything();
        }
    }

    public void closeEverything() {
        try {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = input.readUTF();
                if (messageFromClient.equals("fig")) {
                    output.write(generateMatrix());
                } else if (messageFromClient.equals("start")) {
                        for (var out : outputStreams)
                            out.writeBoolean(true);
                } else if (messageFromClient.startsWith("placed")) {
                    score = Integer.parseInt(messageFromClient.substring(6));
                } else if (messageFromClient.startsWith("covered")) {
                    score *= Double.parseDouble(messageFromClient.substring(7));
                    if ((long) scores.size() == 1) {
                        output.writeBoolean(score >= scores.get(0));
                        for (var out : outputStreams)
                            if (!out.equals(output))
                                out.writeBoolean(score <= scores.get(0));
                    }
                    scores.add(score);
                }
                if (scores.size() == 2) {
                    scores.clear();
                    outputStreams.clear();
                    Server.players = 0;
                    Server.time = 0;
                }
            } catch (IOException e) {
                closeEverything();
                break;
            }
        }
    }

    private byte[] generateMatrix() {
        return switch (Math.abs(new Random().nextInt()) % 31) {
            // All possible figures.
            case 0  -> new byte[]{1, 0, 0, 0, 0, 0, 0, 0, 0};
            case 1  -> new byte[]{1, 1, 0, 1, 0, 0, 1, 0, 0};
            case 2  -> new byte[]{1, 0, 0, 1, 1, 1, 0, 0, 0};
            case 3  -> new byte[]{0, 1, 0, 0, 1, 0, 1, 1, 0};
            case 4  -> new byte[]{1, 1, 1, 0, 0, 1, 0, 0, 0};
            case 5  -> new byte[]{1, 1, 0, 0, 1, 0, 0, 1, 0};
            case 6  -> new byte[]{0, 0, 1, 1, 1, 1, 0, 0, 0};
            case 7  -> new byte[]{1, 0, 0, 1, 0, 0, 1, 1, 0};
            case 8  -> new byte[]{1, 1, 1, 1, 0, 0, 0, 0, 0};
            case 9  -> new byte[]{1, 0, 0, 1, 1, 0, 0, 1, 0};
            case 10 -> new byte[]{0, 1, 1, 1, 1, 0, 0, 0, 0};
            case 11 -> new byte[]{0, 1, 0, 1, 1, 0, 1, 0, 0};
            case 12 -> new byte[]{1, 1, 0, 0, 1, 1, 0, 0, 0};
            case 13 -> new byte[]{1, 0, 0, 1, 0, 0, 1, 1, 1};
            case 14 -> new byte[]{1, 1, 1, 0, 0, 1, 0, 0, 1};
            case 15 -> new byte[]{1, 1, 1, 1, 0, 0, 1, 0, 0};
            case 16 -> new byte[]{0, 0, 1, 0, 0, 1, 1, 1, 1};
            case 17 -> new byte[]{0, 1, 0, 0, 1, 0, 1, 1, 1};
            case 18 -> new byte[]{1, 1, 1, 0, 1, 0, 0, 1, 0};
            case 19 -> new byte[]{1, 0, 0, 1, 1, 1, 1, 0, 0};
            case 20 -> new byte[]{0, 0, 1, 1, 1, 1, 0, 0, 1};
            case 21 -> new byte[]{1, 1, 1, 0, 0, 0, 0, 0, 0};
            case 22 -> new byte[]{1, 0, 0, 1, 0, 0, 1, 0, 0};
            case 23 -> new byte[]{1, 1, 0, 1, 0, 0, 0, 0, 0};
            case 24 -> new byte[]{1, 1, 0, 0, 1, 0, 0, 0, 0};
            case 25 -> new byte[]{1, 0, 0, 1, 1, 0, 0, 0, 0};
            case 26 -> new byte[]{0, 1, 0, 1, 1, 0, 1, 0, 0};
            case 27 -> new byte[]{0, 1, 0, 1, 1, 1, 0, 0, 0};
            case 28 -> new byte[]{1, 1, 1, 0, 1, 0, 0, 0, 0};
            case 29 -> new byte[]{1, 0, 0, 1, 1, 0, 1, 0, 0};
            case 30 -> new byte[]{0, 1, 0, 1, 1, 0, 0, 1, 0};
            default -> null;
        };
    }
}
