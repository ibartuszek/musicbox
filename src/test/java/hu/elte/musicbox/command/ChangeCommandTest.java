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

    private static final long SONG_ID = 1L;
    private static final String SPACE = " ";
    private static final String CHANGE = "change";
    private static final String SONG_TITLE = "title";
    private static final String TEMPO = "100";
    private static final String NOTE_MODIFIER = "20";
    private final List<String> clientInput;
    private CommandFactory commandFactory;
    private ConcurrentMap<Long, Song> playList;
    private Song song;
    private Song transformattedSong;
    private ChangeCommand underTest;

    public ChangeCommandTest() {
        clientInput = new ArrayList<>();
    }

    @BeforeMethod
    public void setUp() {
        AtomicLong songIdCounter = new AtomicLong();
        SongTransformer songTransformer = SongTransformer.createSongTransformer();
        commandFactory = CommandFactory.createCommandFactory(songTransformer, null, songIdCounter);

        playList = new ConcurrentHashMap<>();
        song = Song.createSong(SONG_ID, SONG_TITLE);
        song.getSongData().add(Note.createNote(60, 4, "C"));
        song.getSongData().add(Note.createNote(69, 2, "A"));
        song.getSongData().add(Note.createNote(69, 2, "A"));
    }

    @Test
    public void testExecuteShouldReturnEmptyResultWhenSongDoesNotExist() {
        // GIVEN
        clientInput.add(CHANGE + SPACE + SONG_ID + SPACE + TEMPO + SPACE + NOTE_MODIFIER);
        underTest = (ChangeCommand) commandFactory.createCommand(clientInput, null, playList);
        // WHEN
        Result result = underTest.execute();
        // THEN
        Assert.assertNull(result.getSong());
        Assert.assertEquals(playList.size(), 0);
    }

    @Test
    public void testExecuteShouldReturnABasicSongWhenSongExists() {
        // GIVEN
        playList.put(song.getId(), song);
        transformattedSong = Song.createSong(SONG_ID, SONG_TITLE);
        transformattedSong.getSongData().add(Note.createNote(80, 50, "C"));
        transformattedSong.getSongData().add(Note.createNote(89, 25, "A"));
        transformattedSong.getSongData().add(Note.createNote(89, 25, "A"));
        clientInput.add(CHANGE + SPACE + SONG_ID + SPACE + TEMPO + SPACE + NOTE_MODIFIER);
        underTest = (ChangeCommand) commandFactory.createCommand(clientInput, null, playList);
        // WHEN
        Result result = underTest.execute();
        // THEN
        Assert.assertEquals(result.getSong(), transformattedSong);
        Assert.assertEquals(playList.size(), 1);
    }

    @Test
    public void testExecuteShouldReturnABasicSongWhenSongExistsAndNoteModifierIsNull() {
        // GIVEN
        playList.put(song.getId(), song);
        transformattedSong = Song.createSong(SONG_ID, SONG_TITLE);
        transformattedSong.getSongData().add(Note.createNote(60, 50, "C"));
        transformattedSong.getSongData().add(Note.createNote(69, 25, "A"));
        transformattedSong.getSongData().add(Note.createNote(69, 25, "A"));
        clientInput.add(CHANGE + SPACE + SONG_ID + SPACE + TEMPO);
        underTest = (ChangeCommand) commandFactory.createCommand(clientInput, null, playList);
        // WHEN
        Result result = underTest.execute();
        // THEN
        Assert.assertEquals(result.getSong(), transformattedSong);
        Assert.assertEquals(playList.size(), 1);
    }

}
