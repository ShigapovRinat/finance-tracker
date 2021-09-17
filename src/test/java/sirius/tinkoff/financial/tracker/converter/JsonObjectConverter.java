package sirius.tinkoff.financial.tracker.converter;

import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JavaType;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class JsonObjectConverter {
    public static String convertObjectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }

    public static <T> T mvcResultToObject(MvcResult mvcResult, Class<T> objectType) {
        try {
            return new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), objectType);
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> List<T> mvcResultToList(MvcResult mvcResult, Class<T> elementType) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, elementType);
            return mapper.readValue(mvcResult.getResponse().getContentAsString(), type);
        } catch (IOException e) {
            return List.of();
        }
    }
}
