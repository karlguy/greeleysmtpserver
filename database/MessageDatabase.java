package greeleysmtpserver.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author michaelmaitland
 */
public class MessageDatabase extends AbstractDatabase {

    public MessageDatabase(String filename) {
        super(filename);
    }

    @Override
    protected void createTable() {
        // SQL statement for creating a new table
        String query = "CREATE TABLE IF NOT EXISTS conversations (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	mailFrom text NOT NULL,\n"
                + "	rcptTo text NOT NULL,\n"
                + "     subject text,\n"
                + "     inResponseTo integer"
                + "     data text NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void selectAll() {
        String query = "SELECT id, mailFrom, rcptTo, subject, inResponseTo, data FROM conversations";

        try (Connection conn = connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t"
                        + rs.getString("mailFrom") + "\t"
                        + rs.getString("rcptTo")
                        + rs.getString("subject")
                        + rs.getString("inResponseTo")
                        + rs.getString("data"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void add(String mailFrom, String rcptTo, String subject, int inResponseTo, String data) {
        String query = "INSERT INTO users(mailFrom, rcptTo, subject, inResponseTo, data) VALUES(?,?,?,?,?)";

        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, mailFrom);
            pstmt.setString(2, rcptTo);
            pstmt.setString(3, subject);
            pstmt.setInt(4, inResponseTo);
            pstmt.setString(5, data);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}