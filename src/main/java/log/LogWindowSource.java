package log;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Источник логов, который обеспечивает доступ к записям на основе темпоральных окон.
 */
public class LogWindowSource {

    /**
     * Список слушателей изменений лога.
     */
    private final List<WeakReference<LogChangeListener>> listeners = new CopyOnWriteArrayList<>();

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
    }


    /**
     * Получает все записи лога.
     * @return Итерируемый объект всех записей лога.
     */
    public Iterable <LogEntry> all() {
        return temporalLogWindow;
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
        listeners.add(new WeakReference<>(listener));
    }


    /**
     * Уведомляет всех зарегистрированных слушателей об изменении лога.
     */
    private void notifyListeners() {
        for (Iterator<WeakReference<LogChangeListener>> it = listeners.iterator(); it.hasNext();) {
            WeakReference<LogChangeListener> ref = it.next();
            LogChangeListener listener = ref.get();
            if (listener != null) {
                listener.onLogChanged();
            } else {
                it.remove();
            }
        }
    }
}

