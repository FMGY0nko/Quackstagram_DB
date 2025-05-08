import java.util.List;

public class ExploreController extends TabViewController {
    private Database<SimplePicture> uploadedImageDataBase;
    private Database<Picture> detailedImagDatabase;
    private Database<User> credDB;
    ExploreController(Database<User> credDb, Database<SimplePicture> uploadedImageDb, Database<Picture> detailedImageDb, Database<User> usersDb) {   
        super(usersDb);
        uploadedImageDataBase = uploadedImageDb; 
        detailedImagDatabase = detailedImageDb;
        credDB = credDb;
    }

    ExploreController()
    {
        super(new UsersDatabase());
        uploadedImageDataBase = new UploadedImagesDatabase();
        detailedImagDatabase = new PicturesDatabase();
        credDB = new CredentialsDatabase();
    }

    public List<SimplePicture> getAllUploadedImages(String username) {
        List<SimplePicture> images = uploadedImageDataBase.getMatching(username.isBlank() ?
                                                                         (_) -> true // return all images if the search field is empty
                                                                         : (t) -> isUsernamePartiallyMatching( username, t.getImagePosterName()));
        return images;
    }

    public Picture getPictureWithId(String imageId)
    {
        List<Picture> temp = detailedImagDatabase.getMatching((t) -> picIDMatchingPredicate(t, imageId));
        return temp.getFirst();
    }

    private boolean picIDMatchingPredicate(Picture t, String imageId) {
        return t.getImageID().equals(imageId);
    }

    private boolean isUsernamePartiallyMatching(String searchFieldText, String other)
    {
        searchFieldText = searchFieldText.toLowerCase();
        other = other.toLowerCase();
        return other.length() >= searchFieldText.length() && other.substring(0, searchFieldText.length()).equals(searchFieldText);
    }

    public User doesUsernameExist(String username)
    {
        List<User> users = credDB.getMatching((t) -> userNameMatchingPredicate(t, username));
        if(users.isEmpty())
        {
            return null;
        }
        return users.getFirst();
    }

    private boolean userNameMatchingPredicate(User t, String other) {
        return t.getUsername().toLowerCase().equals(other.toLowerCase());
    }
}
