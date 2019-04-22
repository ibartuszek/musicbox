package hu.elte.musicbox;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import lombok.Data;

@Data
public class ClientData implements AutoCloseable {

    private Socket socket;
    private Scanner scanner;
    private PrintWriter printWriter;

    ClientData(ServerSocket serverSocket) throws Exception {
        socket = serverSocket.accept();
        scanner = new Scanner(socket.getInputStream());
        printWriter = new PrintWriter(socket.getOutputStream());
    }

    public void close() throws Exception {
        if (socket == null) {
            return;
        }
        socket.close();
    }

}
