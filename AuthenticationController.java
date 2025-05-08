import java.io.File;
import java.util.List;


public class AuthenticationController {

    private Database<User> credDB;
    private Database<User> userDB;
    private Database<ProfilePhotoData> profilePicDB;
    private IAuthenticationService authService;


    AuthenticationController(Database<User> credDB, Database<User> userDB, Database<ProfilePhotoData> profilePicDB, IAuthenticationService authService) {
        this.credDB = credDB;
        this.userDB = userDB;
        this.profilePicDB = profilePicDB;
        this.authService = authService;
    }

    AuthenticationController() {
        this.credDB = new CredentialsDatabase();
        this.userDB = new UsersDatabase();
        this.profilePicDB = new ProfilePhotoDatabase();
        this.authService = new AuthenticationService(credDB, userDB);
    }

    public VerificationResult verifyCredentials(String username, String password) {
        return authService.loginUser(username, password);
    }

    public void saveProfilePicture(File file, String username) {
        profilePicDB.save(new ProfilePhotoData(file, username));
    }

    public void saveCredentials(String username, String password, String bio) {
        authService.registerUser(username, bio, password);
    }

    public boolean doesUsernameExist(String username) {
        List<User> matches = credDB.getMatching((t) -> userNamePredicate(t, username));
        return !(matches.isEmpty());
    }


    private boolean userNamePredicate(User t, String username) {
        return t.getUsername().equals(username);
    }
}
