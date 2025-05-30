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
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

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
        MockitoAnnotations.openMocks(this);
        useCase = new ConfigMolUseCase(configMolRepository, parameterBankRepository, cacheGateway);
    }


    @Test
    void shouldReturnConfigMolAndParameterBank() {
        String fiid = "FI123";
        String moduleName = "auth";
        String expectedParameterKey = moduleName + "#transactional";

        ConfigMol mockConfig = ConfigMol.builder().moduleName(moduleName).build();
        ParameterBank mockParam = ParameterBank.builder().type(expectedParameterKey).build();

        when(cacheGateway.get(moduleName)).thenReturn(Optional.empty());
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

        verify(cacheGateway).get(moduleName);
        verify(configMolRepository).findByModuleName(moduleName);
        verify(parameterBankRepository).getParameterBank(fiid, expectedParameterKey);
    }

}
