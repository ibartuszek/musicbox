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

public class PlayCommandTest {

    private static final String END_SONG = "FIN";
    private static final long SONG_ID = 1L;
    private static final String SPACE = " ";
    private static final String PLAY = "play";
    private static final String SONG_TITLE = "title";
    private static final String TEMPO = "100";
    private static final String NOTE_MODIFIER = "20";
    private static final String RESULT_MESSAGE = "playing " + SONG_ID;
    private final List<String> clientInput = new ArrayList<>();
    private CommandFactory commandFactory;
    private ConcurrentMap<String, Song> songStore;
    private ConcurrentMap<Long, Song> playList;
    private Song song;
    private Song transformattedSong;

    private PlayCommand underTest;

    @BeforeMethod
    public void setUp() {
        AtomicLong songIdCounter = new AtomicLong();
        SongTransformer songTransformer = SongTransformer.createSongTransformer();
        commandFactory = CommandFactory.createCommandFactory(songTransformer, null, songIdCounter);

        clientInput.add(PLAY + SPACE + TEMPO + SPACE + NOTE_MODIFIER + SPACE + SONG_TITLE);

        songStore = new ConcurrentHashMap<>();
        playList = new ConcurrentHashMap<>();
        song = Song.createSong(SONG_ID, SONG_TITLE);
        song.getSongData().add(Note.createNote(60, 4, "C"));
        song.getSongData().add(Note.createNote(69, 2, "A"));
        song.getSongData().add(Note.createNote(69, 2, "A"));
        transformattedSong = Song.createSong(SONG_ID, SONG_TITLE);
        transformattedSong.getSongData().add(Note.createNote(80, 50, "C"));
        transformattedSong.getSongData().add(Note.createNote(89, 25, "A"));
        transformattedSong.getSongData().add(Note.createNote(89, 25, "A"));
    }

    @Test
    public void testExecuteShouldReturnABasicSongWhenSongExists() {
        // GIVEN
        songStore.put(song.getTitle(), song);
        underTest = (PlayCommand) commandFactory.createCommand(clientInput, songStore, playList);
        // WHEN
        Result result = underTest.execute();
        // THEN
        Assert.assertEquals(result.getSong(), transformattedSong);
        Assert.assertEquals(result.getMessage(), RESULT_MESSAGE);
        Assert.assertEquals(songStore.size(), 1);
    }

    @Test
    public void testExecuteShouldReturnEmptyResultWhenSongDoesNotExist() {
        // GIVEN
        underTest = (PlayCommand) commandFactory.createCommand(clientInput, songStore, null);
        // WHEN
        Result result = underTest.execute();
        // THEN
        Assert.assertNull(result.getSong());
        Assert.assertEquals(result.getMessage(), END_SONG);
        Assert.assertEquals(songStore.size(), 0);
    }

}


