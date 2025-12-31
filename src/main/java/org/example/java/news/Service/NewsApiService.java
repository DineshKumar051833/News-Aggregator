package org.example.java.news.Service;

import org.example.java.news.Config.GNewsConfig;
import org.example.java.news.Model.NewsArticlesResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class NewsApiService {

    private final RestTemplate restTemplate;
    private final GNewsConfig config;

    public NewsApiService(RestTemplate restTemplate, GNewsConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    public NewsArticlesResponse getTopHeadLines(String topic, String language, int max) {
        String safeTopic    = (topic == null || topic.isBlank()) ? "general" : topic.trim();
        String safeLanguage = (language == null || language.isBlank()) ? "en" : language.trim();
        int safeMax         = Math.max(1, Math.min(max, 50));

        URI uri = UriComponentsBuilder
                .fromUriString(config.getBaseUrl() + "/top-headlines")
                .queryParam("topic", safeTopic)
                .queryParam("lang", safeLanguage)
                .queryParam("max", safeMax)
                .queryParam("apikey", config.getKey())
                .build(true)
                .toUri();
        return executeRequest(uri);
    }

    public NewsArticlesResponse getEverything(String query, String language, int max) {
        String safeQuery    = (query == null || query.isBlank()) ? "latest" : query.trim();
        String safeLanguage = (language == null || language.isBlank()) ? "en" : language.trim();
        int safeMax         = Math.max(1, Math.min(max, 50));

        URI uri = UriComponentsBuilder
                .fromUriString(config.getBaseUrl() + "/search")
                .queryParam("q", safeQuery)
                .queryParam("lang", safeLanguage)
                .queryParam("max", safeMax)
                .queryParam("apikey", config.getKey())
                .build(true)
                .toUri();
        return executeRequest(uri);
    }

    public NewsArticlesResponse searchArticles(String query, String language, String fromDate,
                                               String toDate, String sortBy, int max) {
        String safeQuery    = (query == null || query.isBlank()) ? "latest" : query.trim();
        String safeLanguage = (language == null || language.isBlank()) ? "en" : language.trim();
        int safeMax         = Math.max(1, Math.min(max, 50));
        String safeSortBy   = (sortBy == null || sortBy.isBlank()) ? "publishedAt" : sortBy.trim();

        StringBuilder sb = new StringBuilder(config.getBaseUrl())
                .append("/search?")
                .append("q=").append(safeQuery)
                .append("&lang=").append(safeLanguage)
                .append("&sortby=").append(safeSortBy)
                .append("&max=").append(safeMax)
                .append("&apikey=").append(config.getKey());

        if (fromDate != null && !fromDate.isBlank()) sb.append("&from=").append(fromDate);
        if (toDate != null && !toDate.isBlank()) sb.append("&to=").append(toDate);
        URI uri = URI.create(sb.toString());
        return executeRequest(uri);
    }

    private NewsArticlesResponse executeRequest(URI uri) {
        try {
            ResponseEntity<NewsArticlesResponse> response =
                    restTemplate.exchange(uri, HttpMethod.GET, null, NewsArticlesResponse.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new IllegalStateException("Request failed: " + response.getStatusCode());
            }

            return response.getBody();
        } catch (RestClientException exception) {
            throw new IllegalStateException("Failed to reach GNews API", exception);
        }
    }
}