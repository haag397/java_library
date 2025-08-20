package com.library.library.controller.grok;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivateJobsResponse;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.response.DeploymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user-task")
@RequiredArgsConstructor
public class UserTaskController {

    private final ZeebeClient zeebeClient;

    @PostMapping("/deploy-form")
    public ResponseEntity<String> deployForm() {
        DeploymentEvent deployment = zeebeClient.newDeployResourceCommand()
                .addResourceFromClasspath("forms/user-confirmation-form.form")
                .send()
                .join();
        return ResponseEntity.ok("Form deployed: " + deployment.getKey());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Map<String, Object>>> getActiveTasks() {
        ActivateJobsResponse response = zeebeClient.newActivateJobsCommand()
                .jobType("io.camunda.zeebe:userTask")
                .maxJobsToActivate(20)
                .workerName("ui-list")// required to claim
                .timeout(Duration.ofMinutes(5))      // keep claimed briefly
                .fetchVariables("data","userData","validationResponse","userResponse")
                .send()
                .join();

        List<Map<String, Object>> tasks = new ArrayList<>();
        for (ActivatedJob job : response.getJobs()) {
            Map<String, Object> task = new HashMap<>();
            task.put("jobKey", job.getKey());
            task.put("elementId", job.getElementId());
            task.put("processInstanceKey", job.getProcessInstanceKey());
            task.put("variables", job.getVariablesAsMap());
            tasks.add(task);
        }
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/complete/{jobKey}")
    public ResponseEntity<String> completeTask(@PathVariable long jobKey, @RequestBody Map<String, Object> formData) {
        Boolean confirmed = (Boolean) formData.get("confirmed");
        if (confirmed == null || !confirmed) {
            zeebeClient.newThrowErrorCommand(jobKey)
                    .errorCode("USER_ERROR")
                    .errorMessage("Confirmation not provided")
                    .send()
                    .join();
            return ResponseEntity.badRequest().body("Confirmation required");
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("userData", formData.get("details"));
        variables.put("userResponse", Map.of("status", "success", "message", "User confirmed", "data", formData));

        zeebeClient.newCompleteCommand(jobKey)
                .variables(variables)
                .send()
                .join();

        return ResponseEntity.ok("Task completed");
    }
}