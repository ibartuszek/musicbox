package hu.elte.musicbox.command;

import hu.elte.musicbox.song.Song;

public class Result {
    private final Song song;
    private final String message;
    private final CommandType commandType;

    private Result(final Song song, final String message, final CommandType commandType) {
        this.song = song;
        this.message = message;
        this.commandType = commandType;
    }

    static Result createResult(final Song song, final String message, final CommandType commandType) {
        return new Result(song, message, commandType);
    }

    public Song getSong() {
        return song;
    }

    public String getMessage() {
        return message;
    }

    public CommandType getCommandType() {
        return commandType;
    }
}
