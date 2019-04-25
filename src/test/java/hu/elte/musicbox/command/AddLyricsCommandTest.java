package hu.elte.musicbox.command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.musicbox.song.LyricsTransformer;
import hu.elte.musicbox.song.Note;
import hu.elte.musicbox.song.Song;

public class AddLyricsCommandTest {

    private static final long SONG_ID = 1L;
    private static final String SPACE = " ";
    private static final String ADD_LYRICS = "addlyrics";
    private static final String SONG_TITLE = "title";
    private final LyricsTransformer lyricsTransformer = LyricsTransformer.createLyricsTransformer();
    private final CommandFactory commandFactory = CommandFactory.createCommandFactory(null, lyricsTransformer, null);
    private ConcurrentMap<String, Song> songStore;
    private List<String> clientInput;
    private Song song;
    private AddLyricsCommand underTest;

    @BeforeMethod
    public void setUp() {
        clientInput = new ArrayList<>();
        clientInput.add(ADD_LYRICS + SPACE + SONG_TITLE);
        songStore = new ConcurrentHashMap<>();
        song = Song.createSong(SONG_ID, SONG_TITLE);
    }

    @Test
    public void testExecuteShouldReturnABasicSongWhenSongExists() {
        // GIVEN
        song.getSongData().add(Note.createNote(60, 4, "C"));
        song.getSongData().add(Note.createNote(69, 4, "A"));
        songStore.put(song.getTitle(), song);
        clientInput.add("bo ci");
        underTest = (AddLyricsCommand) commandFactory.createCommand(clientInput, songStore, null);
        // WHEN
        Result result = underTest.execute();
        // THEN
        Assert.assertEquals(result.getSong().getLyricsData().size(), 2);
        Assert.assertEquals(result.getSong().getLyricsData().get(0), "bo");
        Assert.assertEquals(result.getSong().getLyricsData().get(1), "ci");
        Assert.assertNull(result.getMessage());
    }

    @Test
    public void testExecuteShouldReturnEmptyResultWhenSongDoesNotExist() {
        // GIVEN
        clientInput.add("bo ci");
        underTest = (AddLyricsCommand) commandFactory.createCommand(clientInput, songStore, null);
        // WHEN
        Result result = underTest.execute();
        // THEN
        Assert.assertNull(result.getMessage());
        Assert.assertNull(result.getSong());
    }

}
