package hu.elte.musicbox.song;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SongTransformerTest {

    private final SongTransformer underTest = SongTransformer.createSongTransformer();
    private List<Note> songData;

    @BeforeMethod
    public void setUp() {
        songData = new CopyOnWriteArrayList<>();
    }

    @Test
    public void testFillSongDataFromRawDataWhenSongIsOnePause() {
        // GIVEN
        String rawSong = "R 4";
        // WHEN
        underTest.fillSongDataFromRawData(songData, rawSong);
        // THEN
        Assert.assertEquals(songData.size(), 1);
        Assert.assertEquals(songData.get(0).getNote(), "R");
        Assert.assertEquals(songData.get(0).getNoteValue(), null);
        Assert.assertEquals(songData.get(0).getBeat(), Integer.valueOf(4));
    }

    @Test
    public void testFillSongDataFromRawDataWhenSongIsOneBasicNote() {
        // GIVEN
        String rawSong = "C 4";
        // WHEN
        underTest.fillSongDataFromRawData(songData, rawSong);
        // THEN
        Assert.assertEquals(songData.size(), 1);
        Assert.assertEquals(songData.get(0).getNote(), "C");
        Assert.assertEquals(songData.get(0).getNoteValue(), Integer.valueOf(60));
        Assert.assertEquals(songData.get(0).getBeat(), Integer.valueOf(4));
    }

    @Test
    public void testFillSongDataFromRawDataWhenSongIsOneBasicNoteWithSimpleLowerModifier() {
        // GIVEN
        String rawSong = "Cb 2";
        // WHEN
        underTest.fillSongDataFromRawData(songData, rawSong);
        // THEN
        Assert.assertEquals(songData.size(), 1);
        Assert.assertEquals(songData.get(0).getNote(), "Cb");
        Assert.assertEquals(songData.get(0).getNoteValue(), Integer.valueOf(59));
        Assert.assertEquals(songData.get(0).getBeat(), Integer.valueOf(2));
    }

    @Test
    public void testFillSongDataFromRawDataWhenSongIsOneBasicNoteWithSimpleUpperModifier() {
        // GIVEN
        String rawSong = "C# 2";
        // WHEN
        underTest.fillSongDataFromRawData(songData, rawSong);
        // THEN
        Assert.assertEquals(songData.size(), 1);
        Assert.assertEquals(songData.get(0).getNote(), "C#");
        Assert.assertEquals(songData.get(0).getNoteValue(), Integer.valueOf(61));
        Assert.assertEquals(songData.get(0).getBeat(), Integer.valueOf(2));
    }

    @Test
    public void testFillSongDataFromRawDataWhenSongIsOneBasicNoteWithOctaveModifier() {
        // GIVEN
        String rawSong = "C/1 1";
        // WHEN
        underTest.fillSongDataFromRawData(songData, rawSong);
        // THEN
        Assert.assertEquals(songData.size(), 1);
        Assert.assertEquals(songData.get(0).getNote(), "C/1");
        Assert.assertEquals(songData.get(0).getNoteValue(), Integer.valueOf(72));
        Assert.assertEquals(songData.get(0).getBeat(), Integer.valueOf(1));
    }

    @Test
    public void testFillSongDataFromRawDataWhenSongIsOneBasicNoteWithOctaveAndSimpleModifier() {
        // GIVEN
        String rawSong = "Cb/-1 1";
        // WHEN
        underTest.fillSongDataFromRawData(songData, rawSong);
        // THEN
        Assert.assertEquals(songData.size(), 1);
        Assert.assertEquals(songData.get(0).getNote(), "Cb/-1");
        Assert.assertEquals(songData.get(0).getNoteValue(), Integer.valueOf(47));
        Assert.assertEquals(songData.get(0).getBeat(), Integer.valueOf(1));
    }

    @Test
    public void testUpdateSongData() {
        // GIVEN
        songData.add(Note.createNote(60, 4, "C"));
        songData.add(Note.createNote(69, 2, "A"));
        songData.add(Note.createNote(69, 2, "A"));
        List<Note> modifiedSongData = new CopyOnWriteArrayList<>();
        modifiedSongData.add(Note.createNote(80, 50, "G#/1"));
        modifiedSongData.add(Note.createNote(89, 25, "F/2"));
        modifiedSongData.add(Note.createNote(89, 25, "F/2"));
        // WHEN
        underTest.updateSongData(songData, 100, 20);
        // THEN
        Assert.assertEquals(songData.size(), 3);
        Assert.assertEquals(songData.get(0), modifiedSongData.get(0));
        Assert.assertEquals(songData.get(1), modifiedSongData.get(1));
        Assert.assertEquals(songData.get(2), modifiedSongData.get(2));
    }

}
