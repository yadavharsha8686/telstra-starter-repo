package au.com.telstra.simcardactivator.controller;

import au.com.telstra.simcardactivator.AppConfig;
import au.com.telstra.simcardactivator.model.SimActivation;
import au.com.telstra.simcardactivator.model.SimActivationRequest;
import au.com.telstra.simcardactivator.repository.SimActivationRepository;
import au.com.telstra.simcardactivator.service.SimActivationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")

public class SimActivationController {
    @Autowired
    private SimActivationRepository repository;

    @Autowired
    private SimActivationService activationService;
    @Autowired
    private RestTemplate restTemplate;



    /* @PostMapping("/activate-sim")
    public String activateSim(@RequestBody SimActivationRequest request) {
        boolean success = activationService.activateSim(request.getIccid());
        return success ? "SIM activation successful" : "SIM activation failed";
    }*/
   @PostMapping("/activate-sim")
   public ResponseEntity<String> activateSim(@RequestBody Map<String, String> request) {
       String iccid = request.get("iccid");
       String customerEmail = request.get("customerEmail");

       // Call actuator
       ResponseEntity<Map> response = restTemplate.postForEntity(
               "http://localhost:8444/actuate",
               Map.of("iccid", iccid),
               Map.class
       );

       boolean success = (Boolean) response.getBody().get("success");

       // Save to DB
       repository.save(new SimActivation(iccid, customerEmail, success));

       return ResponseEntity.ok(success ? "Activation successful" : "Activation failed");
   }
    @GetMapping("/query")
    public ResponseEntity<?> getSimById(@RequestParam Long simCardId) {
        Optional<SimActivation> result = repository.findById(simCardId);
        if (result.isPresent()) {
            SimActivation sim = result.get();
            return ResponseEntity.ok(Map.of(
                    "iccid", sim.getIccid(),
                    "customerEmail", sim.getCustomerEmail(),
                    "active", sim.isActive()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SIM record not found");
        }
    }
}
