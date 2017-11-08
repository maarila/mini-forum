package tikape.minifoorumi.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tikape.minifoorumi.domain.Message;

public class MessageDao implements Dao<Message, Integer> {

    private Database database;

    public MessageDao(Database database) {
        this.database = database;
    }

    @Override
    public Message findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Message> findAll() throws SQLException {
        List<Message> messages = new ArrayList<>();
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Message");
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Integer id = resultSet.getInt("id");
            Integer thread_id = resultSet.getInt("thread_id");
            String message = resultSet.getString("message");
            Timestamp messageTime = resultSet.getTimestamp("posted");

            messages.add(new Message(id, thread_id, message, messageTime));
        }

        resultSet.close();
        statement.close();
        connection.close();

        return messages;
    }

    public List<Message> findOneList(Integer key) throws SQLException {
        List<Message> messages = new ArrayList<>();
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Message WHERE thread_id = ?");
        stmt.setInt(1, key);
        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next()) {
            Integer id = resultSet.getInt("id");
            Integer thread_id = resultSet.getInt("thread_id");
            String message = resultSet.getString("message");
            Timestamp messageTime = resultSet.getTimestamp("posted");

            messages.add(new Message(id, thread_id, message, messageTime));
        }

        resultSet.close();
        stmt.close();
        conn.close();

        return messages;
    }

    @Override
    public Message saveOrUpdate(Message message) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Message ("
                + "thread_id, message, posted) VALUES (?, ?, ?)");
        stmt.setInt(1, message.getThread_id());
        stmt.setString(2, message.getMessage());
        stmt.setTimestamp(3, message.getTimestamp());
        stmt.executeUpdate();

        stmt.close();
        conn.close();

        return message;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
