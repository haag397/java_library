package com.library.library.event.grok;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationFailedEvent {
    private String processId;
    private String errorMessage;
}