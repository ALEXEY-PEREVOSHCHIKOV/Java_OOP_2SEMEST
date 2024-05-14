package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

/**
 * Внутреннее окно для отображения координат робота.
 */
public class RobotCoordinatesWindow extends JInternalFrame implements RobotModelListener,Stateful,LocalizationInterface {

    /**
     * Идентификатор окна для сохранения состояния
     */
    private final String WINDOW_ID = "RobotCoordinatesWindow";

    /**
     * Модель робота, предоставляющая информацию о его координатах
     */
    private RobotModel robotModel;


    /**
     * Метка для отображения текущих координат робота
     */
    private JLabel coordinatesLabel;


    /**
     * Конструктор класса RobotCoordinatesWindow.
     *
     * @param robotModel Модель робота, для которой будет отображаться информация о координатах
     */
    public RobotCoordinatesWindow(RobotModel robotModel) {
        super(LocalizationManager.getString("robotCoordinatesWindowTitle"), true, true, true, true);
        this.robotModel = robotModel;

        setSize(300, 200);
        setLocation(100, 100);

        coordinatesLabel = new JLabel();
        updateCoordinatesLabel();
        add(coordinatesLabel);

        // Добавляем слушателя изменений в модели робота
        robotModel.addListener(this);
    }


    /**
     * Обновляет метку с текущими координатами робота.
     */
    private void updateCoordinatesLabel() {
        Point robotPosition = robotModel.getRobotPosition();
        coordinatesLabel.setText(LocalizationManager.getString("robotCoordinatesMessage")+": x=" + (int)robotPosition.getX() + ", y=" + (int)robotPosition.getY());
    }


    /**
     * Метод, вызываемый при изменении позиции робота.
     *
     * @param x         Координата X новой позиции робота
     * @param y         Координата Y новой позиции робота
     * @param direction Направление робота в радианах (не используется в данном классе)
     */
    @Override
    public void onRobotPositionChanged(double x, double y, double direction) {
        updateCoordinatesLabel();
    }


    /**
     * метод интерфейса Stateful, сохраняет состояние окна
     */
    @Override
    public void saveState() {
        AppConfig.getInstance().saveWindowState(WINDOW_ID, new WindowState(getX(), getY(), getWidth(), getHeight(), isIcon()));
    }

    /**
     * метод интерфейса Stateful, восстанавливает состояние окна из сохраненных данных
     */
    @Override
    public void restoreState() {
        WindowState state = AppConfig.getInstance().getWindowState(WINDOW_ID);
        if (state != null) {
            setBounds(state.getX(), state.getY(), state.getWidth(), state.getHeight());
            try {
                setIcon(state.isIconified());
            } catch (java.beans.PropertyVetoException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void changelocale(Locale locale){
        setTitle(LocalizationManager.getString("robotCoordinatesWindowTitle"));
    }
}
