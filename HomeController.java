
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

public class HomeController extends TabViewController {
    private UserRelationshipManager relationshipManager;
    private UpdatableDatabase<Picture> picDatabase;
    private Database<Notification> notificationsDB;
    HomeController(UpdatableDatabase<Picture> picDatabase, UserRelationshipManager relationshipManager, Database<Notification> notificationsDB, Database<User> usersDB) {
        this.relationshipManager = relationshipManager;
        this.picDatabase = picDatabase;
        this.notificationsDB = notificationsDB;
        
    }

    HomeController()
    {
        this.notificationsDB = new NotificationsDatabase();
        this.picDatabase = new PicturesDatabase();
        this.relationshipManager = new UserRelationshipManager();
    }

    public List<String> getFollowing(String username) {
        try {
            return relationshipManager.getFollowing(username);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Picture> createSampleData() {
        String currentUser = getLoggedInUserName();
        List<String> followedUsers = getFollowing(currentUser);
        List<Picture> toBeShown = new ArrayList<>();

        for (String f : followedUsers) {
            toBeShown.addAll(picDatabase.getMatching(
                    (t) -> picPosterMatchingPredicate(t, f)));
        }
        // TODO: resort the posts however you want, here they are added based on how the
        // db works
        return toBeShown;
    }

    public Picture getPictureWithId(String imageId)
    {
        picDatabase = new PicturesDatabase(); //refreshing database cache
        List<Picture> temp = picDatabase.getMatching((t) -> picIDMatchingPredicate(t, imageId));
        return temp.getFirst();
    }

    public void handleLikeAction(String imageId, JLabel likesLabel) {
        Picture p = getPictureWithId(imageId);
        p.like();
        boolean updated = picDatabase.update(p);
        String currentUser = getLoggedInUserName();
        String imageOwner = p.getImagePosterName();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (updated) {
            likesLabel.setText(p.getLikeLabel());
            notificationsDB.save(new Notification(imageOwner, currentUser, timestamp, imageId));
        }
    }

    private boolean picPosterMatchingPredicate(Picture t, String poster) {
        return t.getImagePosterName().equals(poster);
    }
    private boolean picIDMatchingPredicate(Picture t, String imageId) {
        return t.getImageID().equals(imageId);
    }
}