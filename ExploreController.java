import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExploreController extends TabViewController {

    ExploreController() {
    }

    public List<SimplePicture> getAllUploadedImages(String username) {
        try
        {
            Connection connection = DatabaseConnector.getConnection();
            String query = "";
            boolean isEmpty = username.isBlank();
            if(isEmpty)
            {
                query = "SELECT * FROM uploaded_picture";
            }
            else
            {
                query = "SELECT * FROM uploaded_picture WHERE uploading_user LIKE ?";
            }
            PreparedStatement stmt = connection.prepareStatement(query);
            if(!isEmpty){
                stmt.setString(1, username + "%"); //TODO: could add a wildcard to the other end of the username, but chose not to as that would alter the functionality
            }
            ResultSet rs = stmt.executeQuery();
            List<SimplePicture> images = new ArrayList<>();
            while (rs.next()) {
                String imageId = rs.getString("uploading_user") + "_" + rs.getInt("number");
                String imagePosterName = rs.getString("uploading_user");
                images.add(new SimplePicture(imagePosterName, "img/uploaded/" + imageId + ".png", imageId));
            }
            stmt.close();
            rs.close();
            connection.close();
            return images;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Picture getPictureWithId(String imageId) {
        String[] parts = imageId.split("_");
        String uploadingUser = parts[0];
        int number = Integer.parseInt(parts[1]);
        Picture p = null;
        try{
            Connection connection = DatabaseConnector.getConnection();
            String query = """
            SELECT uploading_user, number, time_uploaded, bio,
                (SELECT COUNT(*) FROM picture_like WHERE uploading_user = ? AND picture_number = ?) AS like_count
            FROM uploaded_picture
            WHERE uploading_user = ? AND number = ?
             """;

             PreparedStatement statement = connection.prepareStatement(query);

             statement.setString(1, uploadingUser);
             statement.setInt(2, number);
             statement.setString(3, uploadingUser);
             statement.setInt(4, number);

             ResultSet rs = statement.executeQuery();
             if(rs.next())
             {
                p = new Picture(imageId, uploadingUser, "img/uploaded/" + imageId + ".png",
                    rs.getString("bio"), rs.getTimestamp("time_uploaded").toString(), rs.getInt("like_count"));
             }

                rs.close();
                statement.close();
                connection.close();
                return p; 
        }catch (SQLException e) {
            e.printStackTrace();

            return null;
        }
    }

    public User doesUsernameExist(String username) {
        try {
            Connection c = DatabaseConnector.getConnection();
            String query = "SELECT * FROM user WHERE ID = ?";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User u = new User(rs.getString("ID"), rs.getString("bio"), "");
                stmt.close();
                rs.close();
                c.close();
                return u;
            }
            stmt.close();
            rs.close();
            c.close();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
