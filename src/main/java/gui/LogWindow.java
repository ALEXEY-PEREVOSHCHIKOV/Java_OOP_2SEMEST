package gui;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

import java.awt.*;
import javax.swing.*;


/**
 * Класс LogWindow представляет окно с протоколом работы приложения.
 */
public class LogWindow extends JInternalFrame implements LogChangeListener, Stateful {


    /**
     * Идентификатор окна для сохранения состояния
     */
    private final String WINDOW_ID = "LogWindow";


    /**
     * источник протокола работы
     */
    private final LogWindowSource logSource;


    /**
     *  компонент TextArea для отображения содержимого протокола.
     */
    private final TextArea logContent;


    /**
     * Конструктор класса LogWindow.
     *
     * @param logSource источник протокола работы
     */
    public LogWindow(LogWindowSource logSource) {
        super("Протокол работы", true, true, true, true);
        this.logSource = logSource;
        this.logSource.registerListener(this);

        logContent = new TextArea("");
        logContent.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }


    /**
     * Обновляет содержимое протокола работы в окне
     */
    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        logContent.setText(content.toString());
        logContent.invalidate();
    }


    /**
     * метод интерфейса LogChangeListener, вызывается при изменении протокола работы
     */
    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
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
        // Получаем сохраненное состояние окна из AppConfig по его идентификатору
        WindowState state = AppConfig.getInstance().getWindowState(WINDOW_ID);
        // Если состояние найдено, восстанавливаем позицию и размеры окна
        if (state != null) {
            setBounds(state.getX(), state.getY(), state.getWidth(), state.getHeight());
            try {
                // Если окно было свернуто, восстанавливаем его свернутое состояние
                setIcon(state.isIconified());
            } catch (java.beans.PropertyVetoException e) {
                // Обработка исключения при установке свернутого состояния
                e.printStackTrace();
            }
        }
    }
}
