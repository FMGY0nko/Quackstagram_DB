import java.util.List;

class AuthenticationService implements IAuthenticationService {
    private Database<User> credDB;
    private Database<User> userDB;

    AuthenticationService(Database<User> credDB, Database<User> userDB) {
        this.credDB = credDB;
        this.userDB = userDB;
    }

    @Override
    public void registerUser(String username, String bio, String password) {
        if(bio.isEmpty())
        {
            bio = "No bio";
        }

        if (!(username.isEmpty()) && !(password.isEmpty()))
        {
            User u = new User(username, bio, password);
            credDB.save(u);
        }
    }

    @Override
    public VerificationResult loginUser(String username, String password) {
        List<User> matches;
        matches = credDB.getMatching(
                (t) -> credentialsPredicate(t, username, password));

        if (!(matches.isEmpty())) {
            User u = matches.getFirst(); // getting first since only one user is going to match the predicate
            saveUserInformation(u);
            return new VerificationResult(true, u);
        }
        return new VerificationResult(false, null);
    }

    private void saveUserInformation(User user) {
        userDB.save(user);
    }

    private boolean credentialsPredicate(User t, String username, String password) {
        return (t.getUsername().equals(username)
                && t.getPassword().equals(password));
    }
}
