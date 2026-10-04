package offer_tracker.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import offer_tracker.exception.AiServiceException;
import offer_tracker.exception.AiUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public class AiQuestionClient {

    private static final int QUESTION_COUNT = 10;

    private static final String SYSTEM_PROMPT = "You are an experienced interviewer who writes realistic interview questions.";

    private static final String USER_PROMPT = """
            Generate %d interview questions for the job below.
            Mix technical questions based on the required skills with a few behavioral questions.

            Company: %s
            Position: %s
            Job description:
            %s

            Return only a JSON object in this format: {"questions": ["question 1", "question 2"]}
            """;

    private final RestClient restClient;
    private final JsonMapper jsonMapper;
    private final String model;
    private final boolean enabled;

    public AiQuestionClient(@Value("${app.ai.base-url}") String baseUrl,
                            @Value("${app.ai.api-key}") String apiKey,
                            @Value("${app.ai.model}") String model,
                            JsonMapper jsonMapper) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(10));
        requestFactory.setReadTimeout(Duration.ofSeconds(60));

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .requestFactory(requestFactory)
                .build();
        this.jsonMapper = jsonMapper;
        this.model = model;
        this.enabled = !apiKey.isBlank();
    }

    public List<String> generateQuestions(String company, String position, String jobDescription) {
        if (!enabled) {
            throw new AiUnavailableException();
        }

        String userPrompt = USER_PROMPT.formatted(QUESTION_COUNT, company, position, jobDescription);
        ChatRequest request = new ChatRequest(
                model,
                List.of(new Message("system", SYSTEM_PROMPT), new Message("user", userPrompt)),
                0.7,
                new ResponseFormat("json_object")
        );

        try {
            ChatResponse response = restClient.post()
                    .uri("/chat/completions")
                    .body(request)
                    .retrieve()
                    .body(ChatResponse.class);

            if (response == null || response.choices() == null || response.choices().isEmpty()) {
                throw new AiServiceException("AI returned an empty response", null);
            }
            String content = response.choices().get(0).message().content();
            List<String> questions = jsonMapper.readValue(content, QuestionList.class).questions();
            if (questions == null || questions.isEmpty()) {
                throw new AiServiceException("AI returned no questions", null);
            }
            return questions;
        } catch (RestClientException | JacksonException ex) {
            log.error("AI request failed", ex);
            throw new AiServiceException("Failed to generate questions from AI service", ex);
        }
    }

    record ChatRequest(String model,
                       List<Message> messages,
                       double temperature,
                       @JsonProperty("response_format") ResponseFormat responseFormat) {
    }

    record Message(String role, String content) {
    }

    record ResponseFormat(String type) {
    }

    record ChatResponse(List<Choice> choices) {
    }

    record Choice(Message message) {
    }

    record QuestionList(List<String> questions) {
    }
}