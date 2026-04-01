package com.vasvass.ppmtool.services;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class MapValidationErrorServiceTest {

    private final MapValidationErrorService service = new MapValidationErrorService();

    @Test
    void withNoErrors_returnsNull() {
        BindingResult result = new MapBindingResult(new HashMap<>(), "target");

        ResponseEntity<?> response = service.MapValidationService(result);

        assertThat(response).isNull();
    }

    @Test
    void withErrors_returnsBadRequest() {
        MapBindingResult result = new MapBindingResult(new HashMap<>(), "target");
        result.addError(new FieldError("target", "field", "Field is required"));

        ResponseEntity<?> response = service.MapValidationService(result);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    void withErrors_responseBodyContainsFieldMessages() {
        MapBindingResult result = new MapBindingResult(new HashMap<>(), "target");
        result.addError(new FieldError("target", "projectName", "Project Name required!"));
        result.addError(new FieldError("target", "description", "Description is required"));

        ResponseEntity<?> response = service.MapValidationService(result);

        Map<String, String> body = (Map<String, String>) response.getBody();
        assertThat(body).containsEntry("projectName", "Project Name required!");
        assertThat(body).containsEntry("description", "Description is required");
    }
}
