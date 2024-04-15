package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;

/**
 * Контроллер для управления роботом в игре.
 */
public class GameController extends MouseAdapter {
    private RobotModel robotModel;
    private final Timer timer;


    /**
     * Конструктор класса GameController.
     *
     * @param robotModel      Модель робота, которую будет управлять контроллер
     * @param gameVisualizer  Визуализатор игры, к которому привязывается слушатель событий мыши
     */
    public GameController(RobotModel robotModel, GameVisualizer gameVisualizer) {
        this.robotModel = robotModel;
        gameVisualizer.addMouseListener(this);
        this.timer = new Timer(true);
    }


    /**
     * Обработчик события клика мыши.
     *
     * @param e Объект события мыши, содержащий информацию о клике
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        robotModel.moveRobotTo(x, y);
    }
}