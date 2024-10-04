package in.ac.vitap.cse1005.railmadad.exceptions;

import lombok.experimental.StandardException;

/**
 * Exception thrown when required details are incomplete.
 *
 * <p>This exception is typically used to indicate that a user has not provided all the necessary
 * information to perform a certain action.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * if (detailsIncomplete) {
 *   throw new IncompleteDetailsException("Required details are missing.");
 * }
 * }</pre>
 */
@StandardException
public class IncompleteDetailsException extends RuntimeException {}
