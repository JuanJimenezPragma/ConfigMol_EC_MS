package co.com.redeban.cache.config;

import co.com.redeban.model.configmol.ConfigMol;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import com.github.benmanes.caffeine.cache.Cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = CacheConfig.class)
@TestPropertySource(properties = {
        "durationExpire=60",
        "maximumSize=100"
})
class CacheConfigTest {

    @Autowired
    private Cache<String, ConfigMol> configMolCache;

    @Test
    void shouldCreateConfigMolCacheBean() {
        assertNotNull(configMolCache);

        ConfigMol config = ConfigMol.builder()
                .moduleName("P2P")
                .build();

        configMolCache.put("P2P", config);

        ConfigMol cached = configMolCache.getIfPresent("P2P");

        assertNotNull(cached);
        assertEquals("P2P", cached.getModuleName());
    }
}
