package model.exceptions;

// Class taken from
// https://github.students.cs.ubc.ca/CPSC210/AlarmSystem
/**
 * Represents the exception that can occur when
 * printing the event log.
 */
public class LogException extends Exception {
    public LogException() {
        super("Error printing log");
    }

    public LogException(String msg) {
        super(msg);
    }
}