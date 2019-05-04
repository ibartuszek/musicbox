package hu.elte.musicbox.song;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Public responsibility is to transform a raw String to a Song, which has a List of Notes.
 * e.g.: "C 4 E 4 C 4 E 4 G 8 G 8 REP 6;1 C/1 4 B 4 A 4 G 4 F 8 A 8 G 4 F 4 E 4 D 4 C 8 C 8"
 * Note midi codes: 60 C; 61 C#; 62 D; 63 Eb; 64 E; 65 F; 66 F#; 67 G; 68 Ab; 69 A; 70 A#; 71 B
 */
public class SongTransformer {

    private static final int TIME_BEAT_CONSTANT = 8;
    private static final String RAW_SONG_SEPARATOR = " ";
    private static final String END_SONG = "END_SONG";
    private static final String REPEAT = "REP";
    private static final String REPEAT_SEPARATOR = ";";
    private static final String PAUSE = "R";

    private final NoteTransformer noteTransformer;

    private SongTransformer() {
        noteTransformer = NoteTransformer.createNoteTransformer();
    }

    public static SongTransformer createSongTransformer() {
        return new SongTransformer();
    }

    public void fillSongDataFromRawData(final List<Note> songData, final String rawSong) {
        boolean finished = false;
        List<String> songElements = Arrays.asList(rawSong.split(RAW_SONG_SEPARATOR));
        Iterator<String> iterator = songElements.iterator();
        while (iterator.hasNext() && !finished) {
            String nextNote = iterator.next();
            if (END_SONG.equals(nextNote)) {
                finished = true;
                addEndToSong(songData);
            } else if (REPEAT.equals(nextNote)) {
                String[] repeatArguments = iterator.next().split(REPEAT_SEPARATOR);
                repeat(songData, Integer.parseInt(repeatArguments[0]), Integer.parseInt(repeatArguments[1]));
            } else {
                addNoteToSong(songData, nextNote, Integer.parseInt(iterator.next()));
            }
        }
    }

    private void addEndToSong(final List<Note> songData) {
        songData.add(Note.createNote(null, null, END_SONG));
    }

    private void addNoteToSong(final List<Note> songData, final String noteElement, final Integer tempo) {
        Note note;
        if (PAUSE.equals(noteElement)) {
            note = Note.createNote(null, tempo, PAUSE);
        } else {
            note = noteTransformer.transformToNote(noteElement, tempo);
        }
        songData.add(note);
    }

    private void repeat(final List<Note> songData, final int repeatArgument, final int multiplication) {
        List<Note> repeatedPart = songData.subList(songData.size() - repeatArgument, repeatArgument);
        for (int i = 0; i < multiplication; i++) {
            songData.addAll(repeatedPart);
        }
    }

    public void updateSongData(final List<Note> newSongData, final float tempo, final int noteModifierFactor) {
        newSongData.forEach(note -> replaceNote(newSongData, note, tempo, noteModifierFactor));
    }

    private void replaceNote(List<Note> newSongData, Note note, float tempo, int noteModifierFactor) {
        newSongData.remove(note);
        newSongData.add(transformNote(note, tempo, noteModifierFactor));
    }

    private Note transformNote(Note note, float tempo, int noteModifierFactor) {
        int newBeat = (int) (note.getBeat() * tempo / TIME_BEAT_CONSTANT);
        Integer newNoteValue = note.getNoteValue() != null ? note.getNoteValue() + noteModifierFactor : null;
        return Note.createNote(newNoteValue, newBeat, noteTransformer.getModifiedNoteCharacter(newNoteValue));
    }

    public float getCorrectedTempo(final Note original, final Note modified, final float newTempo) {
        float originalTempo = (float) modified.getBeat() / (float) original.getBeat();
        return newTempo / originalTempo;
    }

    public int getCorrectedNoteModifierFactor(final Note originalFirstNote, final Note modifiedFirstNote,
        final int noteModifierFactor) {
        return modifiedFirstNote.getNoteValue() - originalFirstNote.getNoteValue() + noteModifierFactor;
    }

}
