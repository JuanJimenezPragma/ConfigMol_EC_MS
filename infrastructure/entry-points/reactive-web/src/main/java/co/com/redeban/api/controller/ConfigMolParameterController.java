package co.com.redeban.api.controller;

import co.com.redeban.api.dto.ConfigMolParameterRequestDTO;
import co.com.redeban.api.dto.ConfigMolParameterResponseDTO;
import co.com.redeban.api.exception.MolParameterException;
import co.com.redeban.usecase.configmol.ConfigMolUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(
            summary = "Get ConfigMol and ParameterBank",
            description = "Returns configuration data from ConfigMol and optionally ParameterBank based on FIID, module, and molActive parameters.",
            parameters = {
                    @Parameter(name = "fiid", description = "FIID to search the configuration for", required = true),
                    @Parameter(name = "module", description = "Module name associated with the configuration", required = true),
                    @Parameter(name = "molActive", description = "Boolean flag to include ParameterBank info if true", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Configuration found",
                            content = @Content(schema = @Schema(implementation = ConfigMolParameterResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Validation failed C409-000",
                            content = @Content(schema = @Schema(implementation = ConfigMolParameterResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Data not found B404-000",
                            content = @Content(schema = @Schema(implementation = ConfigMolParameterResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error I500-000",
                            content = @Content(schema = @Schema(implementation = ConfigMolParameterResponseDTO.class))
                    )
            }
    )
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