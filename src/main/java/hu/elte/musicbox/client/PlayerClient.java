package hu.elte.musicbox.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.Scanner;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import hu.elte.musicbox.song.NoteTransformer;

public class PlayerClient {

    private static final String HOST = "localhost";
    private static final int PORT = 40000;
    private final Synthesizer synthesizer;
    private final MidiChannel midiChannel;
    private final NoteTransformer noteTransformer;
    private Integer beat;
    private Long passedTime;
    private Integer lastNote;

    PlayerClient() throws MidiUnavailableException {
        lastNote = null;
        beat = null;
        passedTime = null;
        synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        midiChannel = synthesizer.getChannels()[0];
        noteTransformer = NoteTransformer.createNoteTransformer();
    }

    public static void main(String[] args) throws IOException, MidiUnavailableException {
        try (Socket server = new Socket(HOST, PORT)) {
            Scanner serverScanner = new Scanner(server.getInputStream());
            PrintWriter serverPrintWriter = new PrintWriter(server.getOutputStream());
            Scanner clientInput = new Scanner(System.in);

            String command = clientInput.nextLine();
            PlayerClient playerClient = new PlayerClient();
            if (validate(command)) {
                serverPrintWriter.println(command);
                serverPrintWriter.flush();

                while (serverScanner.hasNext()) {
                    String message = serverScanner.next();
                    if ("FIN".equals(message)) {
                        System.out.print(message);
                    } else if ("playing".contains(message)) {
                        System.out.print(message + " ");
                        System.out.println(serverScanner.next());
                    } else {
                        System.out.print(message + " ");
                        System.out.print(serverScanner.next() + " ");
                        playNote(message, playerClient);
                    }
                }
                playerClient.synthesizer.close();
            } else {
                System.out.println(MessageFormat.format("Invalid type of command: {0}", command));
            }
        }
    }

    private static boolean validate(String command) {
        String[] line = command.split(" ");

        boolean result = line.length == 4 &&
            "play".equals(line[0]) &&
            checkIntegerArgument(line[1]) &&
            checkIntegerArgument(line[2]) &&
            !"".equals(line[3]);

        return result;
    }

    private static boolean checkIntegerArgument(String argument) {
        try {
            int value = Integer.parseInt(argument);
            if (value < 1) {
                return false;
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    private static void playNote(final String message, final PlayerClient playerClient) {
        if (playerClient.lastNote != null) {
            playerClient.midiChannel.noteOff(playerClient.lastNote);
        }
        playerClient.lastNote = playerClient.noteTransformer.calculateNoteValue(message.split(" ")[0]);
        if (playerClient.passedTime == null) {
            playerClient.passedTime = System.currentTimeMillis();
            playerClient.midiChannel.noteOn(playerClient.lastNote, 100);
        } else {
            playerClient.beat = (int) ((System.currentTimeMillis() - playerClient.passedTime) / 8);
            playerClient.passedTime = System.currentTimeMillis();
            playerClient.midiChannel.noteOn(playerClient.lastNote, playerClient.beat);
        }
    }

}
