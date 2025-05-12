import java.awt.Taskbar.State;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TabViewController {
    public String getLoggedInUserName()
    {
        try
        {
            Connection connection = DatabaseConnector.getConnection();
            String query = "SELECT ID FROM cu";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next())
            {
                String username = rs.getString("ID");
                rs.close();
                stmt.close();
                connection.close();
                return username;
            }
            else
            {
                rs.close();
                stmt.close();
                connection.close();
                return null;
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public User getLoggedInUser()
    {
        String id = getLoggedInUserName();
        try{
            Connection connection = DatabaseConnector.getConnection();
            String query = "SELECT * FROM user WHERE ID = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                User u = new User(rs.getString("ID"), rs.getString("bio"), rs.getString("password"));
                rs.close();
                stmt.close();
                connection.close();
                return u;
            }
            else
            {
                rs.close();
                stmt.close();
                connection.close();
                return null;
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
