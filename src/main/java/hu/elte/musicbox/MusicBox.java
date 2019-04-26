package hu.elte.musicbox;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import hu.elte.musicbox.command.CommandFactory;
import hu.elte.musicbox.command.CommandType;
import hu.elte.musicbox.command.Result;
import hu.elte.musicbox.song.LyricsTransformer;
import hu.elte.musicbox.song.Song;
import hu.elte.musicbox.song.SongTransformer;

public class MusicBox {

    private static final int PORT = 40000;

    public static void main(final String[] args) throws Exception {

        AtomicLong songId = new AtomicLong();
        Set<MusicBoxClient> otherClients = new HashSet<>();
        ConcurrentMap<String, Song> songStore = new ConcurrentHashMap<>();
        ConcurrentMap<Long, Song> playList = new ConcurrentHashMap<>();
        SongTransformer songTransformer = SongTransformer.createSongTransformer();
        LyricsTransformer lyricsTransformer = LyricsTransformer.createLyricsTransformer();
        CommandFactory commandFactory = CommandFactory.createCommandFactory(songTransformer, lyricsTransformer, songId);

        // For test only:
        initSongStore(commandFactory, songStore);

        try (final ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                final MusicBoxClient client = new MusicBoxClient(serverSocket);
                synchronized (otherClients) {
                    otherClients.add(client);
                }
                new Thread(() -> {
                    List<String> clientInput = new ArrayList<>();
                    while (client.getScanner().hasNextLine()) {
                        String line = client.getScanner().nextLine();
                        clientInput.add(line);
                        if (commandFactory.isReady(clientInput)) {
                            Result result = commandFactory.createCommand(clientInput, songStore, playList).execute();
                            sendAnswerToClient(client, result, songStore, playList);
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

    private static void sendAnswerToClient(final MusicBoxClient client, final Result result,
        final ConcurrentMap<String, Song> songStore, final ConcurrentMap<Long, Song> playList) {
        if (result.getCommandType().equals(CommandType.PLAY)) {
            if (result.getSong() != null) {
                new SongPlayer.SongPlayerBuilder()
                    .withClient(client)
                    .withResult(result)
                    .withSongStore(songStore)
                    .withPlayList(playList)
                    .build()
                    .playSong();
            } else {
                SongPlayer.sendEndMessage(client.getPrintWriter());
            }
        }
    }

    private static void initSongStore(final CommandFactory commandFactory, final ConcurrentMap<String, Song> songStore) {
        List<String> input = new ArrayList<>();

        input.add("add sample");
        input.add("C 4 E 4 C 4 E 4 G 8 G 8 REP 6;1 C/1 4 B 4 A 4 G 4 F 8 A 8 G 4 F 4 E 4 D 4 C 8 C 8");
        commandFactory.createCommand(input, songStore, null).execute();
        input.clear();

        input.add("addlyrics sample");
        input.add("bo ci bo ci tar ka se fü le se far ka o da me gyünk lak ni a hol te jet kap ni");
        commandFactory.createCommand(input, songStore, null).execute();
        input.clear();

        input.add("add sample2");
        input.add("D 1 D 3 D/1 1 D/1 3 C/1 1 C/1 3 C/1 2 C/1 2 D/1 1 D/1 3 C/1 1 Bb 3 A 4 A 2 R 2 REP 15;1 "
            + "Bb 4 A 2 G 2 F 1 F 3 E 2 D 2 G 2 G 2 C/1 2 Bb 2 A 4 D/1 2 R 2 C/1 1 Bb 3 A 2 G 2 G 1 A 3 G 2 "
            + "F 2 A 1 G 3 F# 2 Eb 2 D 4 D 2 R 2");
        commandFactory.createCommand(input, songStore, null).execute();
    }

}
