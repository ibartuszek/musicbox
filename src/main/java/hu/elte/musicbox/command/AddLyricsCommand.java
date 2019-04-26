package hu.elte.musicbox.command;

import java.util.concurrent.ConcurrentMap;

import hu.elte.musicbox.song.LyricsTransformer;
import hu.elte.musicbox.song.Song;

public class AddLyricsCommand implements Command {

    private final CommandType commandType;
    private final String title;
    private final String rawLyrics;
    private final LyricsTransformer lyricsTransformer;
    private final ConcurrentMap<String, Song> songStore;

    private AddLyricsCommand(final String title, final String rawLyrics, final LyricsTransformer lyricsTransformer,
        final ConcurrentMap<String, Song> songStore) {
        this.commandType = CommandType.ADD_LYRICS;
        this.title = title;
        this.rawLyrics = rawLyrics;
        this.lyricsTransformer = lyricsTransformer;
        this.songStore = songStore;
    }

    static AddLyricsCommand createAddLyricsCommand(final String title, final String rawLyrics, final LyricsTransformer lyricsTransformer,
        final ConcurrentMap<String, Song> songStore) {
        return new AddLyricsCommand(title, rawLyrics, lyricsTransformer, songStore);
    }

    @Override
    public Result execute() {
        Song song = null;
        if (songStore.containsKey(title)) {
            song = songStore.get(title);
            song.getLyricsData().clear();
            lyricsTransformer.fillLyricsDataFromRawData(song, rawLyrics);
        }
        return Result.createResult(song, null);
    }
}
