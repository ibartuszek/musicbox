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

    private ChangeCommand(ChangeCommandBuilder builder) {
        this.commandType = CommandType.CHANGE;
        this.songId = builder.songId;
        this.tempo = builder.tempo;
        this.noteModifierFactor = builder.noteModifierFactor;
        this.playList = builder.playList;
        this.songTransformer = builder.songTransformer;
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
        return Result.createResult(song, null, commandType);
    }

    static class ChangeCommandBuilder {
        private Long songId;
        private int tempo;
        private int noteModifierFactor;
        private ConcurrentMap<Long, Song> playList;
        private SongTransformer songTransformer;

        ChangeCommandBuilder() {
        }

        ChangeCommand build() {
            return new ChangeCommand(this);
        }

        ChangeCommandBuilder withSongId(final Long songId) {
            this.songId = songId;
            return this;
        }

        ChangeCommandBuilder withTempo(final int tempo) {
            this.tempo = tempo;
            return this;
        }

        ChangeCommandBuilder withNoteModifier(final int noteModifierFactor) {
            this.noteModifierFactor = noteModifierFactor;
            return this;
        }

        ChangeCommandBuilder withPlayList(final ConcurrentMap<Long, Song> playList) {
            this.playList = playList;
            return this;
        }

        ChangeCommandBuilder withSongTransformer(final SongTransformer songTransformer) {
            this.songTransformer = songTransformer;
            return this;
        }

    }
}
