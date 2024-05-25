package gui;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class RobotLoader {

    /**
     * Загружает класс робота из JAR-файла.
     *
     * @param jarFile  JAR-файл, содержащий класс робота
     * @param className Имя класса робота (с пакетом, если он есть)
     * @return Экземпляр загруженного класса робота
     * @throws Exception Если произошла ошибка загрузки класса
     */
    public static IRobotModel loadRobotFromJar(File jarFile, String className) throws Exception {
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{jarFile.toURI().toURL()});
        Class<?> robotClass = Class.forName(className, true, classLoader);
        Object robotInstance = robotClass.getDeclaredConstructor().newInstance();
        if (robotInstance instanceof IRobotModel) {
            return (IRobotModel) robotInstance;
        } else {
            throw new IllegalArgumentException("Класс робота не реализует интерфейс IRobotModel");
        }
    }
}

