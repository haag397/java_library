package com.library.library.event.grok;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckingSucceededEvent {
    private String processId;
    private String message;
}