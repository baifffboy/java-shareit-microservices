package ru.practicum.shareit.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BaseClientTest {

    private RestTemplate restTemplate;
    private TestClient testClient;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        testClient = new TestClient(restTemplate);
    }

    @Test
    void shouldMakeGetRequest() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), eq(Object.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = testClient.get("/test");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldMakeGetRequestWithUserId() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), eq(Object.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = testClient.get("/test", 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldMakePostRequest() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), eq(Object.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = testClient.post("/test", "body");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldMakePostRequestWithUserId() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), eq(Object.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = testClient.post("/test", 1L, "body");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldMakePatchRequest() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), eq(Object.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = testClient.patch("/test", "body");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldMakeDeleteRequest() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), eq(Object.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = testClient.delete("/test");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // Тестовый класс для BaseClient
    private static class TestClient extends BaseClient {
        public TestClient(RestTemplate rest) {
            super(rest);
        }
    }
}
