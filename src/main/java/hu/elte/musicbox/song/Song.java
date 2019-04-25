package hu.elte.musicbox.song;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class Song {

    private final Long id;
    private final String title;
    private final List<Note> songData;
    private final List<String> lyricsData;

    private Song(final Long id, final String title) {
        this.id = id;
        this.title = title;
        songData = new CopyOnWriteArrayList<>();
        lyricsData = new CopyOnWriteArrayList<>();
    }

    public static Song createSong(final Long id, final String title) {
        return new Song(id, title);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Note> getSongData() {
        return songData;
    }

    public List<String> getLyricsData() {
        return lyricsData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Song song = (Song) o;
        return Objects.equals(title, song.title) &&
            Objects.equals(id, song.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, id);
    }

    @Override
    public String toString() {
        return "Song{" +
            "title='" + title + '\'' +
            ", songData=" + songData +
            ", id=" + id +
            '}';
    }
}
