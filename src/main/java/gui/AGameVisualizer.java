package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Абстрактный класс для визуализаторов игры с роботом.
 */
public abstract class AGameVisualizer extends JPanel implements RobotModelListener {

    protected IRobotModel robotModel;
    protected Point clickPoint;

    /**
     * Конструктор класса AGameVisualizer.
     *
     * @param robotModel Модель робота для отображения и слежения за его движением.
     */
    public AGameVisualizer(IRobotModel robotModel) {
        this.robotModel = robotModel;
        setDoubleBuffered(true);
        setFocusable(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickPoint = e.getPoint();
                handleMouseClick(clickPoint);
            }
        });

        robotModel.addListener(this);
    }


    /**
     * Обрабатывает событие клика мыши и перемещает робота в указанное место.
     *
     * @param point Точка клика.
     */
    protected void handleMouseClick(Point point) {
        int x = point.x;
        int y = point.y;
        robotModel.moveRobotTo(x, y);
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
    protected void fillOval(Graphics2D g, int centerX, int centerY, int diam1, int diam2) {}

    /**
     * Рисует овал указанным цветом.
     *
     * @param g      Графический контекст для отрисовки.
     * @param centerX Координата X центра овала.
     * @param centerY Координата Y центра овала.
     * @param diam1   Диаметр овала по горизонтали.
     * @param diam2   Диаметр овала по вертикали.
     */
    protected void drawOval(Graphics2D g, int centerX, int centerY, int diam1, int diam2) {}

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
