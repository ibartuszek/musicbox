package hu.elte.musicbox.command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.musicbox.song.Note;
import hu.elte.musicbox.song.Song;
import hu.elte.musicbox.song.SongTransformer;

public class ChangeCommandTest {

    public static final int NOTE_VALUE = 60;
    public static final int BEAT = 4;
    public static final String NOTE = "C";
    public static final String MODIFIED_NOTE = "G#/1";
    private static final long SONG_ID = 1L;
    private static final String SPACE = " ";
    private static final String CHANGE = "change";
    private static final String SONG_TITLE = "title";
    private static final int TEMPO = 100;
    private static final int NOTE_MODIFIER = 20;
    private List<String> clientInput;
    private CommandFactory commandFactory;
    private ConcurrentMap<String, Song> songStore;
    private ConcurrentMap<Long, Song> playList;
    private Note firstNote;
    private Song song;
    private Song transformattedSong;

    private ChangeCommand underTest;

    @BeforeMethod
    public void setUp() {
        AtomicLong songIdCounter = new AtomicLong();
        SongTransformer songTransformer = SongTransformer.createSongTransformer();
        commandFactory = CommandFactory.createCommandFactory(songTransformer, null, songIdCounter);

        clientInput = new ArrayList<>();

        songStore = new ConcurrentHashMap<>();
        playList = new ConcurrentHashMap<>();

        Note note = Note.createNote(NOTE_VALUE, BEAT, NOTE);

        song = Song.createSong(SONG_ID, SONG_TITLE);
        song.getSongData().add(note);
        song.getSongData().add(Note.createNote(69, 2, "A"));
        song.getSongData().add(Note.createNote(69, 2, "A"));
    }

    @Test
    public void testExecuteShouldReturnEmptyResultWhenSongDoesNotExist() {
        // GIVEN
        clientInput.add(CHANGE + SPACE + SONG_ID + SPACE + TEMPO + SPACE + NOTE_MODIFIER);
        underTest = (ChangeCommand) commandFactory.createCommand(clientInput, songStore, playList);
        // WHEN
        Result result = underTest.execute();
        // THEN
        Assert.assertNull(result.getSong());
        Assert.assertEquals(playList.size(), 0);
    }

    @Test
    public void testExecuteShouldReturnABasicSongWhenSongExists() {
        // GIVEN
        firstNote = Note.createNote(NOTE_VALUE + NOTE_MODIFIER, BEAT * TEMPO / 8, MODIFIED_NOTE);
        songStore.put(song.getTitle(), song);
        playList.put(song.getId(), song);
        transformattedSong = Song.createSong(SONG_ID, SONG_TITLE);
        transformattedSong.getSongData().add(Note.createNote(80, 50, "C"));
        transformattedSong.getSongData().add(Note.createNote(89, 25, "A"));
        transformattedSong.getSongData().add(Note.createNote(89, 25, "A"));
        clientInput.add(CHANGE + SPACE + SONG_ID + SPACE + TEMPO + SPACE + NOTE_MODIFIER);
        underTest = (ChangeCommand) commandFactory.createCommand(clientInput, songStore, playList);
        // WHEN
        Result result = underTest.execute();
        // THEN
        Assert.assertEquals(result.getSong(), transformattedSong);
        Assert.assertEquals(playList.size(), 1);
        Assert.assertEquals(playList.get(SONG_ID).getSongData().get(0), firstNote);
    }

    @Test
    public void testExecuteShouldReturnABasicSongWhenSongExistsAndNoteModifierIsNull() {
        // GIVEN
        firstNote = Note.createNote(NOTE_VALUE, BEAT * TEMPO / 8, NOTE);
        songStore.put(song.getTitle(), song);
        playList.put(song.getId(), song);
        transformattedSong = Song.createSong(SONG_ID, SONG_TITLE);
        transformattedSong.getSongData().add(Note.createNote(60, 50, "C"));
        transformattedSong.getSongData().add(Note.createNote(69, 25, "A"));
        transformattedSong.getSongData().add(Note.createNote(69, 25, "A"));
        clientInput.add(CHANGE + SPACE + SONG_ID + SPACE + TEMPO);
        underTest = (ChangeCommand) commandFactory.createCommand(clientInput, songStore, playList);
        // WHEN
        Result result = underTest.execute();
        // THEN
        Assert.assertEquals(result.getSong(), transformattedSong);
        Assert.assertEquals(playList.size(), 1);
        Assert.assertEquals(playList.get(SONG_ID).getSongData().get(0), firstNote);
    }

}
