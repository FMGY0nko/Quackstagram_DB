import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

public class ProfilePhotoDatabase extends Database<ProfilePhotoData> {
    private final String profilePhotoStoragePath = "img/storage/profile/";  

    @Override
    protected List<ProfilePhotoData> retrieveAll() {
        List<ProfilePhotoData> profilePhotos = new ArrayList<>();
        try (Stream<Path> paths = Files.list(Paths.get(profilePhotoStoragePath))) {
            paths.forEach(path -> {
                File file = path.toFile();
                String fileName = file.getName();
                if (fileName.endsWith(".png")) {
                    String username = fileName.substring(0, fileName.lastIndexOf("."));
                    System.out.println(username);
                    profilePhotos.add(new ProfilePhotoData(file, username));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return profilePhotos;
    }

    @Override
    public boolean save(ProfilePhotoData d) {
            try {
        BufferedImage image = ImageIO.read(d.getFile());
        File outputFile = new File(profilePhotoStoragePath + d.getUsername() + ".png");
        ImageIO.write(image, "png", outputFile);
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

