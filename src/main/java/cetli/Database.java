package cetli;

import cetli.framework.Article;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    public static Database database;
    private Connection connection;
    
    public Database(String name) {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:file:" + name);
            createTables();
        } catch(ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void clearTables() throws SQLException {
        connection.createStatement().execute(
              "DROP TABLE ARTICLES; "
            + "DROP TABLE RELATIONSHIPS"
        );
    }
    
    public final void createTables() throws SQLException {
        connection.createStatement().execute(
            "CREATE TABLE IF NOT EXISTS ARTICLES("
          + "ID INT AUTO_INCREMENT, "
          + "TITLE VARCHAR, "
          + "CONTENT VARCHAR, "
          + "URI VARCHAR, "
          + "PRIMARY KEY (ID))"
        );
        
        connection.createStatement().execute(
            "CREATE TABLE IF NOT EXISTS RELATIONSHIPS("
          + "ID INT AUTO_INCREMENT, "
          + "PARENT INT, "
          + "CHILD INT, "
          + "PRIMARY KEY (ID))"
        );
    }
    
    public int newArticle(String title) {
        try {
            String sql = "INSERT INTO ARTICLES (TITLE) VALUES(?)";
            PreparedStatement preparedStatement = 
                    connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, title);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.first();
            return generatedKeys.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public Article getArticle(int id) {
        Article article;
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(
                    "SELECT * FROM ARTICLES WHERE ID=" + String.valueOf(id)
            );
            resultSet.first();
            article = new Article(
                    resultSet.getInt("ID"),
                    resultSet.getString("TITLE"),
                    resultSet.getString("CONTENT"),
                    resultSet.getString("URI")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return article;
    }
    
    public void updateArticle(Article article) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                "UPDATE ARTICLES "
              + "SET TITLE=?, "
              + "CONTENT=?, "
              + "URI=? "
              + "WHERE ID=" + article.getId()
            );
            statement.setString(1, article.getTitle());
            statement.setString(2, article.getContent());
            statement.setString(3, article.getUri());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void removeArticle(int id) {
        try {
            connection.createStatement().executeUpdate(
                "DELETE FROM ARTICLES "
              + "WHERE ID=" + id
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void addRelationship(int parent, int child) {
        try {
            connection.createStatement().executeUpdate(
                "INSERT INTO RELATIONSHIPS (PARENT, CHILD) VALUES "                    
              + "(" + parent + ", " + child + ")"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public List<Integer> search(String searchTerm) {
        List<Integer> ret = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(
                "SELECT ID FROM ARTICLES WHERE LOWER(TITLE) LIKE ?"
            );
            statement.setString(1, "%" + searchTerm.toLowerCase() + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                ret.add(id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }
    
    public Set<Integer> getParents(Integer id) {
        Set<Integer> ret = new HashSet<>();
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(
                "SELECT PARENT FROM RELATIONSHIPS "
              + "WHERE CHILD = " + id
            );
            while (resultSet.next()) {
                ret.add(resultSet.getInt("PARENT"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }
    
    public Set<Integer> getChildren(Integer id) {
        Set<Integer> ret = new HashSet<>();
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(
                "SELECT CHILD FROM RELATIONSHIPS "
              + "WHERE PARENT = " + id
            );
            while (resultSet.next()) {
                ret.add(resultSet.getInt("CHILD"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }
    
    public void removeRelationship(int parent, int child) {
        try {
            connection.createStatement().executeUpdate(
                "DELETE FROM RELATIONSHIPS "
              + "WHERE PARENT = " + parent + " AND CHILD = " + child
            );
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void stall(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
