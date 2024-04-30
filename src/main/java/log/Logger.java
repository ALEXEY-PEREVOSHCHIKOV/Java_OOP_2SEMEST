package log;


/**
 * Утилитарный класс для логирования.
 */
public final class Logger
{

    /**
     * Лог-источник по умолчанию.
     */
    private static final LogWindowSource defaultLogSource;
    static {
        defaultLogSource = new LogWindowSource(4);
    }


    /**
     * Приватный конструктор для предотвращения создания экземпляров класса.
     */
    private Logger() {
    }


    /**
     * Записывает отладочное сообщение в лог.
     * @param strMessage Сообщение для записи в лог.
     */
    public static void debug(String strMessage)
    {
        defaultLogSource.append(LogLevel.Debug, strMessage);
    }



    /**
     * Возвращает лог-источник по умолчанию.
     * @return Лог-источник по умолчанию.
     */
    public static LogWindowSource getDefaultLogSource()
    {
        return defaultLogSource;
    }
}
