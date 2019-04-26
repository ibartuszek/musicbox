package hu.elte.musicbox;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;

import lombok.Data;

@Data
public class MusicBoxClient implements AutoCloseable {

    private Socket socket;
    private Scanner scanner;
    private MidiChannel channel;
    private PrintWriter printWriter;

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

}
