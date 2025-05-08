import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class ProfileController extends TabViewController {
    UserRelationshipManager relationshipManager;
    Database<User> credDB;
    Database<Picture> picDatabase;
    private User currentUser;
    Database<SimplePicture> uploadedPicsDB;
    Database<ProfilePhotoData> profilePicDB;

    ProfileController(User user) {
        super(new UsersDatabase());
        relationshipManager = new UserRelationshipManager();
        picDatabase = new PicturesDatabase();
        credDB = new CredentialsDatabase();
        uploadedPicsDB = new UploadedImagesDatabase();
        profilePicDB = new ProfilePhotoDatabase();
        this.currentUser = getUserWithCompleteData(user);
    }

    ProfileController(User user, UserRelationshipManager relationshipManager, Database<User> credDB,Database<User> userDatabase, Database<Picture> picDatabase, Database<SimplePicture> uploadedPicsDB, Database<ProfilePhotoData> profilePicDB) {
        super(userDatabase);
        this.relationshipManager = relationshipManager;
        this.picDatabase = picDatabase;
        this.credDB = credDB;
        this.uploadedPicsDB = uploadedPicsDB;
        this.profilePicDB = profilePicDB;
        this.currentUser = getUserWithCompleteData(user);
    }

    public User getUserWithCompleteData(User user) {
        User currentUser = credDB.getMatching((t) -> userNameMatchingPredicate(t, user)).getFirst();
        int followerCount;
        try {
            followerCount = relationshipManager.getFollowers(currentUser.getUsername()).size();
            int followingCount = relationshipManager.getFollowing(currentUser.getUsername()).size();
            currentUser.setFollowersCount(followerCount);
            currentUser.setFollowingCount(followingCount);
            int postCount = picDatabase.getMatching(
                    (t) -> picPosterMatchingPredicate(t, currentUser.getUsername())).size();
            currentUser.setPostCount(postCount);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return currentUser;
    }

    private boolean picPosterMatchingPredicate(Picture t, String posterName) {
        return t.getImagePosterName().equals(posterName);
    }

    private boolean userNameMatchingPredicate(User t, User other) {
        return t.getUsername().equals(other.getUsername());
    }

    public User getCurrentlyViewedUser() {
        return currentUser;
    }

    public boolean isCurrentUserLoggedInUser() {
        return currentUser.getUsername().equals(getLoggedInUserName());
    }

    public boolean isCurrentUserFollowed() {
        try {
            return relationshipManager.isAlreadyFollowing(getLoggedInUserName(), currentUser.getUsername());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void handleFollowAction() {
        try {
            relationshipManager.followUser(getLoggedInUserName(), currentUser.getUsername());
            currentUser.setFollowersCount(relationshipManager.getFollowers(currentUser.getUsername())
                    .size());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<ImageIcon> getProfileImages(User user) {
        List<SimplePicture> dbResult = uploadedPicsDB.getMatching((t) -> 
        {
            return t.getImagePosterName().equals(user.getUsername());
        });

        List<ImageIcon> temp = new ArrayList<>();
        for(SimplePicture sp : dbResult)
        {
            temp.add(new ImageIcon(sp.getImagePath()));
        }
        return temp;
    }

    public String getCurrentlyViewedUserUsername() {
        return getCurrentlyViewedUser().getUsername();
    }

    public ImageIcon getCurrentlyViewedUserProfilePicture() {
        List<ProfilePhotoData> dbResult = profilePicDB.getMatching((t) -> 
        {
            return t.getUsername().equals(getCurrentlyViewedUserUsername());
        });
        if(dbResult.isEmpty())
        {
            return new ImageIcon("img/default_profile.png");
        }
        else{
            ProfilePhotoData ppd = dbResult.getFirst();
            return new ImageIcon(ppd.getProfilePicturePath());
        }
    }
}

