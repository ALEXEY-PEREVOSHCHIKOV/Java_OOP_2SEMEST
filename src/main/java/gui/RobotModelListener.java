package gui;

/**
 * Интерфейс наблюдателя для обновления представления (визуализатора) при изменениях в модели робота
 */
public interface RobotModelListener {
    void onRobotPositionChanged(double x, double y, double direction);
}
