package controller;

/**
 * Represents a command that can be executed in the calendar application.
 */
public interface Command {
  /**
   * Executes a command and returns a result message.
   *
   * @param command the command to be executed
   * @return a string response to the command
   * @throws IllegalArgumentException if the command is null or empty
   */
  String executeCommand(String command) throws IllegalArgumentException;
}