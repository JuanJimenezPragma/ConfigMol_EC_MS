package co.com.redeban.metrics.aws;

import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import io.micrometer.core.instrument.logging.LoggingRegistryConfig;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.metrics.internal.EmptyMetricCollection;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MicrometerMetricPublisherTest {

    @Test
    void metricTest() {
        LoggingMeterRegistry loggingMeterRegistry = LoggingMeterRegistry
            .builder(LoggingRegistryConfig.DEFAULT)
            .build();

        MicrometerMetricPublisher micrometerMetricPublisher = new MicrometerMetricPublisher(loggingMeterRegistry);

        micrometerMetricPublisher.publish(EmptyMetricCollection.create());
        assertThrows(UnsupportedOperationException.class, micrometerMetricPublisher::close);

        assertNotNull(micrometerMetricPublisher);

    }
}