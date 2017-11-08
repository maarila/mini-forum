package tikape.minifoorumi.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tikape.minifoorumi.domain.Thread;

public class ThreadDao implements Dao<Thread, Integer> {

    private Database database;

    public ThreadDao(Database database) {
        this.database = database;
    }

    @Override
    public Thread findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Thread "
                + "WHERE id = ?");
        statement.setInt(1, key);
        ResultSet resultSet = statement.executeQuery();

        Thread ketju = new Thread(resultSet.getInt("id"), resultSet.getString("title"));

        resultSet.close();
        statement.close();
        connection.close();

        return ketju;
    }

    @Override
    public List<Thread> findAll() throws SQLException {
        List<Thread> threads = new ArrayList<>();
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Thread "
                + "INNER JOIN Message ON Thread.id = Message.thread_id "
                + "GROUP BY Thread.id "
                + "ORDER BY Message.posted DESC LIMIT 100");
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Integer id = resultSet.getInt("id");
            String title = resultSet.getString("title");
            threads.add(new Thread(id, title));
        }

        resultSet.close();
        statement.close();
        connection.close();

        return threads;
    }

    public List<Integer> findSizes() throws SQLException {
        List<Integer> sizes = new ArrayList<>();
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS maara FROM Thread "
                + "INNER JOIN Message ON Thread.id = Message.thread_id "
                + "GROUP BY Thread.id "
                + "ORDER BY Message.posted DESC LIMIT 100");
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Integer maara = resultSet.getInt("maara");
            sizes.add(maara);
        }

        resultSet.close();
        statement.close();
        connection.close();

        return sizes;
    }

    @Override
    public Thread saveOrUpdate(Thread thread) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Thread ("
                + "title) VALUES (?)");
        stmt.setString(1, thread.getTitle());
        stmt.executeUpdate();

        PreparedStatement retrieveStatement = conn.prepareStatement("SELECT last_insert_rowid() as id");
        ResultSet rs = retrieveStatement.executeQuery();
        
        thread.setId(rs.getInt("id"));

        rs.close();
        stmt.close();
        retrieveStatement.close();
        conn.close();

        return thread;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
