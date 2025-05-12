import java.io.File;
import java.sql.*;

public class AuthenticationController {

    private IAuthenticationService authService;

        AuthenticationController(IAuthenticationService authService) {
        this.authService = authService;
    }
    AuthenticationController() {
        this.authService = new AuthenticationService();
    }

    public VerificationResult verifyCredentials(String username, String password) {
        return authService.loginUser(username, password);
    }

    public void saveProfilePicture(File file, String username) {
        try{
            Connection connection = DatabaseConnector.getConnection();
            String query = "UPDATE user SET profile_picture = ? WHERE ID = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, file.getAbsolutePath());
            stmt.setString(2, username);
            stmt.executeUpdate();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveCredentials(String username, String password, String bio) {
        authService.registerUser(username, bio, password);
    }

    public boolean doesUsernameExist(String username) {
        try{
            Connection c = DatabaseConnector.getConnection();
            String query = "SELECT * FROM user WHERE ID = ?";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, username); 
            ResultSet rs = stmt.executeQuery();

            if(rs.next())
            {
                stmt.close();
                rs.close();
                c.close();
                return true;
            }
            stmt.close();
            rs.close();
            c.close();
            return false;
        }catch (SQLException e) {
            e.printStackTrace();
            return true; // assume it exists if theres an error
        }
    }
}
