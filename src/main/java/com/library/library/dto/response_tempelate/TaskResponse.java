package com.library.library.dto.response_tempelate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private String taskName;
    private String status;       // "SUCCESS" or "ERROR"
    private String result;       // success result
    private String errorMessage; // error details
}
