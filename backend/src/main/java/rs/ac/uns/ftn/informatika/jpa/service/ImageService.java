package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImageService {

    private static final String IMAGE_DIRECTORY = "src/main/resources/static/images/";

    public String saveImage(String imageBase64) throws IOException {
        // Remove the prefix if it exists
        if (imageBase64.startsWith("data:image")) {
            imageBase64 = imageBase64.substring(imageBase64.indexOf(",") + 1);
        }

        // Decode the Base64 string
        byte[] imageBytes = Base64Utils.decodeFromString(imageBase64);

        // Create a unique filename
        String uniqueFileName = UUID.randomUUID() + ".jpg";
        File imageFile = new File(IMAGE_DIRECTORY + uniqueFileName);

        // Ensure the directory exists
        File dir = new File(IMAGE_DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Write bytes to file
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            fos.write(imageBytes);
        }

        return "/images/" + uniqueFileName;
    }
}
