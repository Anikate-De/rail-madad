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
public class CustomerControllerIntegrationTests {
  @Autowired private MockMvc mockMvc;

  @Test
  public void testSignUp_WithIncompleteDetails() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/customers/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/customers/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"firstName\": \"John\"}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/customers/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"phoneNumber\": \"1234567890\"}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void testSignUp_WithNoPassword() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/customers/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"firstName\": \"John\", \"phoneNumber\": 1234567890}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void testSignUp_WithWeakPassword() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/customers/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    "{\"firstName\": \"John\", \"phoneNumber\": 1234567890, \"password\":"
                        + " \"password\"}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void testSignUp_WithExistingPhoneNumber() throws Exception {
    mockMvc.perform(
        MockMvcRequestBuilders.post("/customers/signup")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(
                "{\"firstName\": \"John\", \"phoneNumber\": 1234567890, \"password\":"
                    + " \"Password123!\"}"));

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/customers/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    "{\"firstName\": \"Jane\", \"phoneNumber\": 1234567890, \"password\":"
                        + " \"Password123!\"}"))
        .andExpect(MockMvcResultMatchers.status().isConflict());
  }

  @Test
  public void testSignUp_Successful() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/customers/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    "{\"firstName\": \"John\", \"phoneNumber\": 1234567890, \"password\":"
                        + " \"Password123!\"}"))
        .andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  public void testLogin_WithIncompleteDetails() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/customers/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/customers/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"phoneNumber\": 1234567890}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/customers/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"password\": \"password\"}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void testLogin_WithMissingPhoneNumber() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/customers/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"phoneNumber\": 1234567890, \"password\": \"password\"}"))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void testLogin_WithMismatchedPassword() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/customers/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    "{\"firstName\": \"John\", \"phoneNumber\": 1234567890, \"password\":"
                        + " \"Password123!\"}"))
        .andExpect(MockMvcResultMatchers.status().isCreated());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/customers/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"phoneNumber\": 1234567890, \"password\": \"Password123@\"}"))
        .andExpectAll(
            MockMvcResultMatchers.status().isUnauthorized(),
            MockMvcResultMatchers.jsonPath("token").doesNotExist());
  }

  @Test
  public void testLogin_Successful() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/customers/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    "{\"firstName\": \"John\", \"phoneNumber\": 1234567890, \"password\":"
                        + " \"Password123!\"}"))
        .andExpect(MockMvcResultMatchers.status().isCreated());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/customers/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"phoneNumber\": 1234567890, \"password\": \"Password123!\"}"))
        .andExpectAll(
            MockMvcResultMatchers.status().isAccepted(),
            MockMvcResultMatchers.jsonPath("token").exists());
  }
}
