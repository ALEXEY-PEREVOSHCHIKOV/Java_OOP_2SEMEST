package gui;

import java.awt.*;
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
     * Cеттер для установки текущей локали
     * @param locale
     */
    public static void setLocale(Locale locale){
        resources = ResourceBundle.getBundle("messages", locale);
    }





    /**
     * Рекурсивно обновляет локализацию всех компонентов в указанном контейнере и его подконтейнерах.
     *  Если компонент реализует интерфейс LocalizationInterface, вызывается метод изменения локали.
     * @param locale новая локаль, которая будет установлена.
     * @param component корневой компонент, начиная с которого будет производиться обновление локализации.
     */
    public static void updateFrames(Locale locale, Component component) {
        if (component instanceof LocalizationInterface localizationInterface) {
            localizationInterface.changelocale(locale);
        }
        if (component instanceof Container container) {
            for(Component iComponent : container.getComponents()) {
                updateFrames(locale, iComponent);
            }
        }
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
