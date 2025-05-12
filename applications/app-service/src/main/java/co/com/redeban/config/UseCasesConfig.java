package co.com.redeban.config;

import co.com.redeban.model.cache.CacheGateway;
import co.com.redeban.model.configmol.gateways.ConfigMolRepository;
import co.com.redeban.model.parameterbank.gateways.ParameterBankRepository;
import co.com.redeban.usecase.configmol.ConfigMolUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

        @Bean
        public ConfigMolUseCase configMolUseCase(ConfigMolRepository configMolRepository, ParameterBankRepository parameterBankRepository, CacheGateway cacheGateway) {
                return new ConfigMolUseCase(configMolRepository, parameterBankRepository, cacheGateway);
        }

}
