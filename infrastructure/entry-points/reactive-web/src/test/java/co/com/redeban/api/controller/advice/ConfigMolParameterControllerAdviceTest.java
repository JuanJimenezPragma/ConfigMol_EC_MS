package co.com.redeban.api.controller.advice;

import co.com.redeban.api.dto.ConfigMolParameterResponseDTO;
import co.com.redeban.api.enums.MolParameterErrorsEnum;
import co.com.redeban.api.exception.MolParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConfigMolParameterControllerAdviceTest {

    private ConfigMolParameterControllerAdvice advice;

    @BeforeEach
    void setUp() {
        advice = new ConfigMolParameterControllerAdvice();
    }

    @Test
    void handleValidationErrors_shouldReturn409WithValidationDetails() {
        // Arrange
        BindingResult bindingResult = mock(BindingResult.class);
        WebExchangeBindException ex = new WebExchangeBindException(null, bindingResult);

        List<FieldError> fieldErrors = List.of(
                new FieldError("object", "field1", "must not be null"),
                new FieldError("object", "field2", "must not be empty")
        );

        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        Mono<ResponseEntity<ConfigMolParameterResponseDTO>> result = advice.handleValidationErrors(ex);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
                    ConfigMolParameterResponseDTO body = response.getBody();
                    assertNotNull(body);
                    assertEquals(MolParameterErrorsEnum.VALIDATION_ERROR.getCode(), body.getCodError());
                    assertEquals(MolParameterErrorsEnum.VALIDATION_ERROR.getMessage(), body.getError());
                    assertTrue(body.getDetailError().contains("field1: must not be null"));
                    assertTrue(body.getDetailError().contains("field2: must not be empty"));
                })
                .verifyComplete();
    }

    @Test
    void handleNotFoundException_shouldReturn404WithErrorMessage() {
        // Arrange
        String errorMessage = "No se encontró configuración";
        MolParameterException ex = new MolParameterException(errorMessage);

        Mono<ResponseEntity<ConfigMolParameterResponseDTO>> result = advice.handleNotFoundException(ex);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                    ConfigMolParameterResponseDTO body = response.getBody();
                    assertNotNull(body);
                    assertEquals(MolParameterErrorsEnum.DATA_NOT_FOUND.getCode(), body.getCodError());
                    assertEquals(MolParameterErrorsEnum.DATA_NOT_FOUND.getMessage(), body.getError());
                    assertEquals(errorMessage, body.getDetailError());
                })
                .verifyComplete();
    }

}
