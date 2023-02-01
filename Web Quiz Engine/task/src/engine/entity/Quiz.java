package engine.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@JsonIgnoreProperties(value = { "answers","ownerEmail" })
@Entity
@Table(name = "quiz")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public String title;
    public String text;
    public String[] options;
    public int[] answers;
    private String ownerEmail;

    public void setId(long id) {
        this.id = id;
    }

    public String[] getOptions() {
        return options;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setAnswers(int[] answers) {
        this.answers = answers;
    }

    public int[] getAnswers() {
        return answers;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }
    @Override
    public String toString() {
        return String.format(
                "Quiz[id=%d, title='%s', text='%s']",
                id, title, text);
    }
}
