package hu.elte.musicbox.command;

import java.util.concurrent.ConcurrentMap;

import hu.elte.musicbox.song.Song;
import hu.elte.musicbox.song.SongTransformer;

public class AddSongCommand implements Command {

    private final CommandType commandType;
    private final String title;
    private final String rawSong;
    private final SongTransformer songTransformer;
    private final ConcurrentMap<String, Song> songStore;

    AddSongCommand(final String title, final String rawSong, final SongTransformer songTransformer,
        final ConcurrentMap<String, Song> songStore) {
        this.commandType = CommandType.ADD;
        this.title = title;
        this.rawSong = rawSong;
        this.songTransformer = songTransformer;
        this.songStore = songStore;
    }

    @Override
    public Song execute() {
        final Song song;

        if (songStore.containsKey(title)) {
            song = songStore.get(title);
            song.getSongData().clear();
        } else {
            synchronized (songStore) {
                song = Song.createSong(title, (long) songStore.size());
                songStore.put(song.getTitle(), song);
            }
        }
        songTransformer.transformToSong(song.getSongData(), rawSong);
        return song;
    }

}
