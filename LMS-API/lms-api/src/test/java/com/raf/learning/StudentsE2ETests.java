package com.raf.learning;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class StudentsE2ETests {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    @Test
    public void testRafStudentsGetApi() {
        String result;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://192.168.124.28:8091/api/v1/students"))
                    .build();

            java.net.http.HttpResponse<String> response = CLIENT.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            result = response.body();
            System.out.println("### response: \n" + result);
            assertThat(response.statusCode(), is(equalTo(200)));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}