import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class PicturesDatabase extends UpdatableDatabase<Picture> {

    private Path databasePath;
    PicturesDatabase()
    {
    }
    @Override
    protected List<Picture> retrieveAll() {
        ArrayList<Picture> temp = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(databasePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(", ");
                String imageID = details[0].split(": ")[1].trim();
                String imagePoster = details[1].split(": ")[1].trim();
                String timeStamp = details[3].split(": ")[1].trim();
                String imagePath = ("img/uploaded/" + details[0].split(": ")[1] + ".png").trim();
                String description = details[2].split(": ")[1];
                int likes = Integer.parseInt(details[4].split(": ")[1].trim());

                Picture p = new Picture(imageID, imagePoster, imagePath, description, timeStamp, likes);
                temp.add(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return temp;
    }

    @Override
    public boolean update(Picture m) {
        boolean updated = false;
        StringBuilder newContent = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(databasePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("ImageID: " + m.getImageID())) {
                    line = m.getDataString();
                    updated = true;
                }
                newContent.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (updated) {
            try (BufferedWriter writer = Files.newBufferedWriter(databasePath)) {
                writer.write(newContent.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return updated;
    }
    @Override
    protected void loadPath() {
        databasePath = Paths.get("img", "image_details.txt");
        if (!Files.exists(databasePath)) {
            try {
                Files.createFile(databasePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean save(Picture m) {
            try (BufferedWriter writer = Files.newBufferedWriter(databasePath, StandardOpenOption.APPEND)) {
            writer.write(String.format(m.getDataString()));
            writer.newLine();
            return true;
        }catch(Exception _){
            return false;
        }
    }

}
