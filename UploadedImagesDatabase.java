import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class UploadedImagesDatabase extends Database<SimplePicture> {

    Path imageDir;

    @Override
    protected List<SimplePicture> retrieveAll() {
        List<SimplePicture> temp = new ArrayList<>();
        try (Stream<Path> paths = Files.list(imageDir)) {
            paths.forEach(path -> {
                String[] parts = path.toString().split("/");
                String fileName = parts[2];
                temp.add(new SimplePicture(fileName.split("_")[0], path.toString(), fileName.split("\\.")[0]));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    @Override
    public boolean save(SimplePicture m) {
        String originPath = m.getImagePath();
        String fileExtension = getFileExtension(originPath);
        String newFileName = "";
        try {
            String username = m.getImagePosterName();
            String newImageID = username + "_" + getNextImageId(username);
            newFileName = newImageID + "." + fileExtension;
            Path destPath = Paths.get(imageDir.toString(), newFileName);
            Files.copy(Paths.get(originPath), destPath, StandardCopyOption.REPLACE_EXISTING);
            m.setImageID(newImageID);
            m.setImagePath(destPath.toString());
            m.setImagePosterName(username);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getFileExtension(String path) {
        int lastIndexOf = path.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return path.substring(lastIndexOf + 1);
    }

    @Override
    protected void loadPath() {
        imageDir = Paths.get("img", "uploaded");
        if (!Files.exists(imageDir)) {
            try {
                Files.createDirectories(imageDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int getNextImageId(String username) throws IOException {
        List<SimplePicture> sps = getMatching((t) -> {
            return t.getImagePosterName().equals(username);
        });

        return sps.size() + 1;
    }

}


