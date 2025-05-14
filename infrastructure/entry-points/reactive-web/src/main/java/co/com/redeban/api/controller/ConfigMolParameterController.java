package co.com.redeban.api.controller;

import co.com.redeban.api.dto.ConfigMolParameterRequestDTO;
import co.com.redeban.api.dto.ConfigMolParameterResponseDTO;
import co.com.redeban.api.exception.MolParameterException;
import co.com.redeban.usecase.configmol.ConfigMolUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/config-mol", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Tag(name = "ConfigMolParameterController", description = "Controller ConfigMolParameter")
public class ConfigMolParameterController {
    private final ConfigMolUseCase configMolUseCase;

    @GetMapping
    @Operation(summary = "Get ConfigMol and ParameterBank", description = "this endpoint obtains the information from the tables in dynamoDB")
    public Mono<ConfigMolParameterResponseDTO> getConfigParameters(@Valid @ModelAttribute ConfigMolParameterRequestDTO requestDTO) {
        return configMolUseCase.getConfigMolParameter(requestDTO.getFiid(), requestDTO.getModule())
                .map(tuple -> {
                            ConfigMolParameterResponseDTO dto = ConfigMolParameterResponseDTO.builder()
                                    .configMol(tuple.getT1())
                                    .build();
                            if (Boolean.TRUE.equals(requestDTO.getMolActive()) && tuple.getT1().isMol()) {
                                dto.setParameterBank(tuple.getT2());
                            }
                            return dto;
                        }
                ).switchIfEmpty(Mono.error(new MolParameterException("No configuration found for fiid=" + requestDTO.getFiid() + ", module=" + requestDTO.getModule())));
    }
}