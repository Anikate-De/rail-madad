package in.ac.vitap.cse1005.railmadad.domain.enums;

/**
 * Represents the status of a complaint.
 *
 * <p>This enum is used to track the current status of a complaint in the system.
 */
public enum ComplaintStatus {
  /** The complaint has been received but not yet addressed. */
  PENDING,

  /** The complaint is currently being addressed. */
  IN_PROGRESS,

  /** The complaint has been resolved and closed. */
  CLOSED
}
