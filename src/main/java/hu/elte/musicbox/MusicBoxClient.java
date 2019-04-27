package hu.elte.musicbox;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;

public class MusicBoxClient implements AutoCloseable {

    private final Socket socket;
    private final Scanner scanner;
    private final MidiChannel channel;
    private final PrintWriter printWriter;

    MusicBoxClient(ServerSocket serverSocket) throws Exception {
        socket = serverSocket.accept();
        scanner = new Scanner(socket.getInputStream());
        printWriter = new PrintWriter(socket.getOutputStream());
        Synthesizer synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        channel = synthesizer.getChannels()[0];
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

    public MidiChannel getChannel() {
        return channel;
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }
}
