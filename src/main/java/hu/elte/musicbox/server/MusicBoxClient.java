package hu.elte.musicbox.server;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MusicBoxClient implements AutoCloseable {

    private final Socket socket;
    private final Scanner scanner;
    private final PrintWriter printWriter;

    MusicBoxClient(ServerSocket serverSocket) throws Exception {
        socket = serverSocket.accept();
        scanner = new Scanner(socket.getInputStream());
        printWriter = new PrintWriter(socket.getOutputStream());
    }

    @Override
    public void close() throws Exception {
        if (socket == null) {
            return;
        }
        socket.close();
    }

    public Socket getSocket() {
        return socket;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }

}
