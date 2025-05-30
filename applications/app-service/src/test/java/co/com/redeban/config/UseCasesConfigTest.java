package co.com.redeban.config;

import co.com.redeban.model.cache.CacheGateway;
import co.com.redeban.model.configmol.gateways.ConfigMolRepository;
import co.com.redeban.model.parameterbank.gateways.ParameterBankRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertTrue;


class UseCasesConfigTest {


    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            String[] beanNames = context.getBeanDefinitionNames();

            boolean useCaseBeanFound = false;
            for (String beanName : beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true;
                    break;
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'Use Case' were found");
        }
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

        @Bean
        public CacheGateway cacheGateway() {
            return Mockito.mock(CacheGateway.class);
        }
    }
}