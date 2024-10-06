package in.ac.vitap.cse1005.railmadad.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import in.ac.vitap.cse1005.railmadad.domain.entity.Officer;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OfficerRepositoryIntegrationTests {
  private final OfficerRepository underTest;

  @Autowired
  public OfficerRepositoryIntegrationTests(OfficerRepository underTest) {
    this.underTest = underTest;
  }

  @Test
  public void testSave() {
    Officer officer = Officer.builder().firstName("John").build();

    underTest.save(officer);

    List<Officer> officers = (List<Officer>) underTest.findAll();
    Officer received = officers.getFirst();

    assertThat(officers).hasSize(1);
    AssertionsForClassTypes.assertThat(received.getFirstName()).isEqualTo(officer.getFirstName());
    AssertionsForClassTypes.assertThat(received.getId()).isNotNull();
    AssertionsForClassTypes.assertThat(received.getLastLogin()).isNull();
    AssertionsForClassTypes.assertThat(received.getDateRegistered())
        .isBetween(Instant.now().minusSeconds(1), Instant.now());
  }

  @Test
  public void testUpdateLastLoginById() {
    Officer officer = Officer.builder().firstName("John").build();

    Officer savedCustomer = underTest.save(officer);
    officer.setId(savedCustomer.getId());

    Instant lastLogin = Instant.now();
    underTest.updateLastLoginById(lastLogin, officer.getId());

    Optional<Officer> received = underTest.findById(officer.getId());

    AssertionsForClassTypes.assertThat(received).isPresent();
    AssertionsForClassTypes.assertThat(received.get().getLastLogin())
        .isBetween(lastLogin.minusMillis(100), lastLogin.plusMillis(100));
  }

  @Test
  public void testFindByDepartmentId() {
    Officer officer1 = Officer.builder().firstName("John").build();
    Officer officer2 = Officer.builder().firstName("Jane").build();

    underTest.save(officer1);
    underTest.save(officer2);

    List<Officer> officers = underTest.findByDepartment_Id(1L);

    assertThat(officers).isEmpty();
  }
}
