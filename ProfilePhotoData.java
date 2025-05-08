import java.io.File;

public class ProfilePhotoData
{
    private File file;
    private String username;

    ProfilePhotoData(File f, String username)
    {
        this.file = f;
        this.username = username;
    }
    public File getFile() {
        return file;
    }

    public String getUsername() {
        return username;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePicturePath()
    {
        return file.getAbsolutePath();
    }
}