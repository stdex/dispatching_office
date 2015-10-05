package utils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by rostunov on 30.08.15.
 */
public class ImgUtils {

    public static BufferedImage resizeImage(BufferedImage originalImage, int screenWidth, int screenHeight) {

        int w = originalImage.getWidth();
        int h = originalImage.getHeight();
        BufferedImage dimg = new BufferedImage(screenWidth, screenHeight, originalImage.getType());
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(originalImage, 0, 0, screenWidth, screenHeight, 0, 0, w, h, null);
        g.dispose();

        return dimg;
    }

}
