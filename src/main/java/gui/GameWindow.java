package gui;

import java.awt.BorderLayout;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;


/**
 * Внутреннее окно для отображения игрового поля.
 */
public class GameWindow extends JInternalFrame {

    /**
     * Визуализатор игрового поля.
     */
    private final GameVisualizer m_visualizer;


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
}
