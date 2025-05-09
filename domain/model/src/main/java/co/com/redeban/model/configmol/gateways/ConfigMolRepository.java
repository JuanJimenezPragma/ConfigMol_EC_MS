package co.com.redeban.model.configmol.gateways;

import co.com.redeban.model.configmol.ConfigMol;
import reactor.core.publisher.Mono;

public interface ConfigMolRepository {
    Mono<ConfigMol> findByModuleName(String moduleName);
}
