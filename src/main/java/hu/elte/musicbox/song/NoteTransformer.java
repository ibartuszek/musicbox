package hu.elte.musicbox.song;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NoteTransformer {

    private static final String EMPTY_STRING = "";
    private static final Character NOTE_MODIFIER_TO_UPPER = '#';
    private static final Character NOTE_MODIFIER_TO_LOWER = 'b';
    private static final String OCTAVE_SEPARATOR = "/";
    private static final int OCTAVE_SHIFT = 12;

    private final Map<Character, Integer> noteCodeTable;

    private NoteTransformer() {
        noteCodeTable = initNoteCodeTable();
    }

    public static NoteTransformer createNoteTransformer() {
        return new NoteTransformer();
    }

    private Map<Character, Integer> initNoteCodeTable() {
        final Map<Character, Integer> noteCodeTable = new HashMap<>();
        noteCodeTable.put('C', 60);
        noteCodeTable.put('D', 62);
        noteCodeTable.put('E', 64);
        noteCodeTable.put('F', 65);
        noteCodeTable.put('G', 67);
        noteCodeTable.put('A', 69);
        noteCodeTable.put('B', 71);
        return noteCodeTable;
    }

    Note transformToNote(final String noteElement, final Integer tempo) {
        return Note.createNote(calculateNoteValue(noteElement), tempo, noteElement);
    }

    public Integer calculateNoteValue(final String noteElement) {
        String[] elements = noteElement.split(OCTAVE_SEPARATOR);
        Character aimNote = elements[0].charAt(0);
        int targetValue = noteCodeTable.get(aimNote);
        int modifier = createNoteModifiers(elements);
        return targetValue + modifier;
    }

    private int createNoteModifiers(final String[] elements) {
        int noteModifier = 0;
        Character modifier = elements[0].length() > 1 ? elements[0].charAt(1) : null;
        if (NOTE_MODIFIER_TO_UPPER.equals(modifier)) {
            noteModifier += 1;
        } else if (NOTE_MODIFIER_TO_LOWER.equals(modifier)) {
            noteModifier -= 1;
        }
        int octaveModifier = elements.length > 1 ? Integer.parseInt(elements[1]) : 0;
        return noteModifier + octaveModifier * OCTAVE_SHIFT;
    }

    String getModifiedNoteCharacter(final Integer newNoteValue) {
        int value = newNoteValue;

        int octaveModifier = calculateOctaveModifier(newNoteValue);
        value -= octaveModifier * OCTAVE_SHIFT;

        int noteBasicModifier = calculateNoteBasicModifier(value);
        value -= noteBasicModifier;

        return getNoteWithoutModifier(value) + getNoteModifiers(noteBasicModifier, octaveModifier);
    }

    private Character getNoteWithoutModifier(final int value) {
        Optional<Character> optionalValue = noteCodeTable.entrySet().stream()
            .filter(e -> value == e.getValue())
            .map(Map.Entry::getKey)
            .findFirst();
        return optionalValue.orElse(null);
    }

    private int calculateOctaveModifier(final int newNoteValue) {
        int octaveModifier = 0;
        int value = newNoteValue;
        while (value < 60 || value > 71) {
            if (newNoteValue < 60) {
                value += OCTAVE_SHIFT;
                octaveModifier--;
            } else {
                value -= OCTAVE_SHIFT;
                octaveModifier++;
            }
        }
        return octaveModifier;
    }

    private int calculateNoteBasicModifier(final int newNoteValue) {
        return noteCodeTable.containsValue(newNoteValue) ? 0 : 1;
    }

    private String getNoteModifiers(final int noteBasicModifier, final int octaveModifier) {
        return (noteBasicModifier != 0 ? NOTE_MODIFIER_TO_UPPER : EMPTY_STRING)
            + (octaveModifier != 0 ? OCTAVE_SEPARATOR + octaveModifier : EMPTY_STRING);
    }

}
