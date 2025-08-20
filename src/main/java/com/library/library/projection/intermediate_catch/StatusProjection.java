package com.library.library.projection.intermediate_catch;

import com.library.library.event.intermediate_catch.*;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StatusProjection {
    private final Map<String,String> status = new ConcurrentHashMap<>();

//    @EventHandler public void on(LoanApplicationStarted e){ status.put(e.applicationId(),"STARTED"); }
    @EventHandler
    public void on(LoanApplicationStarted e) {
        status.put(e.getApplicationId(), "STARTED");
    }
    @EventHandler public void on(AccountValidated e){ status.put(e.getApplicationId(),"ACCOUNT_VALIDATED"); }
    @EventHandler public void on(ValidationRequested e){ status.put(e.getApplicationId(),"AWAITING_USER_DATA"); }
    @EventHandler public void on(UserDataProvided e){ status.put(e.getApplicationId(),"DATA_PROVIDED"); }
    @EventHandler public void on(LoanGranted e){ status.put(e.getApplicationId(),"GRANTED"); }

    public String get(String id){ return status.getOrDefault(id,"UNKNOWN"); }
}