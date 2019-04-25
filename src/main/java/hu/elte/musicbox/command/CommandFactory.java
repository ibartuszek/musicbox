package hu.elte.musicbox.command;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import hu.elte.musicbox.song.LyricsTransformer;
import hu.elte.musicbox.song.Song;
import hu.elte.musicbox.song.SongTransformer;

public class CommandFactory {

    private static final String CLIENT_INPUT_SEPARATOR = " ";
    private final SongTransformer songTransformer;
    private final LyricsTransformer lyricsTransformer;
    private final AtomicLong songId;

    private CommandFactory(final SongTransformer songTransformer,
        final LyricsTransformer lyricsTransformer, final AtomicLong songId) {
        this.songTransformer = songTransformer;
        this.lyricsTransformer = lyricsTransformer;
        this.songId = songId;
    }

    public static CommandFactory createCommandFactory(final SongTransformer songTransformer,
        final LyricsTransformer lyricsTransformer, final AtomicLong songId) {
        return new CommandFactory(songTransformer, lyricsTransformer, songId);
    }

    public boolean isReady(final List<String> clientInput) {
        boolean result = false;
        final String firstWord = clientInput.get(0).split(CLIENT_INPUT_SEPARATOR)[0];
        if (checkInputContainsCommand(firstWord, CommandType.ADD) ||
            checkInputContainsCommand(firstWord, CommandType.ADD_LYRICS)) {
            result = clientInput.size() == 2;
        } else if (checkInputContainsCommand(firstWord, CommandType.PLAY) ||
            checkInputContainsCommand(firstWord, CommandType.CHANGE) ||
            checkInputContainsCommand(firstWord, CommandType.STOP)) {
            result = true;
        }
        return result;
    }

    private boolean checkInputContainsCommand(final String firstWord, final CommandType commandType) {
        return firstWord.contains(commandType.getCommandName()) &&
            firstWord.length() == commandType.getCommandName().length();
    }

    public Command createCommand(final List<String> clientInput, final ConcurrentMap<String, Song> songStore,
        final ConcurrentMap<String, Song> playList) {
        final String firstLine = clientInput.get(0);
        final String[] commandArguments = firstLine.split(CLIENT_INPUT_SEPARATOR);
        final CommandType commandType = CommandType.getTypeByName(commandArguments[0]);
        Command command = null;

        if (commandType.equals(CommandType.ADD)) {
            command = new AddSongCommand(commandArguments[1], clientInput.get(1), songTransformer, songStore);
        } else if (commandType.equals(CommandType.ADD_LYRICS)) {
            command = new AddLyricsCommand(commandArguments[1], clientInput.get(1), lyricsTransformer, songStore);
        } else if (commandType.equals(CommandType.PLAY)) {
            command = new PlayCommand(commandArguments, songStore, playList, songId.incrementAndGet(), songTransformer);
        } else if (commandType.equals(CommandType.CHANGE)) {
            // TODO
            System.out.println(CommandType.CHANGE);
        } else if (commandType.equals(CommandType.STOP)) {
            // TODO
            System.out.println(CommandType.STOP);
        }

        return command;
    }
}
