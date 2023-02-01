package engine.http.controller;

import engine.entity.Completion;
import engine.entity.Quiz;
import engine.entity.User;
import engine.http.dto.request.QuizCreateDTO;
import engine.http.dto.request.QuizSolveDTO;
import engine.http.dto.response.QuizCreateResponseDTO;
import engine.http.dto.response.QuizGetterResponseDTO;
import engine.http.dto.response.QuizNotFoundResponseDTO;
import engine.http.dto.response.QuizValidateResponseDTO;
import engine.repository.CompletionRepository;
import engine.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
public class QuizzesController {

    private QuizRepository quizRepository;
    @Autowired
    private CompletionRepository completionRepository;
    public QuizzesController(
            QuizRepository repository,
            CompletionRepository completionRepository
    ){
        this.quizRepository = repository;
        this.completionRepository = completionRepository;
    }

    @PostMapping(value = "/api/quizzes", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Object> handleCreateQuiz(
            @RequestBody QuizCreateDTO quizCreateDTO
    ) {
        if(quizCreateDTO.getText() == null ||
           quizCreateDTO.getText().equals("") ||
           quizCreateDTO.getTitle() == null ||
           quizCreateDTO.getTitle().equals("") ||
           quizCreateDTO.getOptions() == null ||
           quizCreateDTO.getOptions().length < 2) {
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Quiz quiz = new Quiz();
        quiz.setOwnerEmail(user.getEmail());
        quiz.setOptions(quizCreateDTO.getOptions());
        quiz.setText(quizCreateDTO.getText());
        quiz.setTitle(quizCreateDTO.getTitle());
        quiz.setAnswers(quizCreateDTO.getAnswer());

        QuizCreateResponseDTO quizGetterDTO = new QuizCreateResponseDTO();
        quizGetterDTO.setText(quiz.getText());
        quizGetterDTO.setTitle(quiz.getTitle());
        quizGetterDTO.setOptions(quiz.getOptions());
        quizGetterDTO.setId(quiz.getId());
        quizGetterDTO.setOwnerEmail(user.getEmail());

        this.quizRepository.save(quiz);

        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/quizzes/{id}")
    @ResponseBody
    public ResponseEntity<Object> handleGetQuizById(
            @PathVariable(value="id") String id
    ) {
        Long idL = Long.valueOf(id);
        Optional<Quiz> quiz = this.quizRepository.findById(idL);
        if(quiz.isEmpty()) {
            QuizNotFoundResponseDTO quizNotFoundResponse = new QuizNotFoundResponseDTO("Not found");
            return new ResponseEntity<>(quizNotFoundResponse, HttpStatus.NOT_FOUND);
        }

        Quiz q = quiz.get();

        QuizGetterResponseDTO quizGetterResponseDTO = new QuizGetterResponseDTO();
        quizGetterResponseDTO.setId(q.getId());
        quizGetterResponseDTO.setOptions(q.getOptions());
        quizGetterResponseDTO.setText(q.getText());
        quizGetterResponseDTO.setTitle(q.getTitle());

        return new ResponseEntity<>(q, HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/quizzes/{id}")
    @ResponseBody
    public ResponseEntity<Object> deleteQuizById(
            @PathVariable(value="id") String id
    ) {
        Long idL = Long.valueOf(id);
        Optional<Quiz> quiz = this.quizRepository.findById(idL);
        if(quiz.isEmpty()) {
            QuizNotFoundResponseDTO quizNotFoundResponse = new QuizNotFoundResponseDTO("Not found");
            return new ResponseEntity<>(quizNotFoundResponse, HttpStatus.NOT_FOUND);
        }

        Quiz q = quiz.get();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!q.getOwnerEmail().equals(user.getEmail())) {
            return new ResponseEntity<>("You cannot delete another user's quiz", HttpStatus.FORBIDDEN);
        }

        this.quizRepository.delete(q);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/api/quizzes")
    @ResponseBody
    public ResponseEntity<Object> handleGetAllQuizzes(
            @RequestParam(required = false, defaultValue = "0", name = "page") int page
    ) {
        int pageSize = 10;
        Pageable paging = PageRequest.of(page, pageSize, Sort.by("id").descending());
        return new ResponseEntity<>(this.quizRepository.findAll(paging), HttpStatus.OK);
    }


    @GetMapping("/api/quizzes/completed")
    public Page<Completion> getCompleted(@RequestParam(required = false, defaultValue = "0", name = "page") int page) {
        int pageSize = 10;
        Pageable paging = PageRequest.of(page, pageSize, Sort.by("completedAt").descending());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return this.completionRepository.findAllByUserEmail(user.getEmail(), paging);
    }

    @PostMapping(value = "/api/quizzes/{id}/solve", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Object> handleSolveQuiz(
            @PathVariable(value="id") String id,
            @RequestBody QuizSolveDTO quizSolveDTO
    ) {
        Long idL = Long.valueOf(id);
        Optional<Quiz> quiz = this.quizRepository.findById(idL);
        if(quiz.isEmpty()) {
            QuizNotFoundResponseDTO quizNotFoundResponse = new QuizNotFoundResponseDTO("Not found");
            return new ResponseEntity<>(quizNotFoundResponse, HttpStatus.NOT_FOUND);
        }
        QuizValidateResponseDTO quizValidateResponseDTO = new QuizValidateResponseDTO();
        Quiz q = quiz.get();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(q.getAnswers() != null &&
                quizSolveDTO.getAnswer().length == 0 &&
                q.getAnswers().length == 0) {
            this.completionRepository.save(new Completion(user.getEmail(), idL, new Date()));
            quizValidateResponseDTO.setSuccess(true);
            quizValidateResponseDTO.setFeedback("Congratulations, you're right!");
            return new ResponseEntity<>(quizValidateResponseDTO, HttpStatus.OK);
        }

        if(q.getAnswers() == null && quizSolveDTO.getAnswer().length == 0) {
            this.completionRepository.save(new Completion(user.getEmail(), idL, new Date()));
            quizValidateResponseDTO.setSuccess(true);
            quizValidateResponseDTO.setFeedback("Congratulations, you're right!");
            return new ResponseEntity<>(quizValidateResponseDTO, HttpStatus.OK);
        }

        if(q.getAnswers() == null && quizSolveDTO.getAnswer().length != 0) {
            quizValidateResponseDTO.setSuccess(false);
            quizValidateResponseDTO.setFeedback("Wrong answer! Please, try again.");
            return new ResponseEntity<>(quizValidateResponseDTO, HttpStatus.OK);
        }

        int totalAnswers = q.getAnswers().length;
        if(totalAnswers == 0 && quizSolveDTO.getAnswer().length == 0) {
            this.completionRepository.save(new Completion(user.getEmail(), idL, new Date()));
            quizValidateResponseDTO.setSuccess(true);
            quizValidateResponseDTO.setFeedback("Congratulations, you're right!");
            return new ResponseEntity<>(quizValidateResponseDTO, HttpStatus.OK);
        }
        int[] answers = quizSolveDTO.getAnswer();
        if(totalAnswers != answers.length) {
            quizValidateResponseDTO.setSuccess(false);
            quizValidateResponseDTO.setFeedback("Wrong answer! Please, try again.");
            return new ResponseEntity<>(quizValidateResponseDTO, HttpStatus.OK);
        }

        int allValidAnswers = 0;
        for (int i = 0; i < totalAnswers; i++) {
            for (int o = 0; o < answers.length; o++) {
                int answerSaved = q.getAnswers()[i];
                int answerR = answers[o];
                if(answerSaved == answerR) {
                    allValidAnswers++;
                }
            }
        }

        if(allValidAnswers == totalAnswers) {
            this.completionRepository.save(new Completion(user.getEmail(), idL, new Date()));
            quizValidateResponseDTO.setSuccess(true);
            quizValidateResponseDTO.setFeedback("Congratulations, you're right!");
            return new ResponseEntity<>(quizValidateResponseDTO, HttpStatus.OK);

        }
        quizValidateResponseDTO.setSuccess(false);
        quizValidateResponseDTO.setFeedback("Wrong answer! Please, try again.");
        return new ResponseEntity<>(quizValidateResponseDTO, HttpStatus.OK);
    }
}
