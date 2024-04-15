package gui;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Внутреннее окно для отображения игрового поля.
 */
public class GameWindow extends JInternalFrame implements Stateful {

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

    /**
     * Конструктор для создания нового игрового окна.
     */
    public GameWindow(RobotModel robotModel) {
        super("Игровое поле", true, true, true, true);
        this.robotModel = robotModel;
        gameVisualizer = new GameVisualizer(robotModel);
        gameController = new GameController(robotModel,gameVisualizer);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(gameVisualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
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
}
