package gui;

import javax.swing.JOptionPane;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Класс для загрузки моделей робота из JAR-файлов.
 */
public class RobotLoader {

    /**
     * Загружает модель робота из указанного JAR-файла.
     *
     * @param jarFile   Файл JAR, содержащий класс модели робота.
     * @param className Полное имя класса модели робота, который необходимо загрузить.
     * @return Экземпляр класса, реализующего интерфейс IRobotModel.
     * @throws Exception Если происходит ошибка при загрузке класса, создании экземпляра или приведении к интерфейсу IRobotModel.
     */
    public static IRobotModel loadRobotFromJar(File jarFile, String className) throws Exception {
        // Проверяем, что файл существует и является .jar файлом
        if (!jarFile.getName().toLowerCase().endsWith(".jar")) {
            //Сообщение об ошибке на экран
            JOptionPane.showMessageDialog(null, "Выбранный файл не является JAR-файлом", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }

        // Создаем URLClassLoader для загрузки классов из .jar файла
        try (URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{jarFile.toURI().toURL()})) {
            // Загружаем класс робота
            Class<?> robotClass = classLoader.loadClass(className);

            // Создаем экземпляр класса робота
            Object robotObject = robotClass.getDeclaredConstructor().newInstance();

            // Приводим объект к интерфейсу IRobotModel
            if (!(robotObject instanceof IRobotModel)) {
                throw new IllegalArgumentException("Класс робота должен реализовывать интерфейс IRobotModel");
            }

            return (IRobotModel) robotObject;
        }
    }
}
