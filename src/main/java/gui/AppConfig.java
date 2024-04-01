package gui;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


/**
 * Класс AppConfig предоставляет сохранение конфигурации окна приложения
 */
public class AppConfig {


    /**
     * Путь к файлу конфигурации окна
     */
    private static final String CONFIG_FILE_PATH = System.getProperty("user.home") + File.separator + "appconfig.ser";


    /**
     * Единственный экземпляр класса AppConfig
     */
    private static AppConfig instance;


    /**
     * Сохраненные состояния окон
     */
    private Map<String, WindowState> windowStates;


    /**
     * Приватный конструктор класса AppConfig
     *      Инициализирует карту состояний окон
     */
    private AppConfig() {
        windowStates = new HashMap<>();
    }


    /**
     * Получение экземпляра AppConfig (реализация шаблона Singleton)
     *  @return единственный экземпляр AppConfig
     */
    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }


    /**
     * Сохраняет состояние окна по идентификатору окна
     *
     *  @param windowId идентификатор окна
     *  @param state    состояние окна для сохранения
     */
    public void saveWindowState(String windowId, WindowState state) {
        windowStates.put(windowId, state);
        saveConfig();
    }


    /**
     * Возвращает сохраненное состояние окна по идентификатору окна
     *
     *   @param windowId идентификатор окна
     *   @return состояние окна
     */
    public WindowState getWindowState(String windowId) {
        return windowStates.get(windowId);
    }


    /**
     * Сохраняет конфигурацию в файл
     */
    public void saveConfig() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(CONFIG_FILE_PATH))) {
            out.writeObject(windowStates);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Загружает конфигурацию из файла
     */
    public void loadConfig() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(CONFIG_FILE_PATH))) {
            windowStates = (Map<String, WindowState>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

