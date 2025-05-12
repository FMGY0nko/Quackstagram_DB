import java.util.List;
import java.sql.*;
class AuthenticationService implements IAuthenticationService {

    @Override
    public void registerUser(String username, String bio, String password) {
        try {
            Connection connection = DatabaseConnector.getConnection();
            String query = bio.isBlank() ? "INSERT INTO user (ID, password) VALUES (?, ?)" : "INSERT INTO user (ID, bio, password) VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            if(!bio.isBlank()){            
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, bio);
            preparedStatement.setString(3, password);}
            else{
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
            }
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public VerificationResult loginUser(String username, String password) {
        try {
            Connection connection = DatabaseConnector.getConnection();
            String query = "SELECT * FROM user WHERE ID = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User u = new User(resultSet.getString("ID"), resultSet.getString("bio"), resultSet.getString("password"));
                saveUserInformation(u);
                resultSet.close();
                preparedStatement.close();
                connection.close();
                return new VerificationResult(true, u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.err.println("Invalid username or password");
        return new VerificationResult(false, null);
    }

    private void saveUserInformation(User user) {
        try{
            Connection connection = DatabaseConnector.getConnection();
            String clearingUpdate = "DELETE FROM cu"; // ensure only one user is logged in
            Statement clear = connection.createStatement();
            clear.executeUpdate(clearingUpdate);
            String query = "INSERT INTO cu (ID) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
