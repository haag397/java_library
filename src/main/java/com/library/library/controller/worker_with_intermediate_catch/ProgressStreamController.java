package com.library.library.controller.worker_with_intermediate_catch;

import com.library.library.dto.worker_with_intermediate_catch_dto.ProgressUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/flow/stream")
@RequiredArgsConstructor
public class ProgressStreamController {

    private final ProgressStreamService progress;

    @GetMapping(value = "/{applicationId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<ProgressUpdate>> stream(@PathVariable String applicationId) {
        return progress.stream(applicationId)
                .map(u -> ServerSentEvent.<ProgressUpdate>builder()
                        .id(u.at().toString())
                        .event("progress")
                        .data(u)
                        .build());
    }
}