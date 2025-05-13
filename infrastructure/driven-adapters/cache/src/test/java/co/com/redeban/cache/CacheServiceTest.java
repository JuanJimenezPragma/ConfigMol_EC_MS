package co.com.redeban.cache;

import co.com.redeban.model.cache.CacheGateway;
import co.com.redeban.model.configmol.ConfigMol;
import com.github.benmanes.caffeine.cache.Cache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CacheServiceTest {


    private Cache<String, ConfigMol> mockCache;
    private CacheGateway cacheService;

    private final String MODULE_NAME = "P2P";
    private ConfigMol configMol;

    @BeforeEach
    void setUp() {
        mockCache = mock(Cache.class);
        cacheService = new CacheService(mockCache);

        configMol = ConfigMol.builder()
                .moduleName(MODULE_NAME)
                .build();
    }

    @Test
    void shouldReturnValueWhenPresentInCache() {
        when(mockCache.getIfPresent(MODULE_NAME)).thenReturn(configMol);

        Optional<ConfigMol> result = cacheService.get(MODULE_NAME);

        assertTrue(result.isPresent());
        assertEquals(configMol, result.get());
        verify(mockCache).getIfPresent(MODULE_NAME);
    }

    @Test
    void shouldReturnEmptyWhenNotPresentInCache() {
        when(mockCache.getIfPresent(MODULE_NAME)).thenReturn(null);

        Optional<ConfigMol> result = cacheService.get(MODULE_NAME);

        assertFalse(result.isPresent());
        verify(mockCache).getIfPresent(MODULE_NAME);
    }

    @Test
    void shouldPutValueInCache() {
        cacheService.put(MODULE_NAME, configMol);

        verify(mockCache).put(MODULE_NAME, configMol);
    }


}
