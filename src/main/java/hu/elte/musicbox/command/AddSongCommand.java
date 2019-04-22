package hu.elte.musicbox.command;

import hu.elte.musicbox.song.Song;

public class AddSongCommand implements Command {

    private final CommandType commandType;
    private final String title;
    private final String rawSong;

    AddSongCommand(final String title, final String rawSong) {
        this.commandType = CommandType.ADD;
        this.title = title;
        this.rawSong = rawSong;
    }

    @Override
    public Song execute() {
        return null;
    }

}
