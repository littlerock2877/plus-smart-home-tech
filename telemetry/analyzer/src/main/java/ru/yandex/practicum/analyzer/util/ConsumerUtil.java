package ru.yandex.practicum.analyzer.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Component;

import java.util.Map;

@UtilityClass
@Component
@Slf4j
public class ConsumerUtil {
    public void manageOffsets(ConsumerRecord<String, ? extends SpecificRecordBase> record, int count,
                              Consumer<String, ? extends SpecificRecordBase> consumer,
                              Map<TopicPartition, OffsetAndMetadata> currentOffsets) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );
        if(count % 10 == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if(exception != null) {
                    log.warn("Error while commiting offsets: {}", offsets, exception);
                }
            });
        }
    }
}