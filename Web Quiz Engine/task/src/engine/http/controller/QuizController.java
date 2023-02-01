package engine.http.controller;

import engine.http.dto.response.QuizGetterResponseDTO;
import engine.http.dto.response.QuizValidateResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class QuizController {
    QuizGetterResponseDTO quizGetterDTO = new QuizGetterResponseDTO();
    private final int CORRECT = 2;
    @GetMapping(value = "/api/quiz")
    @ResponseBody
    public ResponseEntity<Object> handleGetQuiz() {
        quizGetterDTO.setOptions(new String[] {"Robot","Tea leaf","Cup of coffee","Bug"});
        quizGetterDTO.setText("What is depicted on the Java logo?");
        quizGetterDTO.setTitle("The Java Logo");

        return new ResponseEntity<>(quizGetterDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/api/quiz")
    @ResponseBody
    public ResponseEntity<Object> handlePostQuiz(
            @RequestParam int answer
    ) {
        QuizValidateResponseDTO quizValidateResponseDTO = new QuizValidateResponseDTO();
        if(answer == CORRECT) {
            quizValidateResponseDTO.setSuccess(true);
            quizValidateResponseDTO.setFeedback("Congratulations, you're right!");
            return new ResponseEntity<>(quizValidateResponseDTO, HttpStatus.OK);
        }
        quizValidateResponseDTO.setSuccess(false);
        quizValidateResponseDTO.setFeedback("Wrong answer! Please, try again.");
        return new ResponseEntity<>(quizValidateResponseDTO, HttpStatus.OK);
    }
}
