package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;


/**
 * Внутреннее окно для отображения протокола работы.
 * Реализует интерфейс LogChangeListener для обновления содержимого протокола при изменении.
 */
public class LogWindow extends JInternalFrame implements LogChangeListener
{

    /**
     * Источник протокола работы, из которого получается информация для отображения в окне.
     */
    private LogWindowSource m_logSource;


    /**
     * Область текста, предназначенная для отображения содержимого протокола работы.
     */
    private TextArea m_logContent;


    /**
     * Конструктор класса LogWindow.
     * Создает новое внутреннее окно для отображения протокола работы.
     *
     * @param logSource Источник протокола работы.
     */
    public LogWindow(LogWindowSource logSource) 
    {
        super("Протокол работы", true, true, true, true);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }


    /**
     * Обновляет содержимое протокола работы на основе записей из источника протокола.
     */
    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all())
        {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }


    /**
     * Метод обратного вызова, вызываемый при изменении протокола работы.
     * Обновляет содержимое протокола в графическом интерфейсе.
     */

    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
    }


}

