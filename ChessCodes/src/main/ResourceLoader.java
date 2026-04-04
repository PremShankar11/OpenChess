package main;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

final class ResourceLoader {
    private ResourceLoader() {
    }

    static BufferedImage loadImage(String resourcePath) throws IOException {
        try (InputStream stream = ResourceLoader.class.getResourceAsStream(resourcePath)) {
            if (stream != null) {
                return ImageIO.read(stream);
            }
        }

        File fallbackFile = resolveFallbackFile(resourcePath);
        if (fallbackFile != null) {
            return ImageIO.read(fallbackFile);
        }

        throw new IOException("Missing resource: " + resourcePath);
    }

    static ImageIcon loadScaledIcon(String resourcePath, int width, int height) throws IOException {
        URL resource = ResourceLoader.class.getResource(resourcePath);
        if (resource != null) {
            Image scaledIcon = new ImageIcon(resource)
                    .getImage()
                    .getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledIcon);
        }

        File fallbackFile = resolveFallbackFile(resourcePath);
        if (fallbackFile == null) {
            throw new IOException("Missing resource: " + resourcePath);
        }

        Image scaledIcon = new ImageIcon(fallbackFile.getAbsolutePath())
                .getImage()
                .getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledIcon);
    }

    private static File resolveFallbackFile(String resourcePath) {
        String relativePath = resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;
        String[] candidates = {
                "ChessCodes/src/" + relativePath,
                "src/" + relativePath
        };

        for (String candidate : candidates) {
            File file = new File(candidate);
            if (file.isFile()) {
                return file;
            }
        }

        return null;
    }
}
