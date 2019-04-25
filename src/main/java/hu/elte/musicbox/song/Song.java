package hu.elte.musicbox.song;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class Song {

    private final String title;
    private final List<Note> songData;
    private final Long id;

    private Song(final String title, final Long id) {
        this.title = title;
        this.id = id;
        songData = new CopyOnWriteArrayList<>();
    }

    public static Song createSong(final String title, final Long id) {
        return new Song(title, id);
    }

    public String getTitle() {
        return title;
    }

    public List<Note> getSongData() {
        return songData;
    }

    public Long getId() {
        return id;
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
