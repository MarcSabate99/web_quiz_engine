package engine.http.dto.response;

public class QuizNotFoundResponseDTO {
    public String message;

    public QuizNotFoundResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
