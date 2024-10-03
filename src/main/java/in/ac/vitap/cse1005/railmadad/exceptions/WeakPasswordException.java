package in.ac.vitap.cse1005.railmadad.exceptions;

import lombok.experimental.StandardException;

/**
 * Exception thrown when a password is considered weak.
 *
 * <p>This exception is typically used to indicate that the provided password does not meet the
 * required strength criteria.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * if (isWeakPassword(password)) {
 *   throw new WeakPasswordException("Password is too weak.");
 * }
 * }</pre>
 */
@StandardException
public class WeakPasswordException extends RuntimeException {}
