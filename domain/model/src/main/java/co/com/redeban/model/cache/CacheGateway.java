package co.com.redeban.model.cache;

import co.com.redeban.model.configmol.ConfigMol;

import java.util.Optional;

public interface CacheGateway {
    Optional<ConfigMol> get(String moduleName);
    void put(String moduleName, ConfigMol configMol);
}
