package co.com.redeban.api.controller;

import co.com.redeban.api.dto.ConfigMolParameterRequestDTO;
import co.com.redeban.api.dto.ConfigMolParameterResponseDTO;
import co.com.redeban.api.exception.MolParameterException;
import co.com.redeban.usecase.configmol.ConfigMolUseCase;
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
public class ConfigMolParameterController {
    private final ConfigMolUseCase configMolUseCase;

    @GetMapping
    public Mono<ConfigMolParameterResponseDTO> getConfigParameters(@Valid @ModelAttribute ConfigMolParameterRequestDTO requestDTO) {
        return configMolUseCase.getConfigMolParameter(requestDTO.getFiid(), requestDTO.getModule())
                .map(tuple -> {
                            ConfigMolParameterResponseDTO dto = ConfigMolParameterResponseDTO.builder()
                                    .configMol(tuple.getT1())
                                    .build();
                            if (requestDTO.getMolActive() && tuple.getT1().isMol()) {
                                dto.setParameterBank(tuple.getT2());
                            }
                            return dto;
                        }
                ).switchIfEmpty(Mono.error(new MolParameterException("No configuration found for fiid=" + requestDTO.getFiid() + ", module=" + requestDTO.getModule())));
    }
}