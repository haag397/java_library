package com.library.library.dto.check_info;

public record AggregatedResult(
        TaskResult task1,
        TaskResult task2,
        TaskResult task3,
        TaskResult task4,
        boolean allOk) {}