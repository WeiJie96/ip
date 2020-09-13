package duke;

/**
 * Class representing generic tasks.
 */
class Task {

    /** Variable to store task description. */
    protected String description;
    /** Variable to store if the task is completed. */
    protected boolean isDone;

    /**
     * Constructs tasks.
     *
     * @param description Task description.
     */
    Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Constructs tasks.
     *
     * @param description Task description.
     * @param isDone Describes if task is completed.
     */
    Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    /**
     * Gets status icon, depending on whether task is done.
     *
     * @return String representing status icon.
     */
    String getStatusIcon() {
        return (isDone ? "\u2713" : "\u2718"); //return tick or X symbols
    }

    /**
     * Gets task description.
     *
     * @return Task description.
     */
    String getDescription() {
        return this.description;
    }

    /**
     * Gets task done status.
     *
     * @param isDone Task done status.
     */
    void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * Returns string format for file storage.
     *
     * @return String description.
     */
    String toStringForStorage() {
        int intDone = isDone ? 1 : 0;
        return intDone + " | " + this.getDescription();
    }

    @Override
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + this.getDescription();
    }

}
