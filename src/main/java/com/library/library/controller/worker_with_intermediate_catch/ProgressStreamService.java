package com.library.library.controller.worker_with_intermediate_catch;

import com.library.library.dto.worker_with_intermediate_catch_dto.ProgressUpdate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProgressStreamService {

    private final Map<String, Sinks.Many<ProgressUpdate>> sinks = new ConcurrentHashMap<>();

    public Flux<ProgressUpdate> stream(String applicationId) {
        var sink = sinks.computeIfAbsent(applicationId, k -> Sinks.many().multicast().onBackpressureBuffer());
        // heartbeat to keep proxies happy
        var heartbeats = Flux.interval(Duration.ofSeconds(15))
                .map(i -> new ProgressUpdate(applicationId, "HEARTBEAT", "ALIVE", null, Map.of(), Instant.now()));
        return sink.asFlux().mergeWith(heartbeats);
    }

    public void emit(ProgressUpdate update) {
        sinks.computeIfAbsent(update.applicationId(), k -> Sinks.many().multicast().onBackpressureBuffer())
                .tryEmitNext(update);
    }

    public void complete(String applicationId) {
        var sink = sinks.remove(applicationId);
        if (sink != null) sink.tryEmitComplete();
    }
}