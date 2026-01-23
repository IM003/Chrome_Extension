package com.example.chrome_extension;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeminiService {

    private static final String API_KEY = "AIzaSyCEgjJGXYYiOKC1F2bdVXe_TeQyM-gj6LQ";
    private static final String URL =
            "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key=" + API_KEY;

    private static final ObjectMapper mapper = new ObjectMapper();

    public String callGemini(String prompt) throws Exception {

        // Sécurisation du texte
        String safePrompt = prompt
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");

        String jsonBody = """
        {
          "contents": [
            {
              "parts": [
                {
                  "text": "%s"
                }
              ]
            }
          ]
        }
        """.formatted(safePrompt);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        // 🔴 Vérification HTTP
        if (response.statusCode() != 200) {
            throw new RuntimeException("Erreur HTTP Gemini: " + response.body());
        }

        JsonNode root = mapper.readTree(response.body());

        // 🔴 Cas d’erreur Gemini
        if (root.has("error")) {
            String message = root.path("error").path("message").asText("Erreur Gemini inconnue");
            throw new RuntimeException(message);
        }

        // ✅ Extraction SAFE (AUCUN get(0))
        JsonNode candidates = root.path("candidates");

        if (!candidates.isArray() || candidates.isEmpty()) {
            throw new RuntimeException("Aucune réponse générée par Gemini");
        }

        return candidates
                .path(0)
                .path("content")
                .path("parts")
                .path(0)
                .path("text")
                .asText("Traduction vide");
    }
}
