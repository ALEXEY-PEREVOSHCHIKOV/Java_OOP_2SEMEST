package gui;

import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Locale;

/**
 * Главное окно приложения, наследующее JFrame и реализующее интерфейс Stateful.
 */
public class MainApplicationFrame extends JFrame implements Stateful, LocalizationInterface {

    /**
     * Рабочая область для внутренних окон
     */
    private final JDesktopPane desktopPane = new JDesktopPane();

    /**
     * Номер версии класса для сохранения состояния
     */
    private static final long serialVersionUID = 1L;

    /**
     * экземпляр окна протокола работы.
     */
    private final LogWindow logWindow;

    /**
     * Основное игровое окно.
     */
    private GameWindow gameWindow;

    /**
     * Окно с координатами робота.
     */
    private RobotCoordinatesWindow robotCoordinatesWindow;

    /**
     * Константа, содержащая идентификатор окна для сохранения состояния протокола работы.
     */
    private final String LOG_WINDOW_ID = "LogWindow";

    /**
     * Константа, содержащая идентификатор окна для сохранения состояния игрового окна.
     */
    private final String GAME_WINDOW_ID = "GameWindow";

    /**
     * Константа, содержащая идентификатор окна для сохранения состояния окна c координатами робота.
     */
    private final String ROBOT_COORDINATES_WINDOW_ID = "RobotCoordinatesWindow";


    /**
     * пункт меню "Выход"
     */
    private JMenuItem exitMenuItem;

    /**
     * пункт меню "Язык"
     */
    private JMenu languageMenu;

    /**
     * пункт меню "Режим отображения"
     */
    private JMenu lookAndFeelMenu;

    /**
     * пункт меню "Настройки"
     */
    private JMenu fileMenu;

    /**
     * Пункт подменю режим отображения "Универсальная схема"
     */
    private JMenuItem crossplatformLookAndFeel;

    /**
     * пункт меню "Тесты"
     */
    private JMenu testMenu;

    /**
     * пункт подменю режим отображения "Системная схема"
     */
    private JMenuItem systemLookAndFeel;

    /**
     * пункт подменю настроек "Сообщение в лог"
     */
    private JMenuItem addLogMessageItem;

    /**
     * Пункт меню для загрузки нового робота.
     */
    private JMenuItem loadRobotMenuItem;

    /**
     * Модель текущего робота.
     */
    private IRobotModel robotModel;

    /**
     * Визуализатор игрового поля, разделённый по mvc
     */
    private final AGameVisualizer gameVisualizer;


    /**
     * Конструктор MainApplicationFrame
     */
    public MainApplicationFrame() {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);
        setContentPane(desktopPane);

        logWindow = createLogWindow();
        addWindow(logWindow);

        this.robotModel = new RobotModel();
        this.gameVisualizer=new GameVisualizer(robotModel);


        gameWindow = createRobotGameWindow(robotModel, gameVisualizer);
        addWindow(gameWindow);

        robotCoordinatesWindow = createRobotLocationWindow(robotModel);
        addWindow(robotCoordinatesWindow);


        addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
        testMenu = new JMenu("Тесты");
        fileMenu = new JMenu("Настройки");
        lookAndFeelMenu = new JMenu("Режим отображения");
        languageMenu = new JMenu("Язык");
        exitMenuItem = new JMenuItem("Выход"); // Инициализация поля exitMenuItem
        crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
        systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);


        loadRobotMenuItem = new JMenuItem("Загрузить нового робота");
        loadRobotMenuItem.addActionListener((event) -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                loadNewRobot(selectedFile, "gui.RobotModel1", "gui.GameVisualizer1");
            }
        });
        fileMenu.add(loadRobotMenuItem);


        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new ConfirmExitWindowListener());

        restoreState();
    }

    /**
     * Создает и возвращает окно журнала.
     *
     * @return Окно журнала.
     */
    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(LocalizationManager.getString("debugStartMessage"));
        return logWindow;
    }


    /**
     * Создает окно с координатами робота.
     *
     * @param robotModel Модель робота, для которой создается окно с координатами.
     * @return Созданное окно с координатами робота.
     */
    private RobotCoordinatesWindow createRobotLocationWindow(IRobotModel robotModel) {
        RobotCoordinatesWindow robotLocationWindow = new RobotCoordinatesWindow(robotModel);
        robotLocationWindow.setLocation(800, 0);
        robotLocationWindow.setSize(200, 100);
        return robotLocationWindow;
    }


    /**
     * Создает новое окно игры для робота с указанной моделью.
     *
     * @param robotModel модель робота, для которого создается окно игры.
     * @return новое окно игры для робота.
     */
    private GameWindow createRobotGameWindow(IRobotModel robotModel, AGameVisualizer gameVisualizer){
        GameWindow robotGameWindow = new GameWindow(robotModel,gameVisualizer);
        robotGameWindow.setSize(400, 400);
        return robotGameWindow;
    }

    /**
     * Добавляет внутреннее окно на рабочую область.
     *
     * @param frame Внутреннее окно для добавления.
     */
    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    /**
     * Генерирует и возвращает меню приложения.
     *
     * @return Меню приложения.
     */
    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        addLookAndFeelMenu(menuBar);
        addTestMenu(menuBar);
        addSettingsMenu(menuBar);
        addLanguageMenu(menuBar);
        return menuBar;
    }


    /**
     * Добавляет в меню приложения пункт "Режим отображения".
     * @param menuBar Меню приложения.
     */
    private void addLookAndFeelMenu(JMenuBar menuBar) {
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription("Управление режимом отображения приложения");
        addSystemLookAndFeelMenuItem(lookAndFeelMenu);
        addCrossPlatformLookAndFeelMenuItem(lookAndFeelMenu);
        menuBar.add(lookAndFeelMenu);
    }


    /**
     * Добавляет в меню отображения пункт "Системная схема".
     * При выборе данного пункта меняет внешний вид приложения на системную схему.
     * @param lookAndFeelMenu Меню отображения.
     */
    private void addSystemLookAndFeelMenuItem(JMenu lookAndFeelMenu) {
        systemLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        lookAndFeelMenu.add(systemLookAndFeel);
    }


    /**
     * Добавляет в меню отображения пункт "Универсальная схема".
     * При выборе данного пункта меняет внешний вид приложения на универсальную схему.
     * @param lookAndFeelMenu Меню отображения.
     */
    private void addCrossPlatformLookAndFeelMenuItem(JMenu lookAndFeelMenu) {
        crossplatformLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        lookAndFeelMenu.add(crossplatformLookAndFeel);
    }


    /**
     * Добавляет в меню приложения пункт "Тесты".
     * @param menuBar Меню приложения.
     */
    private void addTestMenu(JMenuBar menuBar) {
        testMenu.setMnemonic(KeyEvent.VK_T);
        addLogMessageMenuItem(testMenu);
        menuBar.add(testMenu);
    }


    /**
     * Добавляет подпункт "Сообщение в лог" в меню "Тесты".
     * @param testMenu Меню "Тесты".
     */
    private void addLogMessageMenuItem(JMenu testMenu) {
        addLogMessageItem.addActionListener((event) -> {
            Logger.debug(LocalizationManager.getString("debugMessage"));
        });
        testMenu.add(addLogMessageItem);
    }


    /**
     * Устанавливает указанный класс внешнего вида LookAndFeel для приложения.
     * @param className Имя класса внешнего вида LookAndFeel.
     */
    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                 | UnsupportedLookAndFeelException e) {
        }
    }


    /**
     * Добавляет в меню приложения пункт "Настройки" с подпунктом "Выход".
     * @param menuBar Меню приложения.
     */
    private void addSettingsMenu(JMenuBar menuBar) {
        exitMenuItem.setMnemonic(KeyEvent.VK_X);
        exitMenuItem.addActionListener((event) -> {
            WindowEvent closeEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeEvent);
        });
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
    }


    /**
     * Добавляет меню выбора языка к указанной строке меню приложения.
     *
     * @param menuBar строка меню, к которой добавляется меню выбора языка.
     */
    private void addLanguageMenu(JMenuBar menuBar) {

        JMenuItem russianMenuItem = new JMenuItem("Русский");
        russianMenuItem.addActionListener(e -> {
            LocalizationManager.setLocale (new Locale("ru"));
            LocalizationManager.updateFrames(new Locale("ru"), this);
        });

        JMenuItem translitMenuItem = new JMenuItem("Translit");
        translitMenuItem.addActionListener(e -> {
            LocalizationManager.setLocale (new Locale("en"));
            LocalizationManager.updateFrames(new Locale("en"), this);
        });

        languageMenu.add(russianMenuItem);
        languageMenu.add(translitMenuItem);
        menuBar.add(languageMenu);
    }


    /**
     * Обновляет локализованный текст для элементов интерфейса приложения.
     * Использует ресурсы из класса LocalizationManager для получения локализованных строк
     * и устанавливает их для соответствующих элементов интерфейса.
     */
    @Override
    public void changelocale(Locale locale){
        exitMenuItem.setText(LocalizationManager.getString("exitMenuItemText"));
        languageMenu.setText(LocalizationManager.getString("languageMenuText"));
        lookAndFeelMenu.setText(LocalizationManager.getString("lookAndFeelMenuText"));
        fileMenu.setText(LocalizationManager.getString("fileMenuText"));
        crossplatformLookAndFeel.setText(LocalizationManager.getString("crossplatformLookAndFeelText"));
        testMenu.setText(LocalizationManager.getString("testMenuText"));
        systemLookAndFeel.setText(LocalizationManager.getString("systemLookAndFeelText"));
        addLogMessageItem.setText(LocalizationManager.getString("addLogMessageItem"));
        UIManager.put("OptionPane.yesButtonText", LocalizationManager.getString("yesButtonText"));
        UIManager.put("OptionPane.noButtonText", LocalizationManager.getString("noButtonText"));
    }

    
    /**
     * Завершает работу приложения после подтверждения выхода.
     */
    private void exitApplication() {
       int confirmed = JOptionPane.showConfirmDialog(this, LocalizationManager.getString("confirmExitMessage"), LocalizationManager.getString("confirmExitTitle"),
                JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            saveState(); //сохранение состояния окон
            dispose();
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
    }

    /**
     * Внутренний класс, обрабатывающий событие закрытия окна приложения.
     */
    private class ConfirmExitWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            exitApplication();
        }
    }


    /**
     * Сохраняет состояние окон в конфигурационный файл.
     */
    @Override
    public void saveState() {
        // Получаем единственный экземпляр AppConfig
        AppConfig appConfig = AppConfig.getInstance();
        // Проходим по всем окнам на рабочей области
        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            // Проверяем, является ли окно экземпляром класса, реализующего интерфейс Stateful
            if (frame instanceof Stateful) {
                // Определяем уникальный идентификатор окна
                String windowId = frame instanceof LogWindow ? LOG_WINDOW_ID : (frame instanceof RobotCoordinatesWindow ? ROBOT_COORDINATES_WINDOW_ID : GAME_WINDOW_ID);
                // Сохраняем состояние окна с помощью AppConfig
                appConfig.saveWindowState(windowId, new WindowState(frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight(), frame.isIcon()));
            }
        }
        // Сохраняем все данные в конфигурационный файл
        appConfig.saveConfig();
    }


    /**
     * Восстанавливает состояние окон из конфигурационного файла.
     */
    @Override
    public void restoreState() {
        AppConfig appConfig = AppConfig.getInstance();
        // Загружаем конфигурацию из файла
        appConfig.loadConfig();
        // Проходим по всем окнам на рабочей области
        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            // Проверяем, является ли окно экземпляром класса, реализующего интерфейс Stateful
            if (frame instanceof Stateful) {
                // Определяем уникальный идентификатор окна
                String windowId = frame instanceof LogWindow ? LOG_WINDOW_ID : (frame instanceof RobotCoordinatesWindow ? ROBOT_COORDINATES_WINDOW_ID : GAME_WINDOW_ID);
                // Получаем сохраненное состояние окна из AppConfig
                WindowState state = appConfig.getWindowState(windowId);
                // Если состояние найдено, устанавливаем соответствующие размеры, положение и проверяем, было ли окно свернуто
                if (state != null) {
                    frame.setBounds(state.getX(), state.getY(), state.getWidth(), state.getHeight());
                    try {
                        frame.setIcon(state.isIconified());
                    } catch (java.beans.PropertyVetoException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }



    /**
     * Загружает новую модель робота из указанного JAR-файла и заменяет текущую модель в приложении.
     *
     * @param jarFile Файл JAR, содержащий класс новой модели робота.
     * @throws Exception Если происходит ошибка при загрузке нового класса или его инициализации.
     *
     */
    public void loadNewRobot(File jarFile, String modelClass, String visualizerClass) {
        try {
            IRobotModel newRobotModel = RobotLoader.loadRobotFromJar(jarFile, modelClass);
            AGameVisualizer newGameVisualizer =  VisualizerLoader.loadVisualizerFromJar(jarFile, visualizerClass, newRobotModel);

            Field modelField = MainApplicationFrame.class.getDeclaredField("robotModel");
            modelField.setAccessible(true);
            modelField.set(this, newRobotModel);

            Field visualizerField = MainApplicationFrame.class.getDeclaredField("gameVisualizer");
            visualizerField.setAccessible(true);
            visualizerField.set(this, newGameVisualizer);

            saveState();
            desktopPane.remove(gameWindow);
            desktopPane.remove(robotCoordinatesWindow);


            gameWindow = createRobotGameWindow(newRobotModel, newGameVisualizer);
            addWindow(gameWindow);

            robotCoordinatesWindow = createRobotLocationWindow(newRobotModel);
            addWindow(robotCoordinatesWindow);

            restoreState();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Выбранный файл не имеет класса модели робота или визуализатора", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

}

