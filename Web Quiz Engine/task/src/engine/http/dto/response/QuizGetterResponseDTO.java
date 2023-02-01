package engine.http.dto.response;

import engine.entity.Quiz;

import java.util.List;

public class QuizGetterResponseDTO {

    public long id;
    public String title;
    public String text;
    public String[] options;

    public String[] getOptions() {
        return options;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public void setText(String text) {
        this.text = text;
    }


    public void setId(long id) {
        this.id = id;
    }
}
