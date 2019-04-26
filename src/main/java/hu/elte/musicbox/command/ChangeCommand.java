package hu.elte.musicbox.command;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import hu.elte.musicbox.song.Note;
import hu.elte.musicbox.song.Song;
import hu.elte.musicbox.song.SongTransformer;

public class ChangeCommand implements Command {

    private final CommandType commandType;
    private final Long songId;
    private final int tempo;
    private final int noteModifierFactor;
    private final ConcurrentMap<String, Song> songStore;
    private final ConcurrentMap<Long, Song> playList;
    private final SongTransformer songTransformer;

    private ChangeCommand(ChangeCommandBuilder builder) {
        this.commandType = CommandType.CHANGE;
        this.songId = builder.songId;
        this.tempo = builder.tempo;
        this.noteModifierFactor = builder.noteModifierFactor;
        this.songStore = builder.songStore;
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
            String title = song.getTitle();
            Note originalFirstNote = getOriginalFirstNote(title);
            Note modifiedFirstNote = getModifiedFirstNote();
            float modifiedTempo = (float) this.tempo;
            int noteModifierFactor = this.noteModifierFactor;
            if (originalFirstNote != null && modifiedFirstNote != null) {
                modifiedTempo = songTransformer.getCorrectedTempo(originalFirstNote, modifiedFirstNote, modifiedTempo);
                noteModifierFactor = songTransformer.
                    getCorrectedNoteModifierFactor(originalFirstNote, modifiedFirstNote, noteModifierFactor);
            }
            songTransformer.updateSongData(song.getSongData(), modifiedTempo, noteModifierFactor);
        }
        return Result.createResult(song, null, commandType);
    }

    private Note getOriginalFirstNote(final String title) {
        Note note = null;
        if (songStore.containsKey(title)) {
            note = getFirstNoteFromSongData(songStore.get(title));
        }
        return note;
    }

    private Note getModifiedFirstNote() {
        Note note = null;
        if (playList.containsKey(songId)) {
            note = getFirstNoteFromSongData(playList.get(songId));
        }
        return note;
    }

    private Note getFirstNoteFromSongData(Song song) {
        Optional<Note> first = song.getSongData().stream()
            .filter(note -> note.getNoteValue() != null)
            .findFirst();
        return first.orElse(null);
    }

    static class ChangeCommandBuilder {
        private Long songId;
        private int tempo;
        private int noteModifierFactor;
        private ConcurrentMap<String, Song> songStore;
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

        ChangeCommandBuilder withSongStore(final ConcurrentMap<String, Song> songStore) {
            this.songStore = songStore;
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
