package gui;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.List;

/**
 * Модель робота, управляющая его движением и предоставляющая информацию о его текущем состоянии.
 */
public class RobotModel implements RobotModelListener{

    /**
     * Текущая координата X робота
     */
    protected double robotPositionX = 100;
    /**
     * Текущая координата Y робота
     */
    protected double robotPositionY = 100;

    /**
     * Текущее направление робота в радианах
     */
    protected double robotDirection = 0;

    /**
     * Координата X целевой точки для движения робота
     */
    private int targetPositionX = 150;
    /**
     * Координата Y целевой точки для движения робота
     */
    private int targetPositionY = 100;

    /**
     * Скорость перемещения робота
     */
    private final double SPEED = 1;

    /**
     * Флаг для установки направления поворота
     */
    private boolean rotationDirectionSet = false;

    /**
     * Таймер для обновления состояния робота
     */
    private Timer timer;


    /**
     * Конструктор класса RobotModel.
     */
    public RobotModel() {
        timer = new Timer("RobotMovementTimer", true);
    }


    /**
     * Обновляет состояние модели робота, перемещая его к целевой точке.
     */
    public void updateModel() {
        double distance = distance(targetPositionX, targetPositionY, robotPositionX, robotPositionY);
        if (distance < 0.5) {
            return;
        }
        double angleToTarget = Math.atan2(targetPositionY - robotPositionY, targetPositionX - robotPositionX);

        robotDirection = angleToTarget;
        moveRobot();
        notifyListeners();
    }


    /**
     * Вычисляет расстояние между двумя точками.
     *
     * @param x1 Координата X первой точки
     * @param y1 Координата Y первой точки
     * @param x2 Координата X второй точки
     * @param y2 Координата Y второй точки
     * @return Расстояние между точками
     */
    private double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }


    /**
     * Перемещает робота к целевой точке.
     */
    private void moveRobot() {
        robotPositionX += SPEED * Math.cos(robotDirection);
        robotPositionY += SPEED * Math.sin(robotDirection);
    }


    /**
     * Устанавливает целевую точку для движения робота.
     *
     * @param p Точка, к которой следует переместить робота
     */
    protected void setTargetPosition(Point p) {
        targetPositionX = p.x;
        targetPositionY = p.y;
        rotationDirectionSet = false;
    }


    /**
     * Перемещает робота к указанным координатам.
     *
     * @param x Координата X новой позиции
     * @param y Координата Y новой позиции
     */
    public void moveRobotTo(int x, int y) {
        setTargetPosition(new Point(x, y));
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer("RobotMovementTimer", true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateModel();
            }
        }, 0, 10);
    }


    /**
     * Метод, вызываемый при изменении позиции робота.
     *
     * @param x         Координата X новой позиции робота
     * @param y         Координата Y новой позиции робота
     * @param direction Направление робота в радианах
     */
    @Override
    public void onRobotPositionChanged(double x, double y, double direction) {
    }

    /**
     * поле для хранения слушателей модели робота
     */
    private List<RobotModelListener> listeners = new ArrayList<>();


    /**
     * Добавляет слушателя модели робота.
     *
     * @param listener Слушатель, который будет уведомлен об изменениях состояния робота
     */
    public void addListener(RobotModelListener listener) {
        listeners.add(listener);
    }


    /**
     * Удаляет слушателя модели робота.
     *
     * @param listener Слушатель, который больше не должен получать уведомления
     */
    public void removeListener(RobotModelListener listener) {
        listeners.remove(listener);
    }


    /**
     * Уведомляет всех слушателей о изменении позиции робота.
     */
    private void notifyListeners() {
        for (RobotModelListener listener : listeners) {
            listener.onRobotPositionChanged(robotPositionX, robotPositionY, robotDirection);
        }
    }


    /**
     * Возвращает текущую позицию робота в виде точки.
     *
     * @return Текущая позиция робота
     */
    public Point getRobotPosition() {
        return new Point((int) robotPositionX, (int) robotPositionY);
    }


    /**
     * Возвращает текущее направление робота.
     *
     * @return Текущее направление робота в радианах
     */
    public double getRobotDirection() {
        return robotDirection;
    }
}