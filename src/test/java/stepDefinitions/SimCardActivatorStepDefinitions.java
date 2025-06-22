package stepDefinitions;

import au.com.telstra.simcardactivator.SimCardActivator;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import io.cucumber.java.en.*;
import org.springframework.http.*;


import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = SimCardActivator.class, loader = SpringBootContextLoader.class)
public class SimCardActivatorStepDefinitions {
    @Autowired
    private TestRestTemplate restTemplate;
    private String iccid;
    private String email;
    private ResponseEntity<String> activationResponse;

    @Given("the SIM card ICCID is {string} and the email is {string}")
    public void the_sim_card_iccid_is_and_the_email_is(String iccid, String email) {
        this.iccid = iccid;
        this.email = email;
    }

    @When("the user activates the SIM card")
    public void the_user_activates_the_sim_card() {
        String url = "http://localhost:8080/api/activate-sim";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> payload = Map.of("iccid", iccid, "customerEmail", email);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);
        activationResponse = restTemplate.postForEntity(url, request, String.class);
    }

    @Then("the activation should be successful for ID {int}")
    public void the_activation_should_be_successful_for_id(Integer id) {
        String queryUrl = "http://localhost:8080/api/query?simCardId=" + id;
        ResponseEntity<Map> queryResponse = restTemplate.getForEntity(queryUrl, Map.class);
        Map body = queryResponse.getBody();

        assertEquals("1255789453849037777", body.get("iccid"));
        assertEquals("success@example.com", body.get("customerEmail"));
        assertTrue((Boolean) body.get("active"));
    }

    @Then("the activation should fail for ID {int}")
    public void the_activation_should_fail_for_id(Integer id) {
        String queryUrl = "http://localhost:8080/api/query?simCardId=" + id;
        ResponseEntity<Map> queryResponse = restTemplate.getForEntity(queryUrl, Map.class);
        Map body = queryResponse.getBody();

        assertEquals("8944500102198304826", body.get("iccid"));
        assertEquals("fail@example.com", body.get("customerEmail"));
        assertFalse((Boolean) body.get("active"));
    }

}