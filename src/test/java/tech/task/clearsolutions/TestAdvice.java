package tech.task.clearsolutions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAdvice {

    public static <T> Set<ConstraintViolation<T>> getViolation(T testedClass) {
        return Validation.buildDefaultValidatorFactory()
                .getValidator()
                .validate(testedClass);
    }

    public static <T, F> void testInvalidField(T domainClass, F invalidValue,
                                               String expectedErrorMessage) {
        int invalidValuesInClass = 1;

        assertEquals(invalidValuesInClass, getViolation(domainClass).size());
        assertEquals(invalidValue, getViolation(domainClass).iterator().next().getInvalidValue());
        assertEquals(expectedErrorMessage, getViolation(domainClass)
                .iterator()
                .next()
                .getMessage());
    }

    public static <T> String asJsonString(final T object) throws JsonProcessingException {
        return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(object);
    }

}
