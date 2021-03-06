package hu.elte.musicbox.command;

import java.util.EnumSet;

public enum CommandType {
    ADD("add"),
    ADD_LYRICS("addlyrics"),
    PLAY("play"),
    CHANGE("change"),
    STOP("stop");

    private final String commandName;

    CommandType(final String commandName) {
        this.commandName = commandName;
    }

    public static CommandType getTypeByName(final String commandName) {
        return EnumSet.allOf(CommandType.class)
            .stream()
            .filter(type -> type.commandName.equals(commandName))
            .findFirst()
            .orElse(null);
    }

    public String getCommandName() {
        return this.commandName;
    }
}
