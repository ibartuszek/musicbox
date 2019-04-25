package hu.elte.musicbox.song;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LyricsTransformerTest {

    public static final long ID = 0L;
    public static final String TITLE = "title";

    private final LyricsTransformer underTest = LyricsTransformer.createLyricsTransformer();
    private Song song;

    @BeforeMethod
    public void setUp() {
        song = Song.createSong(ID, TITLE);
    }

    @Test
    public void testFillLyricsDataFromRawDataWhenInputHasTwoSimpleFragment() {
        // GIVEN
        song.getSongData().add(Note.createNote(60, 4, "C"));
        song.getSongData().add(Note.createNote(69, 4, "A"));
        // WHEN
        underTest.fillLyricsDataFromRawData(song, "bo ci");
        // THEN
        Assert.assertEquals(song.getLyricsData().get(0), "bo");
        Assert.assertEquals(song.getLyricsData().get(1), "ci");
    }

    @Test
    public void testFillLyricsDataFromRawDataWhenInputHasTwoSimpleAndAPauseFragment() {
        // GIVEN
        song.getSongData().add(Note.createNote(60, 4, "C"));
        song.getSongData().add(Note.createNote(null, 4, "R"));
        song.getSongData().add(Note.createNote(69, 4, "A"));
        // WHEN
        underTest.fillLyricsDataFromRawData(song, "bo ci");
        // THEN
        Assert.assertEquals(song.getLyricsData().get(0), "bo");
        Assert.assertEquals(song.getLyricsData().get(1), null);
        Assert.assertEquals(song.getLyricsData().get(2), "ci");
    }

    @Test
    public void testFillLyricsDataFromRawDataWhenInputHasRepeat() {
        // GIVEN
        song.getSongData().add(Note.createNote(60, 4, "C"));
        song.getSongData().add(Note.createNote(60, 4, "C"));
        song.getSongData().add(Note.createNote(69, 4, "A"));
        song.getSongData().add(Note.createNote(60, 4, "C"));
        song.getSongData().add(Note.createNote(60, 4, "C"));
        song.getSongData().add(Note.createNote(69, 4, "A"));
        // WHEN
        underTest.fillLyricsDataFromRawData(song, "bo bo ci REP 3;1");
        // THEN
        Assert.assertEquals(song.getLyricsData().size(), 6);
        Assert.assertEquals(song.getLyricsData().get(4), "bo");
        Assert.assertEquals(song.getLyricsData().get(5), "ci");
    }

}
