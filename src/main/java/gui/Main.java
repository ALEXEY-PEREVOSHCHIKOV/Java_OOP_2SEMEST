package gui;

import java.awt.Frame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;


/**
 * Главный класс приложения, содержащий точку входа.
 */
public class Main
{

  /**
   * Точка входа в программу.
   * Задает внешний вид приложения и создает основной фрейм приложения.
   *
   * @param args Аргументы командной строки (не используются).
   */
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
      UIManager.put("OptionPane.yesButtonText", "Да");
      UIManager.put("OptionPane.noButtonText", "Нет");
      // другие внешние виды.
      // UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
      // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      // UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }

    SwingUtilities.invokeLater(() -> {
      MainApplicationFrame frame = new MainApplicationFrame();
      frame.pack();
      frame.setVisible(true);
      frame.setExtendedState(Frame.MAXIMIZED_BOTH);
    });
  }
}