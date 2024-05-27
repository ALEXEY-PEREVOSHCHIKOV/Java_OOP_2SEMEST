package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Контроллер для управления роботом в игре.
 */
public class GameController extends MouseAdapter {
    private IRobotModel robotModel;
    private Timer timer;

    /**
     * Конструктор класса GameController.
     *
     * @param robotModel      Модель робота, которую будет управлять контроллер
     * @param gameVisualizer  Визуализатор игры, к которому привязывается слушатель событий мыши
     */
    public GameController(IRobotModel robotModel, GameVisualizer gameVisualizer) {
        this.robotModel = robotModel;
        gameVisualizer.addMouseListener(this);
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
        startTimer();
    }

    /**
     * Запускает таймер для обновления модели робота.
     */
    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer("RobotMovementTimer", true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                robotModel.updateModel();
            }
        }, 0, 10);
    }
}
