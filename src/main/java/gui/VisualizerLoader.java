package gui;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Класс для загрузки визуализаторов робота из JAR-файлов.
 */
public class VisualizerLoader {

    /**
     * Загружает визуализатор робота из указанного JAR-файла.
     *
     * @param jarFile   Файл JAR, содержащий класс визуализатора.
     * @param className Полное имя класса визуализатора, который необходимо загрузить.
     * @param robotModel Модель робота, которую визуализатор будет использовать.
     * @return Экземпляр класса, наследующего JPanel.
     * @throws Exception Если происходит ошибка при загрузке класса, создании экземпляра или приведении к JPanel.
     */
    public static AGameVisualizer loadVisualizerFromJar(File jarFile, String className, IRobotModel robotModel) throws Exception {
        // Проверяем, что файл существует и является .jar файлом
        if (!jarFile.getName().toLowerCase().endsWith(".jar")) {
            throw new IllegalArgumentException("Неверный файл .jar");
        }

        // Создаем URLClassLoader для загрузки классов из .jar файла
        try (URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{jarFile.toURI().toURL()})) {
            // Загружаем класс визуализатора
            Class<?> visualizerClass = classLoader.loadClass(className);

            // Создаем экземпляр класса визуализатора
            Object visualizerObject = visualizerClass.getDeclaredConstructor(IRobotModel.class).newInstance(robotModel);

            // Приводим объект к типу JPanel
            if (!(visualizerObject instanceof JPanel)) {
                throw new IllegalArgumentException("Класс визуализатора должен наследовать JPanel");
            }

            return (AGameVisualizer) visualizerObject;
        }
    }
}