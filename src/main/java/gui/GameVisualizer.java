package gui;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Панель для визуализации робота и обработки событий мыши.
 */
public class GameVisualizer extends AGameVisualizer {

    private final IRobotModel robotModel;
    private Point clickPoint;

    /**
     * Конструктор класса GameVisualizer.
     *
     * @param robotModel Модель робота для отображения и слежения за его движением.
     */
    public GameVisualizer(IRobotModel robotModel) {
        super(robotModel);
        this.robotModel = robotModel;
    }

    /**
     * Перерисовывает компонент с учетом текущего состояния робота и точки клика мышью.
     *
     * @param g Графический контекст для отрисовки.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (clickPoint != null) {
            g2d.setColor(Color.BLACK);
            g2d.fillOval(clickPoint.x - 5, clickPoint.y - 5, 10, 10);
        }

        Point robotPosition = robotModel.getRobotPosition();
        int robotCenterX = (int) robotPosition.getX();
        int robotCenterY = (int) robotPosition.getY();
        AffineTransform t = AffineTransform.getRotateInstance(robotModel.getRobotDirection(), robotCenterX, robotCenterY);
        g2d.setTransform(t);
        g2d.setColor(Color.MAGENTA);
        fillOval(g2d, robotCenterX, robotCenterY, 30, 10);
        g2d.setColor(Color.BLACK);
        drawOval(g2d, robotCenterX, robotCenterY, 30, 10);
        g2d.setColor(Color.WHITE);
        fillOval(g2d, robotCenterX + 10, robotCenterY, 5, 5);
        g2d.setColor(Color.BLACK);
        drawOval(g2d, robotCenterX + 10, robotCenterY, 5, 5);
    }

    /**
     * Заполняет овал указанным цветом.
     *
     * @param g      Графический контекст для отрисовки.
     * @param centerX Координата X центра овала.
     * @param centerY Координата Y центра овала.
     * @param diam1   Диаметр овала по горизонтали.
     * @param diam2   Диаметр овала по вертикали.
     */
    protected void fillOval(Graphics2D g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    /**
     * Рисует овал указанным цветом.
     *
     * @param g      Графический контекст для отрисовки.
     * @param centerX Координата X центра овала.
     * @param centerY Координата Y центра овала.
     * @param diam1   Диаметр овала по горизонтали.
     * @param diam2   Диаметр овала по вертикали.
     */
    protected void drawOval(Graphics2D g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    /**
     * Обновляет координаты робота при изменении их моделью.
     *
     * @param x         Координата X робота.
     * @param y         Координата Y робота.
     * @param direction Направление робота.
     */
    @Override
    public void onRobotPositionChanged(double x, double y, double direction) {
        repaint();
    }
}
