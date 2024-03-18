package log;

/**
 * Интерфейс, определяющий метод для обработки изменений в протоколе.
 */
public interface LogChangeListener
{
    /**
     * Вызывается при изменении протокола.
     */
    public void onLogChanged(); 
}
