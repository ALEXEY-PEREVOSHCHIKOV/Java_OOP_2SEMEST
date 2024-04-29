package log;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;


/**
 * Структура данных для хранения логов во временных окнах, автоматически удаляет устаревшие логи.
 */
public class TemporalLogStructure implements Iterable <LogEntry> {

    /**
     * Длительность временного окна в миллисекундах, в течение которой логи остаются в структуре.
     */
    private final long windowDurationMillis;

    /**
     * Карта временных окон, ключ - время начала окна, значение - список записей логов {@link LogEntry}.
     */
    private final ConcurrentSkipListMap<Long, List <LogEntry>> temporalWindows;


    /**
     * Конструктор для создания структуры данных для логов с заданной длительностью временных окон.
     *
     * @param windowDurationMillis Длительность временного окна в миллисекундах.
     */
    public TemporalLogStructure(long windowDurationMillis) {
        this.windowDurationMillis = windowDurationMillis;
        this.temporalWindows = new ConcurrentSkipListMap<>();
    }


    /**
     * Добавляет запись лога {@link LogEntry} в структуру.
     *
     * @param logLevel   Уровень логирования.
     * @param strMessage Сообщение лога.
     */
    public void append(LogLevel logLevel, String strMessage) {
        long currentTime = System.currentTimeMillis();
        long windowStart = currentTime - windowDurationMillis;
        LogEntry logEntry = new LogEntry(logLevel, strMessage);
        List  <LogEntry> currentWindow = temporalWindows.computeIfAbsent(windowStart, k -> new ArrayList<>());
        currentWindow.add(logEntry);
        cleanUpExpiredWindows(currentTime);
    }


    /**
     * Удаляет устаревшие временные окна с записями логов.
     *
     * @param currentTime Текущее время в миллисекундах для сравнения с длительностью окна.
     */
    private void cleanUpExpiredWindows(long currentTime) {
        long oldestValidWindow = currentTime - windowDurationMillis;
        temporalWindows.headMap(oldestValidWindow, true).clear();
    }


    /**
     * Возвращает итерируемую часть записей логов в диапазоне времени.
     *
     * @param startTime Время начала диапазона в миллисекундах.
     * @param endTime   Время окончания диапазона в миллисекундах.
     * @return Итерируемый объект записей логов {@link LogEntry} в заданном диапазоне времени.
     */
    public Iterable <LogEntry> range(long startTime, long endTime) {
        List <LogEntry> result = new ArrayList<>();
        SortedMap<Long, List  <LogEntry>> subMap = temporalWindows.subMap(startTime, true, endTime, true);
        for (List  <LogEntry> window : subMap.values()) {
            result.addAll(window);
        }
        return result;
    }


    /**
     * Возвращает общее количество записей логов во всех временных окнах.
     *
     * @return Общее число записей логов.
     */
    public int size() {
        int totalSize = 0;
        for (List <LogEntry> window : temporalWindows.values()) {
            totalSize += window.size();
        }
        return totalSize;
    }


    /**
     * Возвращает итератор по записям логов, агрегирующий все записи из всех временных окон.
     *
     * @return Итератор по записям логов {@link LogEntry}.
     */
    @Override
    public Iterator <LogEntry> iterator() {
        return new TemporalLogIterator();
    }


    /**
     * Вложенный класс, реализующий итератор для всей структуры логов.
     */
    private class TemporalLogIterator implements Iterator <LogEntry> {

        /**
         * Итератор для обхода списков логов в каждом временном окне.
         */
        private final Iterator<List <LogEntry>> windowIterator;

        /**
         * Итератор для обхода записей логов в текущем временном окне.
         */
        private Iterator <LogEntry> entryIterator;


        /**
         * Конструктор итератора. Инициализирует итераторы для обхода временных окон и записей логов.
         */
        public TemporalLogIterator() {
            windowIterator = temporalWindows.values().iterator();
            if (windowIterator.hasNext()) {
                entryIterator = windowIterator.next().iterator();
            } else {
                entryIterator = Collections.emptyIterator();
            }
        }


        /**
         * Проверяет наличие следующей записи лога.
         *
         * @return true, если есть следующая запись лога, false в противном случае.
         */
        @Override
        public boolean hasNext() {
            if (entryIterator.hasNext()) {
                return true;
            } else {
                while (windowIterator.hasNext()) {
                    entryIterator = windowIterator.next().iterator();
                    if (entryIterator.hasNext()) {
                        return true;
                    }
                }
                return false;
            }
        }


        /**
         * Возвращает следующую запись лога в обходе.
         *
         * @return Следующую запись лога.
         * @throws NoSuchElementException если больше нет элементов для возврата.
         */
        @Override
        public LogEntry next() {
            if (hasNext()) {
                return entryIterator.next();
            } else {
                throw new NoSuchElementException("No more elements in the log");
            }
        }
    }
}