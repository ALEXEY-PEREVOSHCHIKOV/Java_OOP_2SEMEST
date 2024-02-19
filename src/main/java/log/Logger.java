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
        defaultLogSource = new LogWindowSource(100);
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
     * Записывает сообщение об ошибке в лог.
     * @param strMessage Сообщение об ошибке для записи в лог.
     */
    public static void error(String strMessage)
    {
        defaultLogSource.append(LogLevel.Error, strMessage);
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
