package co.com.redeban.api.controller;

import co.com.redeban.api.dto.ConfigMolParameterRequestDTO;
import co.com.redeban.api.dto.ConfigMolParameterResponseDTO;
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
        return configMolUseCase.getConfigMolParameter(requestDTO.getFiid(), requestDTO.getModule()).map(
                tuple -> {
                    ConfigMolParameterResponseDTO dto = new ConfigMolParameterResponseDTO();
                    dto.setConfigMol(tuple.getT1());
                    if (requestDTO.isMolActive()) {
                        dto.setParameterBank(tuple.getT2());
                    }
                    return dto;
                }
        );
    }
}

// validate fiid max 4, todos obligatorios
//QueryParam
//get 3 QueryParam
// fiid, modulo, flag

//flag cuando esta activo el mol no se va a la tabla parameter
//mol true y parameter true retorno parameter


//datos a retornar trans fiid type denialLimitSurpassed monto

// sin ohay info 409 y COD y message
// cod, message> "entidad no existe" dos en ingles, message

// Exception handler