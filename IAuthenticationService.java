public interface IAuthenticationService {
    void registerUser(String username, String bio, String password);
    VerificationResult loginUser(String username, String password);
}

