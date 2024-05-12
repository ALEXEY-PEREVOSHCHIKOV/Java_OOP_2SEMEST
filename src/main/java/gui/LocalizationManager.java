package gui;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Управляет локализацией текстовых ресурсов приложения.
 */
public class LocalizationManager {

    /**
     * Инициализирует ресурсы по умолчанию при загрузке класса.
     */
    private static ResourceBundle resources = ResourceBundle.getBundle("messages", Locale.getDefault());


    /**
     * Устанавливает локаль для приложения.
     *
     * @param locale новая локаль.
     */
    public static void setLocale(Locale locale) {
        resources = ResourceBundle.getBundle("messages", locale);
    }

    /**
     * Получает строку из ресурсов по ключу.
     *
     * @param key ключ для получения строки из ресурсов.
     * @return строка из ресурсов с указанным ключом.
     */
    public static String getString(String key) {
        return resources.getString(key);
    }
}
