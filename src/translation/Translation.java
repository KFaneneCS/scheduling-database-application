package translation;

import java.util.Locale;
import java.util.ResourceBundle;

public class Translation {

    public static String translate(String keyText) {
        String resourceBundlePath = "translation/Resource";
        ResourceBundle rb = ResourceBundle.getBundle(resourceBundlePath, Locale.getDefault());
        return rb.getString(keyText);
    }
}
