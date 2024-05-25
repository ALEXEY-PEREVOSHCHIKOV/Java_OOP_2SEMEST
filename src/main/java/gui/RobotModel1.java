package gui;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.List;

public class RobotModel1 implements RobotModelListener, IRobotModel{

    protected double robotPositionX = 100;

    protected double robotPositionY = 100;

    protected double robotDirection = 0;

    private int targetPositionX = 150;

    private int targetPositionY = 100;

    private final double SPEED = 1;

    private boolean rotationDirectionSet = false;

    private Timer timer;

    public RobotModel1() {
        timer = new Timer("RobotMovementTimer", true);
    }

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

    private double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }


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


    @Override
    public void onRobotPositionChanged(double x, double y, double direction) {
    }

    private List<RobotModelListener> listeners = new ArrayList<>();

    public void addListener(RobotModelListener listener) {
        listeners.add(listener);
    }

    public void notifyListeners() {
        for (RobotModelListener listener : listeners) {
            listener.onRobotPositionChanged(robotPositionX, robotPositionY, robotDirection);
        }
    }

    public Point getRobotPosition() {
        return new Point((int) robotPositionX, (int) robotPositionY);
    }

    public double getRobotDirection() {
        return robotDirection;
    }
}
