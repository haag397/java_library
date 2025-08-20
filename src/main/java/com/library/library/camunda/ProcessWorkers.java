//package com.library.library.camunda;
//
//import com.library.library.dto.response_tempelate.TaskResponse;
//import io.camunda.zeebe.spring.client.annotation.JobWorker;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@Slf4j
//public class ProcessWorkers {
//
//    @JobWorker(type = "Service Task 1")
//    public Map<String, Object> serviceTask1(Map<String, Object> variables) {
//        return executeTask("Service Task 1", () -> {
//            // real logic: maybe call a service, query DB, etc.
//            return "Task 1 processed successfully";
//        }, variables);
//    }
//
//    @JobWorker(type = "Service Task 2")
//    public Map<String, Object> serviceTask2(Map<String, Object> variables) {
//        return executeTask("Service Task 2", () -> {
//            return "Task 2 completed fine";
//        }, variables);
//    }
//
//    @JobWorker(type = "Service Task 3")
//    public Map<String, Object> serviceTask3(Map<String, Object> variables) {
//        return executeTask("Service Task 3", () -> {
//            // Example of a failure
//            if (true) {
//                throw new RuntimeException("Service unavailable for Task 3");
//            }
//            return "Task 3 result";
//        }, variables);
//    }
//
//    @JobWorker(type = "Service Task 4")
//    public Map<String, Object> serviceTask4(Map<String, Object> variables) {
//        return executeTask("Service Task 4", () -> {
//            return "Task 4 result ready";
//        }, variables);
//    }
//
//    @JobWorker(type = "Aggregate Responses")
//    public Map<String, Object> aggregate(Map<String, Object> variables) {
//        log.info("Aggregating responses: {}", variables);
//
//        Map<String, TaskResponse> results = new HashMap<>();
//        variables.forEach((key, value) -> {
//            if (value instanceof Map<?, ?> map && map.containsKey("taskName")) {
//                TaskResponse tr = new TaskResponse(
//                        (String) map.get("taskName"),
//                        (String) map.get("status"),
//                        (String) map.get("result"),
//                        (String) map.get("errorMessage")
//                );
//                results.put(tr.getTaskName(), tr);
//            }
//        });
//
//        // put final aggregated response into process variables
//        variables.put("finalResponse", results);
//        return variables;
//    }
//
//    // common execution wrapper for all tasks
//    private Map<String, Object> executeTask(String taskName, TaskLogic logic, Map<String, Object> variables) {
//        TaskResponse response;
//        try {
//            String result = logic.run();
//            response = new TaskResponse(taskName, "SUCCESS", result, null);
//            log.info("{} succeeded: {}", taskName, result);
//        } catch (Exception e) {
//            response = new TaskResponse(taskName, "ERROR", null, e.getMessage());
//            log.error("{} failed: {}", taskName, e.getMessage());
//        }
//
//        variables.put(taskName.replace(" ", "_"), Map.of(
//                "taskName", response.getTaskName(),
//                "status", response.getStatus(),
//                "result", response.getResult(),
//                "errorMessage", response.getErrorMessage()
//        ));
//        return variables;
//    }
//
//    @FunctionalInterface
//    interface TaskLogic {
//        String run() throws Exception;
//    }
//}