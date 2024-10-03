package in.ac.vitap.cse1005.railmadad.exceptions;

import lombok.experimental.StandardException;

/**
 * Exception thrown when access is denied to a resource or operation.
 *
 * <p>This exception is typically used to indicate that a user does not have the necessary
 * permissions to perform a certain action.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * if (!user.hasPermission("ADMIN")) {
 *   throw new AccessDeniedException("User does not have admin rights.");
 * }
 * }</pre>
 */
@StandardException
public class AccessDeniedException extends RuntimeException {}
