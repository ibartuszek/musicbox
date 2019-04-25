package hu.elte.musicbox.command;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import hu.elte.musicbox.song.LyricsTransformer;
import hu.elte.musicbox.song.Song;
import hu.elte.musicbox.song.SongTransformer;

public class CommandFactory {

    private static final String CLIENT_INPUT_SEPARATOR = " ";
    private final SongTransformer songTransformer;
    private final LyricsTransformer lyricsTransformer;

    private CommandFactory(final SongTransformer songTransformer,
        final LyricsTransformer lyricsTransformer) {
        this.songTransformer = songTransformer;
        this.lyricsTransformer = lyricsTransformer;
    }

    public static CommandFactory createCommandFactory(final SongTransformer songTransformer,
        final LyricsTransformer lyricsTransformer) {
        return new CommandFactory(songTransformer, lyricsTransformer);
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

    public Command createCommand(final List<String> clientInput, final ConcurrentMap<String, Song> songStore) {
        final String firstLine = clientInput.get(0);
        final String[] commandArguments = firstLine.split(CLIENT_INPUT_SEPARATOR);
        final CommandType commandType = CommandType.getTypeByName(commandArguments[0]);
        Command command = null;

        if (commandType.equals(CommandType.ADD)) {
            command = new AddSongCommand(commandArguments[1], clientInput.get(1), songTransformer, songStore);
        } else if (commandType.equals(CommandType.ADD_LYRICS)) {
            command = new AddLyricsCommand(commandArguments[1], clientInput.get(1), lyricsTransformer, songStore);
        } else if (commandType.equals(CommandType.PLAY)) {
            // TODO
            System.out.println(CommandType.PLAY);
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
