package kg.peaksoft.peaksoftlmsb6.api;

import kg.peaksoft.peaksoftlmsb6.service.GroupService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties")

class GroupApiTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private GroupService groupService;

    @BeforeEach
    void setUp() throws Exception {


    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void createGroup() {

    }

    @Test
    void updateGroup() {
    }

    @Test
    void deleteGroup() {
    }

    @Test
    @Sql(scripts = "/scripts/script.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAllGroups() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwZWFrc29mdCBob3VzZSIsImV4cCI6MTY3NDYzOTUzMywiaWF0IjoxNjY5NDU1NTMzLCJlbWFpbCI6ImFkbWluQGdtYWlsLmNvbSJ9.lgIkCAoD_F3WUP560GZYNwVLKa_VaCMqge8Ms6PyVsQ");
        mvc.perform(get("/api/group")
                        .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
//                .andExpect((ResultMatcher) jsonPath("$[0].group", is("Group name")));
    }

    @Test
    void getAllStudents() {
    }

    @Test
    void getGroupById() {
    }
}