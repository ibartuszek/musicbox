package hu.elte.musicbox.command;

import java.util.List;

public class CommandFactory {

    public boolean isReady(final List<String> clientInput) {
        boolean result = false;
        final String firstWord = clientInput.get(0).split(" ")[0];
        if (checkInputContainsCommand(firstWord, CommandType.ADD) ||
            checkInputContainsCommand(firstWord, CommandType.ADDLYRICS)) {
            result = clientInput.size() == 2;
        } else if (checkInputContainsCommand(firstWord, CommandType.PLAY) ||
            checkInputContainsCommand(firstWord, CommandType.CHANGE) ||
            checkInputContainsCommand(firstWord, CommandType.STOP)) {
            result = true;
        }
        return result;
    }

    private boolean checkInputContainsCommand(final String firstWord, final CommandType commandType) {
        return firstWord.contains(commandType.getCommandName()) &&
            firstWord.length() == commandType.getCommandName().length();
    }

    public Command createCommand(final List<String> clientInput) {
        final String firstLine = clientInput.get(0);
        final String[] commandArguments = firstLine.split(" ");
        final CommandType commandType = CommandType.getTypeByName(commandArguments[0]);
        Command command = null;

        if (commandType.equals(CommandType.ADD)) {
            command = new AddSongCommand(commandArguments[1], clientInput.get(1));
        } else if (commandType.equals(CommandType.ADDLYRICS)) {
            // TODO
            System.out.println(CommandType.ADDLYRICS);
        } else if (commandType.equals(CommandType.PLAY)) {
            // TODO
            System.out.println(CommandType.PLAY);
        } else if (commandType.equals(CommandType.CHANGE)) {
            // TODO
            System.out.println(CommandType.CHANGE);
        } else if (commandType.equals(CommandType.STOP)) {
            // TODO
            System.out.println(CommandType.STOP);
        }

        return command;
    }
}
