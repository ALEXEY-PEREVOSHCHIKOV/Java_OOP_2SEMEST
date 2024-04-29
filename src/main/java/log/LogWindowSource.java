package log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Источник логов, который обеспечивает доступ к записям на основе темпоральных окон.
 */
public class LogWindowSource {

    /**
     * Список слушателей изменений лога.
     */
    private final List <LogChangeListener> listeners;

    /**
     * Темпоральная структура для хранения логов.
     */
    private final TemporalLogStructure temporalLogWindow;


    /**
     * Конструктор источника логов с заданной длиной очереди.
     * @param queueLength Длина очереди логов.
     */
    public LogWindowSource(int queueLength) {
        this.temporalLogWindow = new TemporalLogStructure(queueLength);
        this.listeners = new CopyOnWriteArrayList<>();
    }


    /**
     * Получает все записи лога.
     * @return Итерируемый объект всех записей лога.
     */
    public Iterable <LogEntry> all() {
        return temporalLogWindow;
    }


    /**
     * Получает диапазон записей лога, начиная с указанной точки времени.
     * @param startFrom Время старта диапазона в миллисекундах (от текущего момента).
     * @param count Количество записей для извлечения.
     * @return Итерируемый объект записей в заданном диапазоне.
     */
    public Iterable <LogEntry> range(int startFrom, int count) {
        long currentTime = System.currentTimeMillis();
        long windowStart = currentTime - startFrom;
        long windowEnd = windowStart + count;
        return temporalLogWindow.range(windowStart, windowEnd);
    }


    /**
     * Возвращает количество записей в логе.
     * @return Общее число записей логов.
     */
    public int size() {
        return temporalLogWindow.size();
    }


    /**
     * Добавляет новую запись в лог.
     * @param logLevel Уровень логирования.
     * @param strMessage Сообщение лога.
     */
    public void append(LogLevel logLevel, String strMessage) {
        temporalLogWindow.append(logLevel, strMessage);
        notifyListeners();
    }


    /**
     * Регистрирует слушателя для получения уведомлений об изменении лога.
     * @param listener Слушатель, который будет получать уведомления.
     */
    public void registerListener(LogChangeListener listener) {
        listeners.add(listener);
    }


    /**
     * Уведомляет всех зарегистрированных слушателей об изменении лога.
     */
    private void notifyListeners() {
        for (LogChangeListener listener : listeners) {
            listener.onLogChanged();
        }
    }
}

