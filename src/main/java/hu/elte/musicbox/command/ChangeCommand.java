package hu.elte.musicbox.command;

import java.util.concurrent.ConcurrentMap;

import hu.elte.musicbox.song.Song;
import hu.elte.musicbox.song.SongTransformer;

public class ChangeCommand implements Command {

    private final CommandType commandType;
    private final Long songId;
    private final int tempo;
    private final int noteModifierFactor;
    private final ConcurrentMap<Long, Song> playList;
    private final SongTransformer songTransformer;

    ChangeCommand(final String[] arguments, final ConcurrentMap<Long, Song> playList, final SongTransformer songTransformer) {
        this.commandType = CommandType.CHANGE;
        this.songId = Long.parseLong(arguments[1]);
        this.tempo = Integer.parseInt(arguments[2]);
        this.noteModifierFactor = arguments[3] != null ? Integer.parseInt(arguments[3]) : 0;
        this.playList = playList;
        this.songTransformer = songTransformer;
    }

    @Override
    public Result execute() {
        Song song;
        if (!playList.containsKey(songId)) {
            song = null;
        } else {
            song = playList.get(songId);
            songTransformer.updateSongData(song.getSongData(), tempo, noteModifierFactor);
        }
        return Result.createResult(song, null);
    }
}
