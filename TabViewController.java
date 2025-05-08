public class TabViewController {
    private Database<User> userDatabase;
    TabViewController(Database<User> userDatabase)
    {
        this.userDatabase = userDatabase;
    }
    public String getLoggedInUserName()
    {
        User u = userDatabase.retrieveAll().getFirst();
        if(u != null)
        {
            return u.getUsername().trim();
        }
        else return null;
    }

    public User getLoggedInUser()
    {
        User u = userDatabase.retrieveAll().getFirst();
        if(u != null)
        {
            return u;
        }
        else return null;
    }
}
