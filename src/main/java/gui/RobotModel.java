package gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс моделирует поведение движения робота в двухмерном пространстве.
 */
public class RobotModel {

    /**
     * Текущая координата X робота.
     */
    protected double robotPositionX = 100;

    /**
     * Текущая координата Y робота.
     */
    protected double robotPositionY = 100;

    /**
     * Текущее направление робота (в радианах).
     */
    protected double robotDirection = 0;

    /**
     * Угол для вращения робота.
     */
    private double rotationAngle = 0;

    /**
     * Целевая координата X для передвижения робота.
     */
    private int targetPositionX = 150;

    /**
     * Целевая координата Y для передвижения робота.
     */
    private int targetPositionY = 100;

    /**
     * Максимально допустимая скорость робота.
     */
    private final double maxVelocity = 1;

    /**
     * Максимально допустимый угол поворота робота.
     */
    private final double maxAngle = 0.01;

    /**
     * Флаг, определяющий, происходит ли в данный момент вращение робота.
     */
    private boolean rotationFlag = false;

    /**
     * Список слушателей изменения модели робота.
     */
    private List<RobotModelListener> listeners = new ArrayList<>();

    public RobotModel() {
    }

    /**
     * Обновляет модель робота, двигая его к целевой точке.
     */
    public void updateModel() {
        double distance = distance(targetPositionX, targetPositionY, robotPositionX, robotPositionY);
        if (distance < 0.5) {
            return;
        }

        double angleToTarget = angleTo (robotPositionX,robotPositionY, targetPositionX, targetPositionY );
        rotationAngle(angleToTarget);
        moveRobot();
        notifyListeners();
    }


    /**
     * Вычисляет расстояние между двумя точками в двумерном пространстве.
     * @return Расстояние между двумя заданными точками.
     */
    private double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }


    /**
     * Вычисляет угол в радианах к цели от текущего положения.
     * @return Угол к целевой точке в радианах от текущего направления робота.
     */
    private double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        return asNormalizedRadians(Math.atan2(diffY, diffX) - robotDirection);
    }

    /**
     * Устанавливает угол поворота робота в зависимости от угла к целевой точке.
     * @param angleToTarget угол к целевой точке в радианах.
     */
    private void rotationAngle(double angleToTarget) {
        if (Math.abs(angleToTarget) < maxAngle) {
            rotationAngle = angleToTarget;
        } else if (!rotationFlag) {
            setRotationAngle(Math.signum(angleToTarget));
            rotationFlag = true;
        }
    }


    /**
     * Устанавливает угол для поворота робота в зависимости от направления поворота.
     * @param rotationDirection направление поворота: 1 для по часовой стрелке, -1 для против часовой стрелки.
     */
    private void setRotationAngle(double rotationDirection) {
        double radiusOfCurve = (maxVelocity / 2) / Math.sin(maxAngle / 2);
        double angleOffset = rotationDirection * (maxAngle + Math.PI) / 2;
        double targetAngle = robotDirection + angleOffset;

        double xRComponent = robotPositionX + radiusOfCurve * Math.cos(targetAngle);
        double yRComponent = robotPositionY + radiusOfCurve * Math.sin(targetAngle);

        double distanceToTarget = distance(targetPositionX, targetPositionY, xRComponent, yRComponent);
        rotationAngle = (distanceToTarget > radiusOfCurve) ? rotationDirection * maxAngle : -rotationDirection * maxAngle;
    }


    /**
     * Перемещает робота на шаг вперед, обновляет его позицию и направление.
     */
    private void moveRobot() {
        robotPositionX += Math.cos(robotDirection + rotationAngle);
        robotPositionY += Math.sin(robotDirection + rotationAngle);
        robotDirection = asNormalizedRadians(robotDirection + rotationAngle);
    }


    private double asNormalizedRadians(double angle) {
        while (angle <= -Math.PI) {
            angle += 2 * Math.PI;
        }
        while (angle >= Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }


    /**
     * Устанавливает целевую позицию для модели робота и сбрасывает флаг вращения.
     * @param p Новая целевая позиция типа {@link Point}.
     */
    protected void setTargetPosition(Point p) {
        targetPositionX = p.x;
        targetPositionY = p.y;
        rotationFlag = false;
    }


    /**
     * Перемещает робота к заданным координатам.
     * @param x Целевая координата X.
     * @param y Целевая координата Y.
     */
    public void moveRobotTo(int x, int y) {
        setTargetPosition(new Point(x, y));
    }


    /**
     * Добавляет слушателя изменений модели робота.
     * @param listener Слушатель для добавления.
     */
    public void addListener(RobotModelListener listener) {
        listeners.add(listener);
    }


    /**
     * Уведомляет слушателя изменений модели робота.
     */
    private void notifyListeners() {
        for (RobotModelListener listener : listeners) {
            listener.onRobotPositionChanged(robotPositionX, robotPositionY, robotDirection);
        }
    }


    /**
     * Возвращает текущее направление робота.
     * @return Текущее направление робота в радианах.
     */
    public double getRobotDirection() {
        return robotDirection;
    }


    /**
     * Возвращает текущую позицию робота.
     */
    public Point getRobotPosition() {
        return new Point((int) robotPositionX, (int) robotPositionY);
    }
}
