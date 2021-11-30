package com.application.app;

import com.application.repository.UserRepo;
import com.application.repository.WalletRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RegistrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private WalletRepo walletRepo;

    @ParameterizedTest
    @CsvSource({
            "Bethzarill,4JD9ujpF92",
            "Severnyakoto,67j6Nzc7MT",
            "Uesday,65j8FdfP2K"
    })
    public void createUserTest(String username, String password) throws Exception {
        this.mockMvc.perform(post("/registration")
                        .param("username", username)
                        .param("password", password)
                        .with(csrf()))
                .andDo(print())
                .andExpect(redirectedUrl("/login"));
        Assertions.assertEquals(username, userRepo.findByUsername(username).getUsername());
        Assertions.assertEquals(password, userRepo.findByUsername(username).getPassword());
        Assertions.assertEquals(new BigDecimal("0.00"),
                walletRepo.getById(userRepo.findByUsername(username).getId()).getBalance());
        Assertions.assertEquals(new BigDecimal("0.00"),
                walletRepo.getById(userRepo.findByUsername(username).getId()).getBudget());
    }

    @ParameterizedTest
    @CsvSource({
            "Bethzarill,"
    })
    public void createEmptyPasswordUserTest(String username, String password) throws Exception {
        this.mockMvc.perform(post("/registration")
                        .param("username", username)
                        .param("password", password)
                        .with(csrf()))
                .andExpect(model().attribute("message", "Incorrect username or password"))
                .andExpect(status().isOk());
        Assertions.assertNull(userRepo.findByUsername(username));
    }

    @ParameterizedTest
    @CsvSource({
            ",4JD9ujpF92"
    })
    public void createEmptyUsernameUserTest(String username, String password) throws Exception {
        this.mockMvc.perform(post("/registration")
                        .param("username", username)
                        .param("password", password)
                        .with(csrf()))
                .andExpect(model().attribute("message", "Incorrect username or password"))
                .andExpect(status().isOk());
        Assertions.assertNull(userRepo.findByUsername(username));
    }

    @ParameterizedTest
    @CsvSource({
            "stalker,123456",
            "phantom,654321"
    })
    public void userAlreadyExistTest(String username, String password) throws Exception {
        this.mockMvc.perform(post("/registration")
                        .param("username", username)
                        .param("password", password)
                        .with(csrf()))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "User already exist!"))
                .andExpect(status().isOk());
        Assertions.assertNotNull(userRepo.findByUsername(username));
        Assertions.assertEquals(username, userRepo.findByUsername(username).getUsername());
        Assertions.assertEquals(password, userRepo.findByUsername(username).getPassword());
    }

    @ParameterizedTest
    @CsvSource({
            "Bethzarill,Гидразинокарбонилметилбромфенилдигидробенздиазепинф"
    })
    public void createTooLongPasswordUserTest(String username, String password) throws Exception {
        this.mockMvc.perform(post("/registration")
                        .param("username", username)
                        .param("password", password)
                        .with(csrf()))
                .andExpect(model().attribute("message", "Incorrect username or password"))
                .andExpect(status().isOk());
        Assertions.assertNull(userRepo.findByUsername(username));
    }

    @ParameterizedTest
    @CsvSource({
            "тифлосурдоолигофренопедагогикаф,4JD9ujpF92"
    })
    public void createTooLongUsernameUserTest(String username, String password) throws Exception {
        this.mockMvc.perform(post("/registration")
                        .param("username", username)
                        .param("password", password)
                        .with(csrf()))
                .andExpect(model().attribute("message", "Incorrect username or password"))
                .andExpect(status().isOk());
        Assertions.assertNull(userRepo.findByUsername(username));
    }
}
