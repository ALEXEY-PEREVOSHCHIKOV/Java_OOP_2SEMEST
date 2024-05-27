package gui;

import java.util.Locale;


/**
 * Интерфейс для реализации локализации в приложении.
 */
public interface LocalizationInterface {

  /**
   * Изменяет текущую локаль приложения на указанную.
   * @param locale Новая локаль для установки.
   */
  void changelocale(Locale locale);
}
