import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UsersDatabase extends Database<User> {
    private final String usersFilePath = "data/users.txt";
    @Override
    public List<User> retrieveAll() {
        List<User> temp = new ArrayList<User>();
        try (BufferedReader reader = new BufferedReader(new FileReader(usersFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(":");
                temp.add(new User(credentials[0], credentials[1], credentials[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (temp);
    }

    @Override
    public boolean save(User u) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(usersFilePath, false))) {
            writer.write(u.toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void loadPath() {}
    
}

