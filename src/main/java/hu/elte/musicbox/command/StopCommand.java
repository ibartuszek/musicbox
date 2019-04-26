package hu.elte.musicbox.command;

import java.util.concurrent.ConcurrentMap;

import hu.elte.musicbox.song.Song;

public class StopCommand implements Command {

    private static final String END_MESSAGE = "FIN";
    private final CommandType commandType;
    private final Long songId;
    private final ConcurrentMap<Long, Song> playList;

    private StopCommand(final Long songId, final ConcurrentMap<Long, Song> playList) {
        this.commandType = CommandType.STOP;
        this.songId = songId;
        this.playList = playList;
    }

    static StopCommand createStopCommand(final Long songId, final ConcurrentMap<Long, Song> playList) {
        return new StopCommand(songId, playList);
    }

    @Override
    public Result execute() {
        Song song = null;
        String message = null;
        if (playList.containsKey(songId)) {
            song = playList.remove(songId);
            message = END_MESSAGE;
        }
        return Result.createResult(song, message, commandType);
    }
}
