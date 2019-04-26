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

public class StopCommandTest {

    private static final long SONG_ID = 1;
    private static final String SONG_TITLE = "title";
    private static final String SPACE = " ";
    private static final String STOP = "stop";
    private static final String END_MESSAGE = "FIN";
    private List<String> clientInput;
    private CommandFactory commandFactory;
    private ConcurrentMap<Long, Song> playList;
    private Song song;
    private StopCommand underTest;

    @BeforeMethod
    public void setUp() {
        clientInput = new ArrayList<>();

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
    public void testExecuteWhenPlayListContainsSong() {
        // GIVEN
        clientInput.add(STOP + SPACE + SONG_ID);
        playList.put(song.getId(), song);
        underTest = (StopCommand) commandFactory.createCommand(clientInput, null, playList);
        // WHEN
        Result result = underTest.execute();
        // THEN
        Assert.assertEquals(result.getSong(), song);
        Assert.assertEquals(result.getMessage(), END_MESSAGE);
        Assert.assertEquals(playList.size(), 0);
    }

    @Test
    public void testExecuteWhenPlayListDoesNotContainSong() {
        // GIVEN
        clientInput.add(STOP + SPACE + SONG_ID);
        underTest = (StopCommand) commandFactory.createCommand(clientInput, null, playList);
        // WHEN
        Result result = underTest.execute();
        // THEN
        Assert.assertNull(result.getSong());
        Assert.assertNull(result.getMessage());
    }

}
