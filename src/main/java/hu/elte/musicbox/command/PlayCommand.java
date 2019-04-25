package hu.elte.musicbox.command;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentMap;

import hu.elte.musicbox.song.Song;
import hu.elte.musicbox.song.SongTransformer;

public class PlayCommand implements Command {

    private static final String RESULT_MESSAGE = "playing {0}";
    private static final String END_SONG = "FIN";
    private final CommandType commandType;
    private final int tempo;
    private final int noteModifierFactor;
    private final String title;
    private final Long songId;
    private final ConcurrentMap<String, Song> songStore;
    private final ConcurrentMap<String, Song> playList;
    private final SongTransformer songTransformer;

    PlayCommand(final String[] arguments, final ConcurrentMap<String, Song> songStore,
        ConcurrentMap<String, Song> playList, final Long songId,
        final SongTransformer songTransformer) {
        this.commandType = CommandType.PLAY;
        this.tempo = Integer.parseInt(arguments[1]);
        this.noteModifierFactor = Integer.parseInt(arguments[2]);
        this.title = arguments[3];
        this.songId = songId;
        this.songStore = songStore;
        this.playList = playList;
        this.songTransformer = songTransformer;
    }

    @Override
    public Result execute() {
        Song song;
        String message;
        if (!songStore.containsKey(title)) {
            song = null;
            message = END_SONG;
        } else {
            Song rawSong = songStore.get(title);
            song = Song.createSong(songId, title);
            songTransformer.modifySong(rawSong.getSongData(), song.getSongData(), tempo, noteModifierFactor);
            playList.put(song.getTitle(), song);
            message = MessageFormat.format(RESULT_MESSAGE, songId);
        }
        return Result.createResult(song, message);
    }
}
