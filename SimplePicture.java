public class SimplePicture {
    private  String imagePosterName;
    private String imagePath;
    private String imageID;
    private String originPath;

    //the originPath is the path of the image before it is saved to the database
    //the imagePath is the path of the image after it is saved to the database

    SimplePicture(String poster, String path, String imageID)
    {
        imagePath = path.trim();
        imagePosterName = poster.trim();
        this.imageID = imageID.trim();
    }
    public String getImagePath() {
        return imagePath;
    }
    public String getImagePosterName() {
        return imagePosterName;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath.trim();
    }
    public void setImagePosterName(String imagePosterName) {
        this.imagePosterName = imagePosterName.trim();
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID.trim();
    }

    public void setOriginPath(String originPath) {
        this.originPath = originPath.trim();
    }

    public String getOriginPath() {
        return originPath;
    }

}

