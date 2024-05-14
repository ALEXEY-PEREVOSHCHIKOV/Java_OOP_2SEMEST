package gui;

import javax.swing.*;
import java.awt.BorderLayout;
import java.io.File;
import java.util.Locale;

/**
 * Внутреннее окно для отображения игрового поля.
 */
public class GameWindow extends JInternalFrame implements Stateful, LocalizationInterface {

    /**
     * Идентификатор окна для сохранения состояния
     */
    private final String WINDOW_ID = "GameWindow";

    /**
     * Визуализатор игрового поля, разделённый по mvc
     */
    private final GameVisualizer gameVisualizer;
    private final GameController gameController;
    private final RobotModel robotModel;

    private final DefaultListModel<RobotModel> robotsList; // Добавляем список роботов


    /**
     * Конструктор для создания нового игрового окна.
     */
    public GameWindow(RobotModel robotModel) {
        super(LocalizationManager.getString("gameWindowTitle"), true, true, true, true);
        this.robotModel = robotModel;
        gameVisualizer = new GameVisualizer(robotModel);
        gameController = new GameController(robotModel,gameVisualizer);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(gameVisualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        robotsList = new DefaultListModel<>(); // Инициализируем список роботов
    }

    /**
     * метод интерфейса Stateful, сохраняет состояние окна
     */
    @Override
    public void saveState() {
        AppConfig.getInstance().saveWindowState(WINDOW_ID, new WindowState(getX(), getY(), getWidth(), getHeight(), isIcon()));
    }

    /**
     * метод интерфейса Stateful, восстанавливает состояние окна из сохраненных данных
     */
    @Override
    public void restoreState() {
        WindowState state = AppConfig.getInstance().getWindowState(WINDOW_ID);
        if (state != null) {
            setBounds(state.getX(), state.getY(), state.getWidth(), state.getHeight());
            try {
                setIcon(state.isIconified());
            } catch (java.beans.PropertyVetoException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void changelocale(Locale locale){
        setTitle(LocalizationManager.getString("gameWindowTitle"));
    }
}

