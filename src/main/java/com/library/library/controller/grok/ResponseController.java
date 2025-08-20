//package com.library.library.controller.grok;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.camunda.operate.CamundaOperateClient;
//import io.camunda.operate.dto.Variable;
//import io.camunda.operate.exception.OperateException;
//import io.camunda.operate.search.SearchQuery;
//import io.camunda.operate.search.VariableFilter;
//import io.camunda.zeebe.client.ZeebeClient;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
//@RestController
//@RequiredArgsConstructor
//public class ResponseController {
//
//    private final ZeebeClient zeebeClient;
//
//    private ObjectMapper objectMapper;
//
//    private CamundaOperateClient operateClient;
//    // این یک راه ساده برای گرفتن variables است؛ در تولید از Operate استفاده کنید
//    @GetMapping("/response/{instanceKey}")
//    public ResponseEntity<Map<String, Object>> getResponses(@PathVariable long instanceKey) {
//        try {
//            // ایجاد VariableFilter
//            VariableFilter variableFilter = new VariableFilter();
//            variableFilter.setProcessInstanceKey(instanceKey);
//
//            // ایجاد SearchQuery با استفاده از Builder
//            SearchQuery query = new SearchQuery.Builder()
//                    .filter(variableFilter)
//                    .build();
//
//            // دریافت متغیرها از Camunda Operate
//            List<Variable> variables = operateClient.searchVariables(query);
//            Map<String, Object> response = new HashMap<>();
//            response.put("instanceKey", instanceKey);
//
//            if (variables.isEmpty()) {
//                response.put("note", "No variables found for process instance " + instanceKey);
//                return ResponseEntity.ok(response);
//            }
//
//            // Parse متغیرهای JSON به اشیاء
//            for (Variable variable : variables) {
//                try {
//                    Object value = objectMapper.readValue(variable.getValue(), Object.class);
//                    response.put(variable.getName(), value);
//                } catch (Exception e) {
//                    response.put(variable.getName(), variable.getValue());
//                    response.put("warning", "Failed to parse variable " + variable.getName() + ": " + e.getMessage());
//                }
//            }
//
//            return ResponseEntity.ok(response);
//        } catch (OperateException e) {
//            return ResponseEntity.status(500).body(Map.of(
//                    "error", "Failed to fetch variables from Operate: " + e.getMessage(),
//                    "instanceKey", instanceKey
//            ));
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(Map.of(
//                    "error", "Unexpected error: " + e.getMessage(),
//                    "instanceKey", instanceKey
//            ));
//        }
//    }
//}