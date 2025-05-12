package co.com.redeban.api.controller;

import co.com.redeban.api.dto.ConfigMolParameterRequestDTO;
import co.com.redeban.api.dto.ConfigMolParameterResponseDTO;
import co.com.redeban.model.configmol.ConfigMol;
import co.com.redeban.model.parameterbank.ParameterBank;
import co.com.redeban.usecase.configmol.ConfigMolUseCase;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConfigMolParameterControllerTest {

    @InjectMocks
    ConfigMolParameterController controller;

    @Mock
    private Validator validator;

    @Mock
    private ConfigMolUseCase useCase;

    private ConfigMol configMol;

    private ParameterBank parameterBank;

    private ConfigMolParameterRequestDTO request;

    private ConfigMolParameterResponseDTO response;

    @BeforeEach
    void setUp() {
        validator = mock(Validator.class);
        useCase = mock(ConfigMolUseCase.class);
        MockitoAnnotations.initMocks(this);

        parameterBank = new ParameterBank("9634","P2P#Configuration", "500");
        configMol = new ConfigMol("P2P",true,true);

        request = ConfigMolParameterRequestDTO.builder()
                .fiid("9634")
                .module("P2P")
                .molActive(true)
                .build();

        response = ConfigMolParameterResponseDTO.builder()
                .configMol(configMol)
                .parameterBank(parameterBank)
                .build();

    }

    @Test
    void testGetConfigParameters() {
       when(useCase.getConfigMolParameter("9634","P2P")).thenReturn(Mono.zip(Mono.just(configMol),Mono.just(parameterBank)));

        controller.getConfigParameters(request).as(StepVerifier::create)
                .expectNextMatches(res ->
                        res.getConfigMol() != null && res.getParameterBank() != null)
                .verifyComplete();
    }

}
