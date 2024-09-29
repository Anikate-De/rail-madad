package in.ac.vitap.cse1005.railmadad.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OfficerControllerIntegrationTests {
  @Autowired private MockMvc mockMvc;

  @Test
  public void testSignUp_WithIncompleteDetails() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/officers/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/officers/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"password\": \"password\"}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void testSignUp_WithNoPassword() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/officers/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"firstName\": \"John\"}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void testSignUp_WithWeakPassword() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/officers/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"firstName\": \"John\", \"password\": \"password\"}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void testSignUp_Successful() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/officers/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"firstName\": \"John\", \"password\": \"Password123!\"}"))
        .andExpectAll(
            MockMvcResultMatchers.status().isCreated(),
            MockMvcResultMatchers.jsonPath("officer.id").exists(),
            MockMvcResultMatchers.jsonPath("officer.id").isNumber(),
            MockMvcResultMatchers.jsonPath("officer.id").value(1));
  }

  @Test
  public void testLogin_WithIncompleteDetails() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/officers/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/officers/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"id\": 1}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/officers/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"password\": \"password\"}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void testLogin_WithMissingID() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/officers/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"id\":1, \"password\": \"Password123!\"}"))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void testLogin_WithMismatchedPassword() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/officers/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"firstName\": \"John\", \"password\": \"Password123!\"}"))
        .andExpect(MockMvcResultMatchers.status().isCreated());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/officers/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"id\": 1, \"password\": \"Password123@\"}"))
        .andExpectAll(
            MockMvcResultMatchers.status().isUnauthorized(),
            MockMvcResultMatchers.jsonPath("token").doesNotExist());
  }

  @Test
  public void testLogin_Successful() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/officers/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"firstName\": \"John\", \"password\": \"Password123!\"}"))
        .andExpect(MockMvcResultMatchers.status().isCreated());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/officers/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"id\": 1, \"password\": \"Password123!\"}"))
        .andExpectAll(
            MockMvcResultMatchers.status().isAccepted(),
            MockMvcResultMatchers.jsonPath("token").exists());
  }
}
