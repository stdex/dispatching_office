package utils;

import javafx.scene.paint.Color;

/**
 * Created by rostunov on 12.08.15.
 */
public class FxUtils
{
    public static String toRGBCode( Color color )
    {
        return String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
    }
}