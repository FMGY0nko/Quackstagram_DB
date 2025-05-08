import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRelationshipManager {

    private final String followersFilePath = "data/following.txt";

    public void followUser(String follower, String followed) throws IOException {
        if (!isAlreadyFollowing(follower, followed)) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(followersFilePath, true))) {
                writer.write(follower + ":" + followed);
                writer.newLine();
            }
        }
    }

    public boolean isAlreadyFollowing(String follower, String followed) throws IOException {
        List<String> following = getFollowing(follower);
        return following.contains(followed);
    }

    public List<String> getFollowers(String username) throws IOException {
        List<String> followers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(followersFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                parts[1].split("; ");
                ArrayList<String> temp = new ArrayList<String>();
                for (String string : parts) {
                    temp.add(string);
                }
                if (temp.contains(username)) {
                    followers.add(parts[0]);
                }
            }
        }
        return followers;
    }

    public List<String> getFollowing(String username) throws IOException {
        List<String> following = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(followersFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].equals(username)) {
                    for(String followed : parts[1].split("; "))
                    {
                        following.add(followed);
                    }
                }
            }
        }
        return following;
    }
}

