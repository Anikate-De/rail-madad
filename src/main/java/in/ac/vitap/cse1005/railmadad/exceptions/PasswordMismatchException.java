package in.ac.vitap.cse1005.railmadad.exceptions;

import lombok.experimental.StandardException;

/**
 * Exception thrown when a password mismatch occurs.
 *
 * <p>This exception is typically used to indicate that the provided password does not match the
 * expected password.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * if (!password.equals(confirmPassword)) {
 *   throw new PasswordMismatchException("Passwords do not match.");
 * }
 * }</pre>
 */
@StandardException
public class PasswordMismatchException extends RuntimeException {}
