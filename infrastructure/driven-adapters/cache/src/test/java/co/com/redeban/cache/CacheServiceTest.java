package co.com.redeban.cache;

import com.github.benmanes.caffeine.cache.Cache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

public class CacheServiceTest {
    @Mock
    Cache<String, String> cacheParameterConfiguration;

    @InjectMocks
    CacheService cacheService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldExistsSuccessfully() {
        when(cacheParameterConfiguration.getIfPresent("1000")).thenReturn("1000");
    }


}
