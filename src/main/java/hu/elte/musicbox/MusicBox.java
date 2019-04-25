package hu.elte.musicbox;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import hu.elte.musicbox.command.CommandFactory;
import hu.elte.musicbox.song.Song;
import hu.elte.musicbox.song.SongTransformer;

public class MusicBox {

    public static final int PORT = 40000;

    public static void main(final String[] args) throws Exception {

        final Set<ClientData> otherClients = new HashSet<>();
        final ConcurrentMap<String, Song> songStore = new ConcurrentHashMap<>();
        final CommandFactory commandFactory = new CommandFactory();
        final SongTransformer songTransformer = SongTransformer.createSongTransformer();

        try (final ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                final ClientData client = new ClientData(serverSocket);
                synchronized (otherClients) {
                    otherClients.add(client);
                    System.out.println("Clinet has accepted");
                }

                new Thread(() -> {
                    List<String> clientInput = new ArrayList<>();
                    while (client.getScanner().hasNextLine()) {
                        String line = client.getScanner().nextLine();
                        clientInput.add(line);

                        synchronized (otherClients) {
                            for (ClientData other : otherClients) {
                                other.getPrintWriter().println(line);
                                other.getPrintWriter().flush();
                            }
                        }

                        if (commandFactory.isReady(clientInput)) {
                            commandFactory.createCommand(clientInput, songTransformer, songStore).execute();
                        }
                    }

                    synchronized (otherClients) {
                        otherClients.remove(client);
                        try {
                            client.close();
                        } catch (Exception e) {
                            // won't happen
                        }
                    }
                }).start();
            }
        }
    }

}
