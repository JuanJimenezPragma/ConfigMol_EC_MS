package co.com.redeban.cache;

import co.com.redeban.model.cache.CacheGateway;
import co.com.redeban.model.configmol.ConfigMol;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CacheService implements CacheGateway {

    private final Cache<String, ConfigMol> cache;

    public CacheService(Cache<String, ConfigMol> cache) {
        this.cache = cache;
    }

    @Override
    public Optional<ConfigMol> get(String moduleName) {
        return Optional.ofNullable(cache.getIfPresent(moduleName));
    }

    @Override
    public void put(String moduleName, ConfigMol configMol) {
        cache.put(moduleName, configMol);
    }

}
