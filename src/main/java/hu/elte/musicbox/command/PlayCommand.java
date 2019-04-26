package hu.elte.musicbox.command;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentMap;

import hu.elte.musicbox.song.Song;
import hu.elte.musicbox.song.SongTransformer;

public class PlayCommand implements Command {

    private static final String RESULT_MESSAGE = "playing {0}";
    private static final String END_SONG = "FIN";
    private final CommandType commandType;
    private final int tempo;
    private final int noteModifierFactor;
    private final String title;
    private final Long songId;
    private final ConcurrentMap<String, Song> songStore;
    private final ConcurrentMap<Long, Song> playList;
    private final SongTransformer songTransformer;

    private PlayCommand(PlayCommandBuilder builder) {
        this.commandType = CommandType.PLAY;
        this.tempo = builder.tempo;
        this.noteModifierFactor = builder.noteModifierFactor;
        this.title = builder.title;
        this.songId = builder.songId;
        this.songStore = builder.songStore;
        this.playList = builder.playList;
        this.songTransformer = builder.songTransformer;
    }

    @Override
    public Result execute() {
        Song song;
        String message;
        if (!songStore.containsKey(title)) {
            song = null;
            message = END_SONG;
        } else {
            Song rawSong = songStore.get(title);
            song = Song.createSong(songId, title);
            song.getSongData().addAll(rawSong.getSongData());
            songTransformer.updateSongData(song.getSongData(), tempo, noteModifierFactor);
            playList.put(songId, song);
            message = MessageFormat.format(RESULT_MESSAGE, songId);
        }
        return Result.createResult(song, message, commandType);
    }

    static class PlayCommandBuilder {
        private int tempo;
        private int noteModifierFactor;
        private String title;
        private Long songId;
        private ConcurrentMap<String, Song> songStore;
        private ConcurrentMap<Long, Song> playList;
        private SongTransformer songTransformer;

        PlayCommandBuilder() {
        }

        PlayCommand build() {
            return new PlayCommand(this);
        }

        PlayCommandBuilder withTempo(final int tempo) {
            this.tempo = tempo;
            return this;
        }

        PlayCommandBuilder withNoteModifier(final int noteModifierFactor) {
            this.noteModifierFactor = noteModifierFactor;
            return this;
        }

        PlayCommandBuilder withTitle(final String title) {
            this.title = title;
            return this;
        }

        PlayCommandBuilder withSongId(final Long songId) {
            this.songId = songId;
            return this;
        }

        PlayCommandBuilder withSongStore(final ConcurrentMap<String, Song> songStore) {
            this.songStore = songStore;
            return this;
        }

        PlayCommandBuilder withPlayList(final ConcurrentMap<Long, Song> playList) {
            this.playList = playList;
            return this;
        }

        PlayCommandBuilder withSongTransformer(final SongTransformer songTransformer) {
            this.songTransformer = songTransformer;
            return this;
        }
    }
}
