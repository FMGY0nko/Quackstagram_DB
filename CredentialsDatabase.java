import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CredentialsDatabase extends Database<User> {
    private final String credentialsFilePath = "data/credentials.txt";
    @Override
        public List<User> retrieveAll() {
            List<User> temp = new ArrayList<User>();
            try (BufferedReader reader = new BufferedReader(new FileReader(credentialsFilePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] credentials = line.split(":");
                    temp.add(new User(credentials[0], credentials[2], credentials[1]));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return (temp);
        }

    @Override
    public boolean save(User u) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(credentialsFilePath, true))) {
            writer.write(u.getUsername() + ":" + u.getPassword() + ":" + u.getBio());
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void loadPath() {
    }

}
