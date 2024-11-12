package rs.ac.uns.ftn.informatika.jpa.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import net.coobird.thumbnailator.Thumbnails;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class ImageCompressionScheduler {


    @Value("${image.upload.dir}")
    private String uploadDir;

    @Scheduled(cron = "0 0 0 * * ?") // Every day at midnight
    public void compressOldImages() {
        File imageDir = new File(uploadDir);
        if (imageDir.exists() && imageDir.isDirectory()) {
            File[] files = imageDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (isOlderThanOneMonth(file)) {
                        try {
                            compressImage(file);
                        } catch (IOException e) {
                            e.printStackTrace(); // Handle the exception appropriately
                        }
                    }
                }
            }
        }
    }

    private boolean isOlderThanOneMonth(File file) {
        LocalDateTime fileDate = new Date(file.lastModified()).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        //LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
        return fileDate.isBefore(oneMonthAgo);
    }

    private void compressImage(File file) throws IOException {
        String compressedFilePath = file.getParent() + "/compressed_" + file.getName();
        Path compressedPath = Paths.get(compressedFilePath);

        BufferedImage bufferedImage = ImageIO.read(file);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        if(width > 800 && height > 600)
        {
            Thumbnails.of(file)
                    .size(800, 600) // Adjust size if needed
                    .outputQuality(0.5) // Set compression quality (0.5 = 50%)
                    .toFile(compressedPath.toFile());
        }




        System.out.println("Compressed image saved as: " + compressedFilePath);
    }
}
