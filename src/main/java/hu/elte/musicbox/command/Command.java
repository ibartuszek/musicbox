package hu.elte.musicbox.command;

import hu.elte.musicbox.song.Song;

public interface Command {

    Song execute();

}
