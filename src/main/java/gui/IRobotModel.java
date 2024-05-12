package gui;

import java.awt.Point;

public interface IRobotModel {
    void updateModel();
    void moveRobotTo(int x, int y);
    void addListener(RobotModelListener listener);
    void notifyListeners();
    double getRobotDirection();
    Point getRobotPosition();
}
