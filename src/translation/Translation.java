package translation;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Translation {

    public static String translate(String keyText) throws MissingResourceException {
        String resourceBundlePath = "translation/Resource";
        ResourceBundle rb = ResourceBundle.getBundle(resourceBundlePath, Locale.getDefault());
        return rb.getString(keyText);
    }
}
