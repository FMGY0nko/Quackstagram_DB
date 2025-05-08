public class VerificationResult {
    private boolean success = false;
    private User newUser = null;

    VerificationResult(boolean success, User newUser) {
        this.newUser = newUser;
        this.success = success;
    }

    public User getNewUser() {
        return newUser;
    }

    public boolean getSuccess()
    {
        return success;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

