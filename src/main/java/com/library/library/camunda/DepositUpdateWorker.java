//package com.library.library.camunda;
//
//import com.library.library.dto.account.DepositResponseDto;
//import io.camunda.zeebe.client.api.response.ActivatedJob;
//import io.camunda.zeebe.client.api.worker.JobClient;
//import io.camunda.zeebe.spring.client.annotation.JobWorker;
//import io.camunda.zeebe.spring.client.annotation.Variable;
//import lombok.RequiredArgsConstructor;
//import org.axonframework.commandhandling.gateway.CommandGateway;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class DepositUpdateWorker {
//
//    private final CommandGateway commandGateway;
//
//    @JobWorker(type = "account management")
//    public void handleJob(
//            JobClient client,
//            ActivatedJob job,
//            @Variable("deposit") DepositResponseDto depositDto
////            @Variable String depositNumber,
////            @Variable String depositRight,
////            @Variable String withdrawRight
//    ){
//    try{
//
//    }
//    }
//}
