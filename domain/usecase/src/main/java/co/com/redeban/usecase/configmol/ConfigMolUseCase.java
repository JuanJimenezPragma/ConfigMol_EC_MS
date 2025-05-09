package co.com.redeban.usecase.configmol;

import co.com.redeban.model.configmol.ConfigMol;
import co.com.redeban.model.configmol.gateways.ConfigMolRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ConfigMolUseCase {
    private final ConfigMolRepository configMolRepository;

    public Mono<ConfigMol> getConfigMol(String moduleName) {
        return configMolRepository.findByModuleName(moduleName);
    }
}
