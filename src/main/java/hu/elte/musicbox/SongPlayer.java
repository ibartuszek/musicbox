package hu.elte.musicbox;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import javax.sound.midi.MidiChannel;

import hu.elte.musicbox.command.Result;
import hu.elte.musicbox.song.Note;
import hu.elte.musicbox.song.Song;

public class SongPlayer {

    private static final int START_SLEEP = 500;
    private static final String SPACE = " ";
    private static final String UNKNOWN_LYRICS_FRAGMENT = "???";
    private static final String END_SONG = "FIN";
    private static final String START_MESSAGE = "start";

    private final PrintWriter printWriter;
    private final MidiChannel midiChannel;
    private final Long songId;
    private final String songTitle;
    private final ConcurrentMap<String, Song> songStore;
    private final ConcurrentMap<Long, Song> playList;

    private SongPlayer(final SongPlayerBuilder songPlayerBuilder) {
        MusicBoxClient musicBoxClient = songPlayerBuilder.musicBoxClient;
        this.printWriter = musicBoxClient.getPrintWriter();
        this.midiChannel = musicBoxClient.getChannel();
        Result result = songPlayerBuilder.result;
        this.songStore = songPlayerBuilder.songStore;
        this.playList = songPlayerBuilder.playList;
        this.songId = result.getSong().getId();
        this.songTitle = playList.get(songId).getTitle();
    }

    static void sendEndMessage(PrintWriter printWriter) {
        printWriter.print(END_SONG);
        printWriter.flush();
    }

    void playSong() {
        sendStartMessage();
        List<Note> songData = getSongData(songId);
        int noteIndex = 0;
        while (noteIndex < songData.size()) {
            Note note = songData.get(noteIndex);
            String lyricsFragment = getNextLyricsFragment(songTitle, songStore, noteIndex);
            if (note.getNoteValue() != null) {
                playNote(note, lyricsFragment);
            } else {
                pause(note.getBeat());
            }
            songData = getSongData(songId);
            noteIndex++;
        }
        sendEndMessage();
    }

    private String getNextLyricsFragment(final String title, final ConcurrentMap<String, Song> songStore,
        final int index) {
        String result;
        List<String> lyricsData = songStore.get(title).getLyricsData();
        if (lyricsData.size() > index && lyricsData.get(index) != null) {
            result = lyricsData.get(index);
        } else {
            result = UNKNOWN_LYRICS_FRAGMENT;
        }
        return result;
    }

    private List<Note> getSongData(Long songId) {
        return playList.get(songId) != null ? playList.get(songId).getSongData() : Collections.emptyList();
    }

    private void playNote(final Note note, final String lyricsFragment) {
        printWriter.print(createMessage(note, lyricsFragment));
        printWriter.flush();
        midiChannel.noteOn(note.getNoteValue(), note.getBeat());
        pause(note.getBeat());
        midiChannel.noteOff(note.getNoteValue(), note.getBeat());
    }

    private void pause(final Integer beat) {
        try {
            Thread.sleep(beat);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String createMessage(final Note nextNote, final String lyricsFragment) {
        String result;
        if (nextNote.getNoteValue() != null) {
            result = nextNote.getNote() + SPACE + lyricsFragment + SPACE;
        } else {
            result = SPACE;
        }
        return result;
    }

    private void sendStartMessage() {
        printWriter.println(START_MESSAGE + SPACE + songId);
        printWriter.flush();
        pause(START_SLEEP);
    }

    private void sendEndMessage() {
        sendEndMessage(this.printWriter);
    }

    static class SongPlayerBuilder {
        private MusicBoxClient musicBoxClient;
        private Result result;
        private ConcurrentMap<String, Song> songStore;
        private ConcurrentMap<Long, Song> playList;

        public SongPlayer build() {
            return new SongPlayer(this);
        }

        public SongPlayerBuilder withClient(final MusicBoxClient musicBoxClient) {
            this.musicBoxClient = musicBoxClient;
            return this;
        }

        public SongPlayerBuilder withResult(final Result result) {
            this.result = result;
            return this;
        }

        public SongPlayerBuilder withSongStore(final ConcurrentMap<String, Song> songStore) {
            this.songStore = songStore;
            return this;
        }

        public SongPlayerBuilder withPlayList(final ConcurrentMap<Long, Song> playList) {
            this.playList = playList;
            return this;
        }
    }

}
