package log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * Представляет источник сообщений для окна протокола.
 */
public class LogWindowSource {

    /**
     * Максимальная длина очереди сообщений в логе.
     */
    private final int m_iQueueLength;


    /**
     * Список сообщений лога.
     */

    private final LinkedList<LogEntry> m_messages;


    /**
     * Список слушателей изменений лога.
     */
    private final List<LogChangeListener> m_listeners;


    /**
     * Создает новый источник сообщений для окна протокола с указанным размером очереди.
     *
     * @param iQueueLength Максимальный размер очереди сообщений.
     */
    public LogWindowSource(int iQueueLength) {
        m_iQueueLength = iQueueLength;
        m_messages = new LinkedList<>();
        m_listeners = new ArrayList<>();
    }


    /**
     * Регистрирует слушателя для получения уведомлений об изменениях в протоколе.
     *
     * @param listener Слушатель изменений в протоколе.
     */
    public void registerListener(LogChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.add(listener);
        }
    }


    /**
     * Удаляет слушателя, чтобы он перестал получать уведомления об изменениях в протоколе.
     *
     * @param listener Слушатель изменений в протоколе.
     */
    public void unregisterListener(LogChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.remove(listener);
        }
    }


    /**
     * Добавляет новую запись в протокол с указанным уровнем и сообщением.
     *
     * @param logLevel    Уровень протоколирования.
     * @param strMessage  Сообщение для записи в протокол.
     */
    public void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        synchronized (m_messages) {
            if (m_messages.size() >= m_iQueueLength) {
                m_messages.removeFirst(); // Удаляем самое старое сообщение
            }
            m_messages.addLast(entry);
        }
        notifyListeners();
    }


    /**
     * Уведомляет всех зарегистрированных слушателей об изменении лога.
     * Создается копия списка слушателей для безопасной итерации, чтобы избежать
     * ошибок синхронизации и предотвратить изменение списка во время итерации.
     */
    private void notifyListeners() {
        List<LogChangeListener> activeListeners;
        synchronized (m_listeners) {
            activeListeners = new ArrayList<>(m_listeners);
        }
        for (LogChangeListener listener : activeListeners) {
            listener.onLogChanged();
        }
    }


    /**
     * Возвращает текущее количество сообщений в протоколе.
     *
     * @return Количество сообщений в протоколе.
     */
    public int size() {
        synchronized (m_messages) {
            return m_messages.size();
        }
    }


    /**
     * Возвращает итератор, который перечисляет сообщения протокола, начиная с указанного индекса
     * и не более указанного количества.
     *
     * @param startFrom Начальный индекс для перечисления сообщений.
     * @param count     Максимальное количество сообщений для перечисления.
     * @return Итератор, перечисляющий сообщения протокола.
     */
    public Iterable<LogEntry> range(int startFrom, int count) {
        synchronized (m_messages) {
            if (startFrom < 0 || startFrom >= m_messages.size()) {
                return Collections.emptyList();
            }
            int indexTo = Math.min(startFrom + count, m_messages.size());
            return new ArrayList<>(m_messages.subList(startFrom, indexTo));
        }
    }


    /**
     * Возвращает итератор, который перечисляет все сообщения протокола.
     *
     * @return Итератор, перечисляющий все сообщения протокола.
     */
    public Iterable<LogEntry> all() {
        synchronized (m_messages) {
            return new ArrayList<>(m_messages);
        }
    }
}

