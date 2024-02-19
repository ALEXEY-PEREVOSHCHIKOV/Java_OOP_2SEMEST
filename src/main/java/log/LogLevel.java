package log;


/**
 * Перечисление для уровней логирования.
 */
public enum LogLevel
{
    Trace(0),
    Debug(1),
    Info(2),
    Warning(3),
    Error(4),
    Fatal(5);
    
    private int m_iLevel;


    /**
     * Конструктор для уровней логирования.
     * @param iLevel Числовое представление уровня логирования.
     */
    private LogLevel(int iLevel)
    {
        m_iLevel = iLevel;
    }


    /**
     * Возвращает числовое представление уровня логирования.
     * @return Числовое представление уровня логирования.
     */
    public int level()
    {
        return m_iLevel;
    }
}

