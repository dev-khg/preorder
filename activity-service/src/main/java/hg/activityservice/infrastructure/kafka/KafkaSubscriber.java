package hg.activityservice.infrastructure.kafka;

import com.example.commonproject.activity.ActivityEvent;
import com.example.commonproject.event.Topic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hg.activityservice.core.service.ActivityFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaSubscriber {
    private final ActivityFacade activityFacade;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = Topic.ACTIVITY_EVENT, groupId = "activity-service")
    public void consume(String message) {
        try {
            ActivityEvent event = objectMapper.readValue(message, ActivityEvent.class);
            log.info("type=[{}] user=[{}] target=[{}]", event.getType(), event.getUserId(), event.getTargetId());
            activityFacade.handleActivityEvent(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
