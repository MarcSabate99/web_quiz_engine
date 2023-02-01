package engine.entity;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "completion")
public class Completion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long completedId;

    private String userEmail;
    private long id;
    private Date completedAt;

    public Completion(String userEmail, long id, Date completedAt) {
        this.userEmail = userEmail;
        this.id = id;
        this.completedAt = completedAt;
    }

    public Completion() {
    }

    public long getId() {
        return id;
    }

    public void setId(long quizId) {
        this.id = quizId;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }
}
