package com.example.learningjava.azure_functions;

import com.example.learningjava.exceptions.AzureFunctionException;
import com.example.learningjava.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class AzureFunctionClient {

    private final RestTemplate restTemplate;
    private final String functionUrl;

    public AzureFunctionClient(
            RestTemplate restTemplate,
            @Value("${azure.function.base-url}") String baseUrl,
            @Value("${azure.function.process-employee-path}") String path
    ) {
        this.restTemplate = restTemplate;
        this.functionUrl = baseUrl + path;
    }

    public Employee processEmployee(Employee employee) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Employee> request =
                new HttpEntity<>(employee, headers);

        try {
            ResponseEntity<Employee> response =
                    restTemplate.postForEntity(
                            functionUrl,
                            request,
                            Employee.class
                    );

            log.info(
                    "Azure Function called successfully. Status={}",
                    response.getStatusCode()
            );

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new AzureFunctionException(
                        "Azure Function returned empty response",
                        new IllegalStateException("Empty response body")
                );
            }

                return response.getBody();

        } catch (HttpStatusCodeException ex) {
            log.error(
                    "Azure Function returned error. Status={}, Body={}",
                    ex.getStatusCode(),
                    ex.getResponseBodyAsString()
            );
            throw new AzureFunctionException("Azure Function error", ex);

        } catch (ResourceAccessException ex) {
            log.error("Azure Function timeout or connectivity issue", ex);
            throw new AzureFunctionException("Azure Function unreachable", ex);
        }
    }
}


