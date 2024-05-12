package gui;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class RobotLoader {
    public static IRobotModel loadRobotFromJar(File jarFile, String className) throws Exception {
        // Проверяем, что файл существует и является .jar файлом
        if (!jarFile.exists() || !jarFile.isFile() || !jarFile.getName().toLowerCase().endsWith(".jar")) {
            throw new IllegalArgumentException("Неверный файл .jar");
        }

        // Создаем URLClassLoader для загрузки классов из .jar файла
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{jarFile.toURI().toURL()});

        // Загружаем класс робота
        Class<?> robotClass = classLoader.loadClass(className);

        // Создаем экземпляр класса робота
        Object robotObject = robotClass.newInstance();

        // Приводим объект к интерфейсу Robot
        if (!(robotObject instanceof IRobotModel)) {
            throw new IllegalArgumentException("Класс робота должен реализовывать интерфейс Robot");
        }

        return (IRobotModel) robotObject;
    }
}
