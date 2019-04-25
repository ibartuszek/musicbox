package hu.elte.musicbox.song;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class LyricsTransformer {

    private static final String LYRICS_SEPARATOR = " ";
    private static final String END_SONG = "END_SONG";
    private static final String REPEAT = "REP";
    private static final String REPEAT_SEPARATOR = ";";
    private static final String PAUSE = "R";

    private LyricsTransformer() {
    }

    public static LyricsTransformer createLyricsTransformer() {
        return new LyricsTransformer();
    }

    public void fillLyricsDataFromRawData(Song song, String rawLyrics) {
        boolean finished = false;
        List<String> lyricsFragments = Arrays.asList(rawLyrics.split(LYRICS_SEPARATOR));
        Iterator<String> lyricsIterator = lyricsFragments.iterator();
        List<String> lyricsData = song.getLyricsData();
        List<Note> songData = song.getSongData();
        int songIndex = 0;
        while (lyricsIterator.hasNext() && songIndex < songData.size() && !finished) {
            Note note = songData.get(songIndex);
            if (note.getNote().equals(END_SONG)) {
                lyricsData.add(END_SONG);
                finished = true;
            } else if (note.getNote().equals(PAUSE)) {
                lyricsData.add(null);
            } else {
                String nextFragment = lyricsIterator.next();
                songIndex = fillSongs(lyricsIterator, lyricsData, songIndex, nextFragment);
            }
            songIndex++;
        }
    }

    private int fillSongs(final Iterator<String> lyricsIterator, final List<String> lyricsData,
        final int songIndex, final String nextFragment) {
        int result = songIndex;
        if (nextFragment.contains(REPEAT)) {
            result += repeat(lyricsData, lyricsIterator.next().split(REPEAT_SEPARATOR));
        } else {
            lyricsData.add(nextFragment);
        }
        return result;
    }

    private int repeat(final List<String> lyricsData, final String[] repeatArguments) {
        int pieces = Integer.parseInt(repeatArguments[0]);
        int multiplication = Integer.parseInt(repeatArguments[1]);
        for (int i = 0; i < multiplication; i++) {
            lyricsData.addAll(getRepeatablePart(lyricsData, pieces));
        }
        return pieces * multiplication;
    }

    private List<String> getRepeatablePart(final List<String> lyricsData, final int repeatArgument) {
        return lyricsData.subList(lyricsData.size() - repeatArgument, repeatArgument);
    }

}
