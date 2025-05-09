package co.com.redeban.config;

import co.com.redeban.model.configmol.gateways.ConfigMolRepository;
import co.com.redeban.model.parameterbank.gateways.ParameterBankRepository;
import co.com.redeban.usecase.configmol.ConfigMolUseCase;
import co.com.redeban.usecase.parameterbank.ParameterBankUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(classes = UseCasesConfigTest.TestConfig.class)
public class UseCasesConfigTest {
    @Autowired
    private ConfigMolUseCase configMolUseCase;

    @Autowired
    private ParameterBankUseCase parameterBankUseCase;

    @Test
    void testUseCaseBeansExist() {
        assertNotNull(configMolUseCase, "ConfigMolUseCase should be injected");
        assertNotNull(parameterBankUseCase, "ParameterBankUseCase should be injected");
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        @Bean
        public ConfigMolRepository configMolRepository() {
            return Mockito.mock(ConfigMolRepository.class);
        }

        @Bean
        public ParameterBankRepository parameterBankRepository() {
            return Mockito.mock(ParameterBankRepository.class);
        }
    }
}