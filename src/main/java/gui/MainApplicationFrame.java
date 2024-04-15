package gui;

import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Главное окно приложения, наследующее JFrame и реализующее интерфейс Stateful.
 */
public class MainApplicationFrame extends JFrame implements Stateful {

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
     * экземпляр главного окна приложения.
     */
    private final GameWindow gameWindow;

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
     * Конструктор MainApplicationFrame
     */
    public MainApplicationFrame() {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);
        setContentPane(desktopPane);

        logWindow = createLogWindow();
        addWindow(logWindow);


        RobotModel robotModel = new RobotModel();


        gameWindow = new GameWindow(robotModel);
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);


        RobotCoordinatesWindow robotCoordinatesWindow = createRobotLocationWindow(robotModel);
        addWindow(robotCoordinatesWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new ConfirmExitWindowListener());

        restoreState(); // загрузка состояния окон
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
        Logger.debug("Протокол работает");
        return logWindow;
    }


    private RobotCoordinatesWindow createRobotLocationWindow(RobotModel robotModel) {
        RobotCoordinatesWindow robotLocationWindow = new RobotCoordinatesWindow(robotModel);
        robotLocationWindow.setLocation(800, 0);
        robotLocationWindow.setSize(200, 100);
        return robotLocationWindow;
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
        return menuBar;
    }


    /**
     * Добавляет в меню приложения пункт "Режим отображения".
     * @param menuBar Меню приложения.
     */
    private void addLookAndFeelMenu(JMenuBar menuBar) {
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
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
        JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
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
        JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
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
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription("Тестовые команды");
        addLogMessageMenuItem(testMenu);
        menuBar.add(testMenu);
    }


    /**
     * Добавляет подпункт "Сообщение в лог" в меню "Тесты".
     * @param testMenu Меню "Тесты".
     */
    private void addLogMessageMenuItem(JMenu testMenu) {
        JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> {
            Logger.debug("Новая строка");
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
        JMenu fileMenu = new JMenu("Настройки");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem exitMenuItem = new JMenuItem("Выход");
        exitMenuItem.setMnemonic(KeyEvent.VK_X);
        exitMenuItem.addActionListener((event) -> {
            WindowEvent closeEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeEvent);
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);
    }


    /**
     * Завершает работу приложения после подтверждения выхода.
     */
    private void exitApplication() {
        int confirmed = JOptionPane.showConfirmDialog(this,
                "Вы действительно хотите выйти из приложения?", "Подтверждение выхода",
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
}

