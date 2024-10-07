package in.ac.vitap.cse1005.railmadad.domain.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing various categories of complaints.
 *
 * <p>Each category has a corresponding database name.
 */
@Getter
@AllArgsConstructor
public enum ComplaintCategory {
  /** Medical assistance related complaints. */
  MEDICAL("Medical Assistance"),

  /** Security related complaints. */
  SECURITY("Security"),

  /** Complaints related to facilities for Divyangjan (differently-abled individuals). */
  DIVYANGJAN("Divyangjan Facilities"),

  /** Complaints related to facilities for women with special needs. */
  SPECIAL_WOMEN("Facilities for Women with Special Needs"),

  /** Complaints related to electrical equipment. */
  ELECTRICAL("Electrical Equipment"),

  /** Complaints related to coach cleanliness. */
  CLEANLINESS("Coach - Cleanliness"),

  /** Complaints related to water availability. */
  WATER("Water Availability"),

  /** Complaints related to train punctuality. */
  PUNCTUALITY("Punctuality"),

  /** Complaints related to coach maintenance. */
  MAINTENANCE("Coach - Maintenance"),

  /** Complaints related to catering and vending services. */
  CATERING("Catering & Vending Services"),

  /** Complaints related to staff behavior. */
  STAFF("Staff Behaviour"),

  /** Complaints related to corruption or bribery. */
  CORRUPTION("Corruption / Bribery"),

  /** Complaints related to bed roll services. */
  BED("Bed Roll"),

  /** Miscellaneous complaints. */
  MISCELLANEOUS("Miscellaneous");

  /** A map of category names to their corresponding enum values. */
  static final Map<String, ComplaintCategory> categoryNames =
      Arrays.stream(ComplaintCategory.values())
          .collect(Collectors.toMap(ComplaintCategory::getCategoryName, Function.identity()));

  /** The database name of the category. */
  private final String categoryName;

  /**
   * Returns the ComplaintCategory corresponding to the given category name.
   *
   * @param categoryName the database name of the category
   * @return the corresponding ComplaintCategory, or null if no match is found
   */
  public static ComplaintCategory fromCategoryName(final String categoryName) {
    return categoryNames.get(categoryName);
  }
}
