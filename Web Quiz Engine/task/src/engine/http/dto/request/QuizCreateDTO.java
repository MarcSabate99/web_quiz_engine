package engine.http.dto.request;

import org.springframework.lang.NonNull;
public class QuizCreateDTO {
    public String title;
    public String text;
    public String[] options;
    public int[] answer;

    QuizCreateDTO(){}

    public String[] getOptions() {
        return options;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public int[] getAnswer() {
        return answer;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAnswer(int[] answer) {
        this.answer = answer;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }
}
