package gui;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationManager {
    private static ResourceBundle resources;

    static {
        // Загрузка ресурсов по умолчанию при инициализации класса
        resources = ResourceBundle.getBundle("messages", Locale.getDefault());
    }

    public static void setLocale(Locale locale) {
        resources = ResourceBundle.getBundle("messages", locale);
    }

    public static String getString(String key) {
        return resources.getString(key);
    }
}
