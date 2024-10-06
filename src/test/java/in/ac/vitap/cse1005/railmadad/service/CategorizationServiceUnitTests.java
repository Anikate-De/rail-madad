package in.ac.vitap.cse1005.railmadad.service;

import in.ac.vitap.cse1005.railmadad.domain.entity.Category;
import in.ac.vitap.cse1005.railmadad.domain.entity.Complaint;
import in.ac.vitap.cse1005.railmadad.domain.entity.Department;
import in.ac.vitap.cse1005.railmadad.domain.entity.Officer;
import in.ac.vitap.cse1005.railmadad.domain.enums.ComplaintCategory;
import in.ac.vitap.cse1005.railmadad.domain.enums.ComplaintStatus;
import in.ac.vitap.cse1005.railmadad.repository.CategoryRepository;
import in.ac.vitap.cse1005.railmadad.repository.ComplaintRepository;
import in.ac.vitap.cse1005.railmadad.repository.OfficerRepository;
import in.ac.vitap.cse1005.railmadad.utils.ContentCategorizationUtils;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategorizationServiceUnitTests {

  @Mock ComplaintRepository complaintRepository;
  @Mock CategoryRepository categoryRepository;
  @Mock OfficerRepository officerRepository;
  @InjectMocks CategorizationService categorizationService;

  @Test
  public void testCategorizeComplaints_NoPendingComplaints() {
    Mockito.when(complaintRepository.findByStatus(ComplaintStatus.PENDING))
        .thenReturn(Collections.emptyList());

    categorizationService.categorizePendingComplaints();

    Mockito.verify(complaintRepository, Mockito.times(1)).findByStatus(ComplaintStatus.PENDING);
    Mockito.verifyNoMoreInteractions(complaintRepository, categoryRepository, officerRepository);
  }

  @Test
  public void testCategorizeComplaints_WithPendingComplaints() throws Exception {
    Complaint complaint = new Complaint();
    complaint.setTitle("Test Complaint");
    complaint.setSummary("Test Summary");
    complaint.setStatus(ComplaintStatus.PENDING);

    Category category = new Category();
    category.setName("Test Category");
    category.setDepartment(Department.builder().id(1L).name("Test Department").build());

    Officer officer = new Officer();
    officer.setId(1L);

    Mockito.when(complaintRepository.findByStatus(ComplaintStatus.PENDING))
        .thenReturn(List.of(complaint));
    try (MockedStatic<ContentCategorizationUtils> ccUtilsMockedStatic =
        Mockito.mockStatic(ContentCategorizationUtils.class)) {
      ccUtilsMockedStatic
          .when(
              () ->
                  ContentCategorizationUtils.getCategory(
                      Mockito.anyString(), Mockito.any(), Mockito.any()))
          .thenReturn(ComplaintCategory.MISCELLANEOUS);

      Mockito.when(categoryRepository.findByNameIgnoreCase(Mockito.anyString()))
          .thenReturn(Optional.of(category));
      Mockito.when(officerRepository.findByDepartment_Id(Mockito.anyLong()))
          .thenReturn(List.of(officer));

      categorizationService.categorizePendingComplaints();

      Mockito.verify(complaintRepository, Mockito.times(1)).findByStatus(ComplaintStatus.PENDING);
      Mockito.verify(categoryRepository, Mockito.times(1))
          .findByNameIgnoreCase(Mockito.anyString());
      Mockito.verify(officerRepository, Mockito.times(1)).findByDepartment_Id(Mockito.anyLong());
      Mockito.verify(complaintRepository, Mockito.times(1)).save(Mockito.any(Complaint.class));
    }
  }
}
