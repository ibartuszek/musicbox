package hu.elte.musicbox.command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.musicbox.song.Note;
import hu.elte.musicbox.song.Song;
import hu.elte.musicbox.song.SongTransformer;

public class AddSongCommandTest {

    public static final String ADD = "add";
    public static final String SPACE = " ";
    public static final String SONG_TITLE = "song";
    public static final Long FIRST_SONG_ID = 0L;
    private final SongTransformer songTransformer = SongTransformer.createSongTransformer();
    private final CommandFactory commandFactory = CommandFactory.createCommandFactory(songTransformer, null);
    private AddSongCommand underTest;
    private List<String> clientInput;
    private ConcurrentMap<String, Song> songStore;

    @BeforeMethod
    public void setUp() {
        clientInput = new ArrayList<>();
        clientInput.add(ADD + SPACE + SONG_TITLE);
        songStore = new ConcurrentHashMap<>();
    }

    @Test
    public void testShouldReturnBasicSongWhenInputIsBasicSongWithRepeat() {
        // GIVEN
        clientInput.add("C 4 E 4 C 4 E 4 G 8 G 8 REP 6;1 C/1 4 B 4 A 4 G 4 F 8 A 8 G 4 F 4 E 4 D 4 C 8 C 8");
        underTest = (AddSongCommand) commandFactory.createCommand(clientInput, songStore);
        // WHEN
        Song result = underTest.execute();
        // THEN
        Assert.assertEquals(result.getTitle(), SONG_TITLE);
        Assert.assertEquals(result.getId(), FIRST_SONG_ID);
        Assert.assertEquals(result.getSongData().size(), 24);
    }

    @Test
    public void testShouldReturnWithNewSongWhenInputIsAnExistingSong() {
        // GIVEN
        songStore.put(SONG_TITLE, createSong());
        clientInput.add("C 4 E 4");
        underTest = (AddSongCommand) commandFactory.createCommand(clientInput, songStore);
        // WHEN
        Song result = underTest.execute();
        // THEN
        Assert.assertEquals(result.getTitle(), SONG_TITLE);
        Assert.assertEquals(result.getId(), FIRST_SONG_ID);
        Assert.assertEquals(result.getSongData().size(), 2);
    }

    private Song createSong() {
        Song song = Song.createSong(FIRST_SONG_ID, SONG_TITLE);
        song.getSongData().add(Note.createNote(60, 4, "C"));
        return song;
    }
}
