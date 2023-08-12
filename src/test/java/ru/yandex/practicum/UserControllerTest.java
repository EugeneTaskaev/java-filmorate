package ru.yandex.practicum;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.controller.UserController;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private UserController userController;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    public void afterEach() {
        userController.clean();
    }

    @Test
    void postValidUserTest() throws Exception {
        User user = new User(null, "e.taskaev@gmail.com", "pkingsbl", "Taskaev", LocalDate.of(1997, 12, 1));
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType("application/json")).andExpect(status().isOk());
    }

    @Test
    void putValidUserTest() throws Exception {
        User user = new User(null, "e.taskaev@gmail.com", "pkingsbl", "Taskaev", LocalDate.of(1997, 12, 1));
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType("application/json")).andExpect(status().isOk());

        User userUpdate = new User(null, "Vova2020@gmail.com", "Vova", "Vovkin", LocalDate.of(1992, 12, 11));
        userUpdate.setId(1);
        body = objectMapper.writeValueAsString(userUpdate);
        this.mockMvc.perform(put("/users").content(body).contentType("application/json")).andExpect(status().isOk());
    }

    @Test
    void getValidUserTest() throws Exception {
        User user = new User(null, "Vova2020@gmail.com", "pkingsbl", "Vovkin", LocalDate.of(1992, 12, 11));
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType("application/json")).andExpect(status().isOk());

        User userSec = new User(null, "lebedev200@gmail.com", "anigilyator2000", "Lebedev", LocalDate.of(2002, 12, 1));
        body = objectMapper.writeValueAsString(userSec);
        this.mockMvc.perform(post("/users").content(body).contentType("application/json")).andExpect(status().isOk());

        this.mockMvc.perform(get("/users")).andExpect(status().isOk()).andExpect(jsonPath("$[0].login").value("pkingsbl")).andExpect(jsonPath("$[1].login").value("anigilyator2000"));
    }

    @Test
    void postUserWithoutLoginTest() throws Exception {
        User user = new User(null, "Vova2020@gmail.com", " ", "pkingsbl", LocalDate.of(1992, 12, 11));
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType("application/json")).andExpect(status().isBadRequest());
    }

    @Test
    void postUserInvalidDateTest() throws Exception {
        User user = new User(null, "Vova20@gmail.comneponyal", "Vova", "pkingsbl", LocalDate.of(2222, 12, 1));
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType("application/json")).andExpect(status().isBadRequest());
    }

    @Test
    void postUserInvalidEmailTest() throws Exception {
        User user = new User(null, "Vova2020", "Vova", "pkingsbl", LocalDate.of(1922, 12, 1));
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType("application/json")).andExpect(status().isBadRequest());
    }

    @Test
    void postValidUserWithoutNameTest() throws Exception {
        User user = new User(null, "e.taskaev@gmail.com", "pkingsbl", "", LocalDate.of(1922, 12, 1));
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType("application/json")).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("pkingsbl"));
    }

}