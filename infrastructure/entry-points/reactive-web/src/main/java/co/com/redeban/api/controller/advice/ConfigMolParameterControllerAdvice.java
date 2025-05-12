package co.com.redeban.api.controller.advice;

import co.com.redeban.api.enums.MolParameterErrorsEnum;
import co.com.redeban.api.exception.MolParameterException;
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
        MolParameterErrorsEnum errorEx = MolParameterErrorsEnum.VALIDATION_ERROR;
        String detailError = ex.getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ConfigMolParameterResponseDTO response = ConfigMolParameterResponseDTO.builder()
                .codError(errorEx.getCode())
                .error(errorEx.getMessage())
                .detailError(detailError)
                .build();

        return Mono.just(ResponseEntity.status(409).body(response));
    }

    @ExceptionHandler(MolParameterException.class)
    public Mono<ResponseEntity<ConfigMolParameterResponseDTO>> handleNotFoundException(MolParameterException ex) {
        MolParameterErrorsEnum errorEx = MolParameterErrorsEnum.DATA_NOT_FOUND;
        ConfigMolParameterResponseDTO response = ConfigMolParameterResponseDTO.builder()
                .codError(errorEx.getCode())
                .error(errorEx.getMessage())
                .detailError(ex.getMessage())
                .build();

        return Mono.just(ResponseEntity.status(404).body(response));
    }

}
