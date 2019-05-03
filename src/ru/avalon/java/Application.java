package ru.avalon.java;

import ru.avalon.java.frames.*;
import javax.swing.JFrame;

/**
 * DEV-J20. Лабораторная работа №3. Оконные приложения:<n>
 * 1. Приложение выбора цвета.<n> ВЫПОЛНЕНО.
 * 2. Калькулятор. В ПРОЦЕССЕ.
 */
public class Application {

    public static void main(String[] args) {
        JFrame frame = new ColorPalette(); // создание окна ColorPalette
        frame.setVisible(true); // делаем это окно видимым
    }

}
