package gui;

import java.io.Serializable;


/**
* Класс представляет состояние окна
 */
public class WindowState implements Serializable {
    /**
     * Положение окна по горизонтали
     */
    private int x;

    /**
     * Положение окна по вертикали
     */
    private int y;

    /**
     * Ширина окна
     */
    private int width;

    /**
     * Высота окна
     */
    private int height;

    /**
     *  Флаг, указывающий на свернутое состояние окна
     */
    private boolean isIconified;


    /**
     *   Конструктор класса WindowState
     */
    public WindowState(int x, int y, int width, int height, boolean isIconified) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isIconified = isIconified;
    }


    /**
     *   Возвращает положение окна по горизонтали
     */
    public int getX() {
        return x;
    }


    /**
     *   Возвращает положение окна по вертикали
     */
    public int getY() {
        return y;
    }


    /**
     *   Возвращает ширину окна
     */
    public int getWidth() {
        return width;
    }


    /**
     *    Возвращает высоту окна
     */
    public int getHeight() {
        return height;
    }


    /**
     *   Проверяет, является ли окно свернутым
     */
    public boolean isIconified() {
        return isIconified;
    }
}
