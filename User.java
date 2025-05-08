import java.util.List;
import java.util.ArrayList;

class User {
    private String username;
    private String bio;
    private String password;
    private int postsCount;
    private int followersCount;
    private int followingCount;
    private List<Picture> pictures;

    public User(String username, String bio, String password) {
        this.username = username;
        this.bio = bio;
        this.password = password;
        this.pictures = new ArrayList<>();
    }

    public User(String username) {
        this.username = username;
    }

    public void addPicture(Picture picture) {
        pictures.add(picture);
        postsCount++;
    }

    public String getUsername() {
        return username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getPostsCount() {
        return postsCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public void setPostCount(int postCount) {
        this.postsCount = postCount;
    }

    @Override
    public String toString() {
        return username + ":" + bio + ":" + password;
    }

    public String getPassword()
    {
        return password;
    }
}
