package gui;

import java.awt.BorderLayout;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;


/**
 * Внутреннее окно для отображения игрового поля.
 */
public class GameWindow extends JInternalFrame implements Stateful{

    /**
     * Визуализатор игрового поля.
     */
    private final GameVisualizer m_visualizer;


    /**
     * Идентификатор окна для сохранения состояния
     */
    private final String WINDOW_ID = "GameWindow";


    /**
     * Конструктор для создания нового игрового окна.
     */
    public GameWindow() {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
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
