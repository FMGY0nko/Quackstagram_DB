import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import javax.swing.ImageIcon;

public class ProfileController extends TabViewController {
    private User currentUser;

    ProfileController(User user) {
        super();
        this.currentUser = getUserWithCompleteData(user);
    }

    public User getUserWithCompleteData(User user) {
        User currentUser;
        int followerCount = 0;
        int followingCount = 0;
        int postCount = 0;
        String bio = "Loading...";
        try {
            Connection connection = DatabaseConnector.getConnection();
            String query = """
            SELECT 
                (SELECT COUNT(user_following) FROM user_follow WHERE user_followed=?) AS follower_count,
                (SELECT COUNT(user_followed) FROM user_follow WHERE user_following=?) AS following_count,
                (SELECT COUNT(*) FROM uploaded_picture WHERE uploading_user = ?) AS post_count,
                (SELECT bio FROM user WHERE ID = ?) AS bio
             """;
             PreparedStatement stmt = connection.prepareStatement(query);
             stmt.setString(1, user.getUsername());
             stmt.setString(2, user.getUsername());
             stmt.setString(3, user.getUsername());
             stmt.setString(4, user.getUsername());

             ResultSet resultSet = stmt.executeQuery();

             if(resultSet.next())
             {
                followerCount = resultSet.getInt("follower_count");
                followingCount = resultSet.getInt("following_count");
                postCount = resultSet.getInt("post_count");
                bio = resultSet.getString("bio");
             }

            currentUser = new User(user.getUsername(), bio, "");
            currentUser.setFollowersCount(followerCount);
            currentUser.setFollowingCount(followingCount);
            currentUser.setPostCount(postCount);

            resultSet.close();
            stmt.close();
            connection.close();
            return currentUser;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getCurrentlyViewedUser() {
        return currentUser;
    }

    public boolean isCurrentUserLoggedInUser() {
        return currentUser.getUsername().equals(getLoggedInUserName());
    }

    public boolean isCurrentUserFollowed() {
        try {
            Connection connection = DatabaseConnector.getConnection();
            String query = "SELECT * FROM user_follow WHERE user_following = ? AND user_followed = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, getLoggedInUserName());
            statement.setString(2, getCurrentlyViewedUserUsername());
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void handleFollowAction() {
        try {
            Connection c = DatabaseConnector.getConnection();
            String sql = "INSERT INTO user_follow (user_following, user_followed) VALUES (?,?)";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, getLoggedInUserName());
            stmt.setString(2, getCurrentlyViewedUserUsername());
            stmt.executeUpdate();
            stmt.close();
            c.close();
            currentUser.setFollowersCount(currentUser.getFollowersCount() + 1);
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public List<ImageIcon> getProfileImages(User user) {

        try{
            Connection c = DatabaseConnector.getConnection();
            String query = "SELECT number FROM uploaded_picture WHERE uploading_user = ?";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            ResultSet rs = stmt.executeQuery();
            List<ImageIcon> dbResult = new ArrayList<>();
            while(rs.next())
            {
                //TODO: could add redundancy back to uploaded_image
                int picNumber = rs.getInt("number");
                dbResult.add(new ImageIcon("img/uploaded/" + user.getUsername() + "_" + picNumber + ".png"));
            }
            rs.close();
            stmt.close();
            c.close();
            return dbResult;
        }catch (SQLException e)
        {
            e.printStackTrace();
            return new ArrayList<ImageIcon>();
        }
        
    }

    public String getCurrentlyViewedUserUsername() {
        return getCurrentlyViewedUser().getUsername();
    }

    public ImageIcon getCurrentlyViewedUserProfilePicture() {
        try{
            Connection c = DatabaseConnector.getConnection();
            String query = "SELECT profile_picture FROM user WHERE ID = ?";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, getCurrentlyViewedUserUsername());
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                String path = rs.getString("profile_picture");
                rs.close();
                stmt.close();
                c.close();
                return new ImageIcon(path);
            }
            else
            {
                rs.close();
                stmt.close();
                c.close();
                return null;
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}

