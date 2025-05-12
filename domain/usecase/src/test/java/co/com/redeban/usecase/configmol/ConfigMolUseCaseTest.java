package co.com.redeban.usecase.configmol;

import co.com.redeban.model.cache.CacheGateway;
import co.com.redeban.model.configmol.ConfigMol;
import co.com.redeban.model.configmol.gateways.ConfigMolRepository;
import co.com.redeban.model.parameterbank.ParameterBank;
import co.com.redeban.model.parameterbank.gateways.ParameterBankRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class ConfigMolUseCaseTest {

    @Mock
    private ConfigMolRepository configMolRepository;

    @Mock
    private ParameterBankRepository parameterBankRepository;

    @Mock
    private CacheGateway cacheGateway;

    @InjectMocks
    private ConfigMolUseCase useCase;

    @BeforeEach
    void setUp() {
        configMolRepository = mock(ConfigMolRepository.class);
        parameterBankRepository = mock(ParameterBankRepository.class);
        useCase = new ConfigMolUseCase(configMolRepository, parameterBankRepository, cacheGateway);
    }

    @Test
    void shouldReturnConfigMolByModuleName() {
        String moduleName = "P2P";
        ConfigMol mockConfig = ConfigMol.builder().moduleName(moduleName).build();

        when(configMolRepository.findByModuleName(moduleName)).thenReturn(Mono.just(mockConfig));

        StepVerifier.create(useCase.getConfigMol(moduleName))
                .expectNextMatches(config -> config.getModuleName().equals(moduleName))
                .verifyComplete();

        verify(configMolRepository).findByModuleName(moduleName);
    }

    @Test
    void shouldReturnConfigMolAndParameterBank() {
        String fiid = "FI123";
        String moduleName = "auth";
        String expectedParameterKey = moduleName + "#transactional";

        ConfigMol mockConfig = ConfigMol.builder().moduleName(moduleName).build();
        ParameterBank mockParam = ParameterBank.builder().type(expectedParameterKey).build();

        when(configMolRepository.findByModuleName(moduleName)).thenReturn(Mono.just(mockConfig));
        when(parameterBankRepository.getParameterBank(fiid, expectedParameterKey)).thenReturn(Mono.just(mockParam));

        StepVerifier.create(useCase.getConfigMolParameter(fiid, moduleName))
                .assertNext(tuple -> {
                    ConfigMol config = tuple.getT1();
                    ParameterBank param = tuple.getT2();
                    assert config.getModuleName().equals(moduleName);
                    assert param.getType().equals(expectedParameterKey);
                })
                .verifyComplete();

        verify(configMolRepository).findByModuleName(moduleName);
        verify(parameterBankRepository).getParameterBank(fiid, expectedParameterKey);
    }

}
