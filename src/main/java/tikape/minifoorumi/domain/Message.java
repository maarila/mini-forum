
package tikape.minifoorumi.domain;

import java.sql.Timestamp;

public class Message {
    
    Integer id;
    Integer thread_id;
    String message;
    Timestamp timestamp;
    
    public Message(Integer id, Integer thread_id, String message, Timestamp timestamp) {
        this.id = id;
        this.thread_id = thread_id;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getThread_id() {
        return thread_id;
    }

    public void setThread_id(Integer thread_id) {
        this.thread_id = thread_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
