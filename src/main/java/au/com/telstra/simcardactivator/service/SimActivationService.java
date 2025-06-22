package au.com.telstra.simcardactivator.service;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
@Service
public class SimActivationService {
    public boolean activateSim(String iccid) {
        String actuatorUrl = "http://localhost:8444/actuate";

        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> request = new HashMap<>();
        request.put("iccid", iccid);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(actuatorUrl, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Boolean success = (Boolean) response.getBody().get("success");
            return success != null && success;
        } else {
            return false;
        }
    }
}
