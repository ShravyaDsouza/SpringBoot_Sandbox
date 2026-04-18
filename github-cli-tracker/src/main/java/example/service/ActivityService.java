package example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.model.GithubEvent;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityService {
    private static final String GITHUB_EVENTS_URL = "https://api.github.com/users/%s/events";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ActivityService(){
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<String> fetch (String username){
        String json = fetchFromApi(username);
        List <GithubEvent> events = mapEvents(json);
        return formatEvents(events);
    }

    private String fetchFromApi(String username) {
        String URL = String.format(GITHUB_EVENTS_URL, username);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header("Accept", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();

            if (statusCode == 404) {
                throw new RuntimeException("GitHub user not found: " + username);
            }

            if (statusCode == 403) {
                throw new RuntimeException("API rate limit exceeded.");
            }

            if (statusCode != 200) {
                throw new RuntimeException("GitHub API error: " + statusCode);
            }

            return response.body();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to fetch data from GitHub.", e);
        }
    }

    private List<GithubEvent> mapEvents(String json) {
        List<GithubEvent> events = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(json);

            for (JsonNode event : root) {

                String type = event.path("type").asText();
                String repoName = event.path("repo").path("name").asText("unknown-repo");

                JsonNode payload = event.path("payload");

                int commitCount = payload.path("commits").size();
                String action = payload.path("action").asText(null);
                String refType = payload.path("ref_type").asText(null);

                GithubEvent gitHubEvent = new GithubEvent(
                        type,
                        repoName,
                        commitCount,
                        action,
                        refType
                );

                events.add(gitHubEvent);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse GitHub response.", e);
        }

        return events;
    }

        private List<String> formatEvents(List<GithubEvent> events) {
        List<String> output = new ArrayList<>();

        for (GithubEvent event : events) {

            String type = event.getType();
            String repo = event.getRepoName();

            switch (type) {

                case "PushEvent" -> {
                    int commitCount = event.getCommitCount();
                    if (commitCount == 0) {
                        output.add("- Pushed to " + repo);
                    } else {
                        output.add("- Pushed " + commitCount + " commit(s) to " + repo);
                    }
                }

                case "IssuesEvent" -> {
                    String action = safe(event.getAction());
                    output.add("- " + capitalize(action) + " an issue in " + repo);
                }

                case "WatchEvent" -> {
                    output.add("- Starred " + repo);
                }

                case "ForkEvent" -> {
                    output.add("- Forked " + repo);
                }

                case "CreateEvent" -> {
                    String refType = safe(event.getRefType());
                    output.add("- Created a new " + refType + " in " + repo);
                }

                case "PullRequestEvent" -> {
                    String action = safe(event.getAction());
                    output.add("- " + capitalize(action) + " a pull request in " + repo);
                }

                default -> {
                    output.add("- Performed " + type + " on " + repo);
                }
            }
        }

        return output;
    }

     private String capitalize(String value) {
        if (value == null || value.isBlank()) return value;
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }

    private String safe(String value) {
        return (value == null || value.isBlank()) ? "performed action on" : value;
    }
}
