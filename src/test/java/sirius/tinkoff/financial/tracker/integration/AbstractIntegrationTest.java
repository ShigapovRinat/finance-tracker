package sirius.tinkoff.financial.tracker.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import sirius.tinkoff.financial.tracker.container.CustomPostgresContainer;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sirius.tinkoff.financial.tracker.converter.JsonObjectConverter.convertObjectToJson;
import static sirius.tinkoff.financial.tracker.converter.JsonObjectConverter.mvcResultToList;
import static sirius.tinkoff.financial.tracker.converter.JsonObjectConverter.mvcResultToObject;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = AbstractIntegrationTest.DockerPostgreDataSourceInitializer.class)
@Testcontainers
public abstract class AbstractIntegrationTest {

    public static PostgreSQLContainer<?> postgreDBContainer = CustomPostgresContainer.getCustomPostgresContainerInstance();

    static {
        postgreDBContainer.start();
    }

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    protected ResultActions getMock(String request, String login, ResultMatcher responseStatus) throws Exception {
        return this.mockMvc.perform(get(request).header("login", login))
                .andExpect(responseStatus);
    }

    protected ResultActions postMock(String request, String login, Object objectToJson, ResultMatcher responseStatus) throws Exception {
        return this.mockMvc.perform(post(request)
                .header("login", login)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(convertObjectToJson(objectToJson)))
                .andExpect(responseStatus);
    }

    protected ResultActions putMock(String request, String login, Object objectToJson, ResultMatcher responseStatus) throws Exception {
        return this.mockMvc.perform(put(request)
                .header("login", login)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(convertObjectToJson(objectToJson)))
                .andExpect(responseStatus);
    }

    protected ResultActions deleteMock(String request, String login, Object objectToJson, ResultMatcher responseStatus) throws Exception {
        return this.mockMvc.perform(delete(request)
                .header("login", login)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(convertObjectToJson(objectToJson)))
                .andExpect(responseStatus);
    }

    protected Object getObject(String get, String login, Class<?> ofClass) throws Exception {
        MvcResult mvcResult = getMock(get, login, status().isOk()).andReturn();
        return mvcResultToObject(mvcResult, ofClass);
    }

    protected <T> List<T> getList(String get, String login, Class<T> elementType) throws Exception {
        MvcResult mvcResult = getMock(get, login, status().isOk()).andReturn();
        return mvcResultToList(mvcResult, elementType);
    }

    public static class DockerPostgreDataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "spring.datasource.url=" + postgreDBContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreDBContainer.getUsername(),
                    "spring.datasource.password=" + postgreDBContainer.getPassword()
            );
        }
    }
}