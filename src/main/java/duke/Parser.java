package duke;

/**
 * Class used for parsing input from user command.
 */
class Parser {

    /**
     * Parses input, and calls corresponding functions.
     *
     * @param input String input from user.
     * @return Corresponding Command with respect to input.
     * @throws DukeException  When date is in wrong format, or task lacks description.
     */
    static Command parseTaskCommand(String input) throws DukeException {
        String[] partsOfCommand = splitCommandAndFormatAction(input);
        String action = partsOfCommand[0];

        CommandName commandName = tryEnum(action);
        return handleCommand(commandName, partsOfCommand);
    }

    static boolean isTaskCommand(String input) throws DukeException {
        String[] partsOfCommand = splitCommandAndFormatAction(input);
        String action = partsOfCommand[0];

        CommandName commandName = tryEnum(action);
        switch(commandName) {
        case LIST:
            // Fallthrough
        case DONE:
            // Fallthrough
        case TODO:
            // Fallthrough
        case DEADLINE:
            // Fallthrough
        case EVENT:
            // Fallthrough
        case DELETE:
            // Fallthrough
        case FIND:
            // Fallthrough
        case BYE:
            return true;
        case ADDCLIENT:
            // Fallthrough
        case LISTCLIENT:
            return false;
        default:
            assert false : "There should only be a fixed number of commands, should not reach here";
            return false;
        }
    }

    private static String[] splitCommandAndFormatAction(String input) {
        String[] partsOfCommand = input.split(" ", 2);
        String action = partsOfCommand[0].toUpperCase();

        if (partsOfCommand.length > 1) {
            assert partsOfCommand.length == 2 : "Length of parts of command should be 2";
            String secondPartOfCommand = partsOfCommand[1];
            return new String[]{action, secondPartOfCommand};
        } else {
            return new String[]{action};
        }
    }

    private static Command handleCommand(CommandName commandName, String[] partsOfCommand) throws DukeException {
        int index;
        String secondPartOfCommand;

        switch(commandName) {
        case LIST:
            return new ListCommand();
        case DONE:
            secondPartOfCommand = partsOfCommand[1];
            index = Integer.parseInt(secondPartOfCommand);
            return new DoneCommand(index);
        case TODO:
            // Fallthrough
        case DEADLINE:
            // Fallthrough
        case EVENT:
            return handleTask(partsOfCommand, commandName);
        case DELETE:
            secondPartOfCommand = partsOfCommand[1];
            index = Integer.parseInt(secondPartOfCommand);
            return new DeleteCommand(index);
        case FIND:
            secondPartOfCommand = partsOfCommand[1];
            return new FindCommand(secondPartOfCommand);
        case BYE:
            return new ExitCommand();
        default:
            assert false : "There should only be a fixed number of task commands, should not reach here";
            return new ExitCommand();
        }
    }

    static ClientCommand parseClientCommand(String input) throws DukeException {
        String[] partsOfCommand = splitCommandAndFormatAction(input);
        String action = partsOfCommand[0];

        CommandName commandName = tryEnum(action);
        return handleClientCommand(commandName, partsOfCommand);
    }

    private static ClientCommand handleClientCommand(CommandName commandName,
                                                     String[] partsOfCommand) throws DukeException {
        switch(commandName) {
        case ADDCLIENT:
            return handleAddClient(partsOfCommand);
        case LISTCLIENT:
            return new ListClientCommand();
        default:
            assert false : "There should only be a fixed number of client commands, should not reach here";
            //TODO: Exit Client Command
            return new AddClientCommand("", "", "");
        }
    }

    private static ClientCommand handleAddClient(String[] partsOfCommand) {
        String clientInfo = partsOfCommand[1];

        String[] clientInfoParts = clientInfo.split(" /isMale ", 2);
        String name = clientInfoParts[0];
        String isMaleStringAndBirthdayString = clientInfoParts[1];

        String[] isMaleStringAndBirthdayParts = isMaleStringAndBirthdayString.split(" /bday ", 2);
        String isMaleString = isMaleStringAndBirthdayParts[0];
        String birthdayString = isMaleStringAndBirthdayParts[1];

        return new AddClientCommand(name, isMaleString, birthdayString);
    }

    /**
     * Takes in task info and command and calls respective handler (e.g. handle event).
     *
     * @param inputs Task info, after removing first word from user input.
     * @param commandName CommandName (e.g. Event), first word from user input.
     * @throws DukeException If deadline date not input for deadline, or event date not input for event.
     */
    private static Command handleTask(String[] inputs, CommandName commandName) throws DukeException {
        try {
            String taskInfo = inputs[1];
            String[] taskInfoParts;
            String description;
            String date;
            //TODO: in switch/if-else statement, okay to define early if we use the same variable name?

            switch (commandName) {
            case TODO:
                return new AddCommand(commandName, taskInfo);
            case DEADLINE:
                taskInfoParts = taskInfo.split(" /by ", 2);
                description = taskInfoParts[0];
                date = taskInfoParts[1];
                return new AddCommand(commandName, description, date);
            case EVENT:
                taskInfoParts = taskInfo.split(" /at ", 2);
                description = taskInfoParts[0];
                date = taskInfoParts[1];
                return new AddCommand(commandName, description, date);
            default:
                assert false : "There should only be 3 types of tasks, should not reach here";
                return new AddCommand(commandName, "");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            String emptyDescriptionErrorMessage = "OOPS!!! The description of a " + commandName.toString().toLowerCase()
                + " cannot be empty.";
            throw new DukeException(emptyDescriptionErrorMessage);
        }
    }

    private static CommandName tryEnum(String action) throws DukeException {
        try {
            return CommandName.valueOf(action);
        } catch (IllegalArgumentException e) {
            throw new DukeException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }
}
