package translation;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Class that handles text translation for English and French.
 *
 * @author Kyle Fanene
 */
public class Translation {

    /**
     * Method that takes text and translates to English or French depending on user locale.
     *
     * @param keyText           Text to be translated.
     *
     * @return Returns translated text.
     */
    public static String translate(String keyText) throws MissingResourceException {
        String resourceBundlePath = "translation/Resource";
        ResourceBundle rb = ResourceBundle.getBundle(resourceBundlePath, Locale.getDefault());
        return rb.getString(keyText);
    }
}
