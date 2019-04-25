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
    public void testTransformToSongWhenSongIsOnePause() {
        //GIVEN
        String rawSong = "R 4";
        // WHEN
        underTest.transformToSong(songData, rawSong);
        // THEN
        Assert.assertEquals(songData.size(), 1);
        Assert.assertEquals(songData.get(0).getNote(), "R");
        Assert.assertEquals(songData.get(0).getNoteValue(), null);
        Assert.assertEquals(songData.get(0).getBeat(), Integer.valueOf(4));
    }

    @Test
    public void testTransformToSongWhenSongIsOneBasicNote() {
        //GIVEN
        String rawSong = "C 4";
        // WHEN
        underTest.transformToSong(songData, rawSong);
        // THEN
        Assert.assertEquals(songData.size(), 1);
        Assert.assertEquals(songData.get(0).getNote(), "C");
        Assert.assertEquals(songData.get(0).getNoteValue(), Integer.valueOf(60));
        Assert.assertEquals(songData.get(0).getBeat(), Integer.valueOf(4));
    }

    @Test
    public void testTransformToSongWhenSongIsOneBasicNoteWithSimpleLowerModifier() {
        //GIVEN
        String rawSong = "Cb 2";
        // WHEN
        underTest.transformToSong(songData, rawSong);
        // THEN
        Assert.assertEquals(songData.size(), 1);
        Assert.assertEquals(songData.get(0).getNote(), "Cb");
        Assert.assertEquals(songData.get(0).getNoteValue(), Integer.valueOf(59));
        Assert.assertEquals(songData.get(0).getBeat(), Integer.valueOf(2));
    }

    @Test
    public void testTransformToSongWhenSongIsOneBasicNoteWithSimpleUpperModifier() {
        //GIVEN
        String rawSong = "C# 2";
        // WHEN
        underTest.transformToSong(songData, rawSong);
        // THEN
        Assert.assertEquals(songData.size(), 1);
        Assert.assertEquals(songData.get(0).getNote(), "C#");
        Assert.assertEquals(songData.get(0).getNoteValue(), Integer.valueOf(61));
        Assert.assertEquals(songData.get(0).getBeat(), Integer.valueOf(2));
    }

    @Test
    public void testTransformToSongWhenSongIsOneBasicNoteWithOctaveModifier() {
        //GIVEN
        String rawSong = "C/1 1";
        // WHEN
        underTest.transformToSong(songData, rawSong);
        // THEN
        Assert.assertEquals(songData.size(), 1);
        Assert.assertEquals(songData.get(0).getNote(), "C/1");
        Assert.assertEquals(songData.get(0).getNoteValue(), Integer.valueOf(72));
        Assert.assertEquals(songData.get(0).getBeat(), Integer.valueOf(1));
    }

    @Test
    public void testTransformToSongWhenSongIsOneBasicNoteWithOctaveAndSimpleModifier() {
        //GIVEN
        String rawSong = "Cb/-1 1";
        // WHEN
        underTest.transformToSong(songData, rawSong);
        // THEN
        Assert.assertEquals(songData.size(), 1);
        Assert.assertEquals(songData.get(0).getNote(), "Cb/-1");
        Assert.assertEquals(songData.get(0).getNoteValue(), Integer.valueOf(47));
        Assert.assertEquals(songData.get(0).getBeat(), Integer.valueOf(1));
    }

}
