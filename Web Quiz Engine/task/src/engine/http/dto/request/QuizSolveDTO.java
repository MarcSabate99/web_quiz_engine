package engine.http.dto.request;

public class QuizSolveDTO {

    public int[] answer;

    QuizSolveDTO(){}

    public int[] getAnswer() {
        return answer;
    }

    public void setAnswer(int[] answer) {
        this.answer = answer;
    }
}
