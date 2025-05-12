package co.com.redeban.api.controller.advice;

import co.com.redeban.api.controller.ConfigMolParameterController;
import co.com.redeban.api.dto.ConfigMolParameterResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RestControllerAdvice(basePackageClasses = ConfigMolParameterController.class)
public class ConfigMolParameterControllerAdvice {

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ConfigMolParameterResponseDTO>> handleValidationErrors(WebExchangeBindException ex) {
        String detailError = ex.getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ConfigMolParameterResponseDTO response = new ConfigMolParameterResponseDTO();
        response.setCodError("409");
        response.setError("Validation failed");
        response.setDetailError(detailError);

        return Mono.just(ResponseEntity.badRequest().body(response));
    }

}
