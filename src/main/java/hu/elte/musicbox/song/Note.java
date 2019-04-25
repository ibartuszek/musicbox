package hu.elte.musicbox.song;

import java.util.Objects;

public class Note {
    private final Integer noteValue;
    private final Integer beat;
    private final String note;

    private Note(final Integer noteValue, final Integer beat, final String note) {
        this.noteValue = noteValue;
        this.beat = beat;
        this.note = note;
    }

    public static Note createNote(final Integer noteValue, final Integer beat, final String note) {
        return new Note(noteValue, beat, note);
    }

    public Integer getNoteValue() {
        return noteValue;
    }

    public Integer getBeat() {
        return beat;
    }

    public String getNote() {
        return note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Note note1 = (Note) o;
        return Objects.equals(noteValue, note1.noteValue) &&
            Objects.equals(beat, note1.beat) &&
            Objects.equals(note, note1.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noteValue, beat, note);
    }

    @Override
    public String toString() {
        return "Note{" +
            "noteValue=" + noteValue +
            ", beat=" + beat +
            ", note='" + note + '\'' +
            '}';
    }
}
