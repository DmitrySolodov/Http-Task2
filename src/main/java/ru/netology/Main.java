package ru.netology;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;

public class Main {
    public static ObjectMapper mapper = new ObjectMapper();
    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=ddevr5bmP7yeXDoCPOy03ogznnMtD57RtXcfdPu4");
        CloseableHttpResponse response = httpClient.execute(request);
        NasaDailyImage nasaDailyImage = mapper.readValue(response.getEntity().getContent(), new TypeReference<NasaDailyImage>() {});
        System.out.println(nasaDailyImage.getUrl());
        HttpGet nasaDailyImageRequest = new HttpGet(nasaDailyImage.getUrl());
        CloseableHttpResponse imageResponce = httpClient.execute(nasaDailyImageRequest);
        try (FileOutputStream fos = new FileOutputStream(
                nasaDailyImage.getUrl().substring(nasaDailyImage.getUrl().lastIndexOf("/") + 1),false)) {
            byte[] bytes = imageResponce.getEntity().getContent().readAllBytes();
            fos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
