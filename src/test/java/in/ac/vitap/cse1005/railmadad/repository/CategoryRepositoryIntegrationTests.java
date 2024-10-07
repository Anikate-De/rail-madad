package in.ac.vitap.cse1005.railmadad.repository;

import static org.assertj.core.api.Assertions.assertThat;

import in.ac.vitap.cse1005.railmadad.domain.entity.Category;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CategoryRepositoryIntegrationTests {
  private final CategoryRepository underTest;

  @Autowired
  public CategoryRepositoryIntegrationTests(CategoryRepository underTest) {
    this.underTest = underTest;
  }

  @Test
  public void testSave() {
    Category category = new Category();
    category.setName("Test Category");

    Category savedCategory = underTest.save(category);

    assertThat(savedCategory).isNotNull();
    assertThat(savedCategory.getId()).isNotNull();
    assertThat(savedCategory.getName()).isEqualTo("Test Category");

    Category foundCategory = underTest.findById(savedCategory.getId()).orElse(null);

    assertThat(foundCategory).isNotNull();
    assertThat(foundCategory.getId()).isEqualTo(savedCategory.getId());
    assertThat(foundCategory.getName()).isEqualTo("Test Category");
  }

  @Test
  public void testFindByNameIgnoreCase() {
    Optional<Category> foundCategory = underTest.findByNameIgnoreCase("test category");

    assertThat(foundCategory).isEmpty();

    Category category = new Category();
    category.setName("Test Category");
    underTest.save(category);

    foundCategory = underTest.findByNameIgnoreCase("test category");

    assertThat(foundCategory).isPresent();
    assertThat(foundCategory.get().getName()).isEqualTo("Test Category");
  }
}
