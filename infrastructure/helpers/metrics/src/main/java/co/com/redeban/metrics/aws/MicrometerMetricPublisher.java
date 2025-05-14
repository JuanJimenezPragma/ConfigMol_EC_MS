package co.com.redeban.metrics.aws;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.metrics.MetricCollection;
import software.amazon.awssdk.metrics.MetricPublisher;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@AllArgsConstructor
public class MicrometerMetricPublisher implements MetricPublisher {
    private final ExecutorService service = Executors.newFixedThreadPool(10);
    private final MeterRegistry registry;

    @Override
    public void publish(MetricCollection metricCollection) {
        service.submit(() -> {
            List<Tag> tags = buildTags(metricCollection);
            metricCollection.stream()
                    .filter(trecord -> trecord.value() instanceof Duration || trecord.value() instanceof Integer)
                    .forEach(trecord -> {
                        if (trecord.value() instanceof Duration) {
                            registry.timer(trecord.metric().name(), tags).record((Duration) trecord.value());
                        } else if (trecord.value() instanceof Integer) {
                            registry.counter(trecord.metric().name(), tags).increment((Integer) trecord.value());
                        }
                    });
        });
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("La operación close no está soportada en esta clase.");
    }

    private List<Tag> buildTags(MetricCollection metricCollection) {
        return metricCollection.stream()
                .filter(recordt -> recordt.value() instanceof String || recordt.value() instanceof Boolean)
                .map(recordt -> Tag.of(recordt.metric().name(), recordt.value().toString()))
                .toList();
    }
}
