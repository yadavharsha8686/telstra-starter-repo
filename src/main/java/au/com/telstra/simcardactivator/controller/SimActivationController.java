package au.com.telstra.simcardactivator.controller;

import au.com.telstra.simcardactivator.model.SimActivationRequest;
import au.com.telstra.simcardactivator.service.SimActivationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")

public class SimActivationController {
    @Autowired
    private SimActivationService activationService;

    @PostMapping("/activate-sim")
    public String activateSim(@RequestBody SimActivationRequest request) {
        boolean success = activationService.activateSim(request.getIccid());
        return success ? "SIM activation successful" : "SIM activation failed";
    }
}
