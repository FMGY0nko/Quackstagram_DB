
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import javax.swing.JLabel;

public class HomeController extends TabViewController {
    HomeController() {
    }

    public List<Picture> createSampleData() {
        List<Picture> toBeShown = new ArrayList<>();
        try {
            Connection connection = DatabaseConnector.getConnection();
            String query = """
                    SELECT u.number, u.uploading_user, u.bio, u.time_uploaded, COUNT(p.liking_user) AS like_count
                    FROM uploaded_picture u LEFT JOIN picture_like p ON
                    u.uploading_user = p.uploading_user AND u.number = p.picture_number
                    WHERE u.uploading_user IN
                                (SELECT user_followed FROM user_follow WHERE user_following = ?)
                    GROUP BY
                        u.uploading_user,
                        u.number

                    -- yeah i know it changes how the app functions, but it is more intuitive
                    ORDER BY
                        u.time_uploaded DESC,
                        like_count DESC
                    """;

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, getLoggedInUserName());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int pictureNumber = resultSet.getInt("number");
                String posterName = resultSet.getString("uploading_user");
                String caption = resultSet.getString("bio");
                String timestamp = resultSet.getTime("time_uploaded").toString();
                int likeCount = resultSet.getInt("like_count");
                String imageID = posterName + "_" + pictureNumber;
                String imagePath = "img/uploaded/" + imageID + ".png";
                toBeShown.add(new Picture(imageID, posterName, imagePath, caption, timestamp, likeCount));
            }
            statement.close();
            resultSet.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toBeShown;
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

    public void handleLikeAction(String imageId, JLabel likesLabel) {
        try {
            Connection connection = DatabaseConnector.getConnection();
            String query0 = "SELECT COALESCE(MAX(number), 0) FROM picture_like WHERE liking_user = ? AND uploading_user = ? AND picture_number = ?";
            PreparedStatement stmt0 = connection.prepareStatement(query0);
            String[] parts = imageId.split("_");
            String uploadingUser = parts[0];
            int number = Integer.parseInt(parts[1]);
            stmt0.setString(1, getLoggedInUserName());
            stmt0.setString(2, uploadingUser);
            stmt0.setInt(3, number);
            ResultSet rs = stmt0.executeQuery();
            int nextID = 0;
            if(rs.next())
            {
                nextID = 1 + rs.getInt(1);
            }
            else{
                return;
            }
            rs.close();
            stmt0.close();

            String query = "INSERT INTO picture_like (liking_user, uploading_user, picture_number, number) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, getLoggedInUserName());
            stmt.setString(2, uploadingUser);
            stmt.setInt(3, number);
            stmt.setInt(4, nextID);

            stmt.executeUpdate();
            likesLabel.setText(String.valueOf(Integer.parseInt(likesLabel.getText().split(":")[1].trim()) + 1));
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
    }
}
}