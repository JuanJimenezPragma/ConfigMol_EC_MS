package co.com.redeban.api.controller;

import co.com.redeban.api.dto.ConfigMolParameterRequestDTO;
import co.com.redeban.model.configmol.ConfigMol;
import co.com.redeban.model.parameterbank.ParameterBank;
import co.com.redeban.usecase.configmol.ConfigMolUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

class ConfigMolParameterControllerTest {

    @InjectMocks
    ConfigMolParameterController controller;


    @Mock
    private ConfigMolUseCase useCase;

    private ConfigMol configMol;

    private ParameterBank parameterBank;

    private ConfigMolParameterRequestDTO request;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        configMol = new ConfigMol("P2P", true, true);
        parameterBank = new ParameterBank("9634", "P2P#Configuration", "500");

        request = ConfigMolParameterRequestDTO.builder()
                .fiid("9634")
                .module("P2P")
                .molActive(true)
                .build();
    }

    @Test
    void testGetConfigParameters() {
        when(useCase.getConfigMolParameter("9634", "P2P"))
                .thenReturn(Mono.zip(Mono.just(configMol), Mono.just(parameterBank)));

        StepVerifier.create(controller.getConfigParameters(request))
                .expectNextMatches(res ->
                        res.getConfigMol() != null &&
                                res.getParameterBank() != null &&
                                res.getParameterBank().getFiid().equals("9634"))
                .verifyComplete();
    }

    @Test
    void testGetConfigParametersWhenMolActiveIsFalse() {
        ConfigMolParameterRequestDTO requestFalseMol = ConfigMolParameterRequestDTO.builder()
                .fiid("9634")
                .module("P2P")
                .molActive(false)
                .build();

        when(useCase.getConfigMolParameter("9634", "P2P"))
                .thenReturn(Mono.zip(Mono.just(configMol), Mono.just(parameterBank)));

        StepVerifier.create(controller.getConfigParameters(requestFalseMol))
                .expectNextMatches(res ->
                        res.getConfigMol() != null &&
                                res.getParameterBank() == null)
                .verifyComplete();
    }

    @Test
    void testGetConfigParametersWhenNoConfigFound() {
        when(useCase.getConfigMolParameter("9634", "P2P")).thenReturn(Mono.empty());

        StepVerifier.create(controller.getConfigParameters(request))
                .expectErrorMatches(throwable ->
                        throwable instanceof co.com.redeban.api.exception.MolParameterException &&
                                throwable.getMessage().contains("No configuration found"))
                .verify();
    }



}
