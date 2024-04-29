package log;


/**
 * Представляет запись в протоколе, содержащую уровень и сообщение.
 */
public class LogEntry
{
    private LogLevel m_logLevel;
    private String m_strMessage;


    /**
     * Создает новую запись протокола с указанным уровнем и сообщением.
     *
     * @param logLevel Уровень протоколирования.
     * @param strMessage Сообщение протокола.
     */
    public LogEntry(LogLevel logLevel, String strMessage)
    {
        m_strMessage = strMessage;
        m_logLevel = logLevel;
    }


    /**
     * Возвращает сообщение этой записи протокола.
     *
     * @return Сообщение протокола.
     */
    public String getMessage()
    {
        return m_strMessage;
    }

}

