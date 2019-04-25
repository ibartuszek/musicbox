package hu.elte.musicbox.command;

import hu.elte.musicbox.song.Song;

public class Result {
    private final Song song;
    private final String message;

    private Result(final Song song, final String message) {
        this.song = song;
        this.message = message;
    }

    static Result createResult(final Song song, final String message) {
        return new Result(song, message);
    }

    public Song getSong() {
        return song;
    }

    public String getMessage() {
        return message;
    }
}
