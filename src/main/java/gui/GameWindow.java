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
    private final AGameVisualizer gameVisualizer;

    /**
     * Контроллер для управления игровым процессом.
     */
    private final GameController gameController;

    /**
     * Модель робота, используемая в приложении.
     */
    private final IRobotModel robotModel;

    /**
     * Список моделей роботов.
     */
    private final DefaultListModel<IRobotModel> robotsList;


    /**
     * Конструктор для создания нового игрового окна.
     */
    public GameWindow(IRobotModel robotModel, AGameVisualizer gameVisualizer) {
        super(LocalizationManager.getString("gameWindowTitle"), true, true, true, true);
        this.robotModel = robotModel;
        this.gameVisualizer=gameVisualizer;
        gameController = new GameController(robotModel,gameVisualizer);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(gameVisualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        robotsList = new DefaultListModel<>();
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


    /**
     * Обновляет заголовок окна в соответствии с текущей локалью.
     *
     * @param locale новая локаль для приложения.
     */
    @Override
    public void changelocale(Locale locale){
        setTitle(LocalizationManager.getString("gameWindowTitle"));
    }
}

