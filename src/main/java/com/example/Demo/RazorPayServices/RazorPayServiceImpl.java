package com.example.Demo.RazorPayServices;

import com.example.Demo.Model.Donations;
import com.example.Demo.RazorPayServices.RazorPayService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
public class RazorPayServiceImpl implements RazorPayService {

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecret;

    private final RestTemplate restTemplate;

    public RazorPayServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String createOrder(Donations donations) {
        String razorpayUrl = "https://api.razorpay.com/v1/orders";

        // Construct request payload
        String payload = "{\"amount\":" + donations.getAmount() + ",\"currency\":\"INR\"}";

        // Set up request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", getAuthorizationHeader()); // Set the Authorization header

        // Make POST request to Razorpay API
        ResponseEntity<String> response = restTemplate.exchange(
                razorpayUrl,
                HttpMethod.POST,
                new HttpEntity<>(payload, headers),
                String.class
        );

        // Extract order ID from response
        if (response.getStatusCode() == HttpStatus.OK) { // Check for HTTP status 201 Created
            return response.getBody(); // Assuming Razorpay API returns order ID directly
        } else {
            throw new RuntimeException("Failed to generate order ID: " + response.getBody());
        }
    }

    private String getAuthorizationHeader() {
        String credentials = apiKey + ":" + apiSecret;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        return "Basic " + encodedCredentials;
    }
}
