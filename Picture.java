import java.util.List;
import java.util.ArrayList;

// Represents a picture on Quackstagram
class Picture extends SimplePicture{
    private String caption;
    private String timeStamp;
    private int likesCount;
    private List<String> comments;

    public Picture(String imageID, String imagePosterName, String imagePath, String caption, String timeStamp, int likesCount) {
        super(imagePosterName, imagePath, imageID);
        this.caption = caption.trim();
        this.likesCount = likesCount;
        this.timeStamp = timeStamp.trim();
        this.comments = new ArrayList<>();
    }

    public void addComment(String comment) {
        comments.add(comment);
    }
    public void like() {
        likesCount++;
    }

    public String getCaption() {
        return caption;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public List<String> getComments() {
        return comments;
    }

    public String getLikeLabel()
    {
        return "Likes: " + this.getLikesCount();
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }


    public String getDataString()
    {
        return String.format("ImageID: %s, Username: %s, Bio: %s, Timestamp: %s, Likes: %s", getImageID(), getImagePosterName(),
        caption, timeStamp, ((Integer)likesCount).toString());
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
