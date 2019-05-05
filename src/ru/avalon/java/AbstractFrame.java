package ru.avalon.java;

import java.awt.event.*;
import java.awt.event.WindowAdapter;
import javax.swing.*;

/**
 * Абстрактная модель окна
 */
public abstract class AbstractFrame extends JFrame {

    // адаптер позволяет отслеживать события возникающие на уровне окна и связанные с жизненным циклом этого окна
    // обработка событий - переопределение методов
    private class EventListener extends WindowAdapter {

        @Override
        public void windowOpened(WindowEvent e) { // если окно открыто, то выполняем onCreate()
            onCreate();
        }
    }

    public AbstractFrame() { // конструктор класса
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // описание того, что делать при закрытии окна
        setLocationByPlatform(true); // место положение окна устанавливается платформой

        //экземпляр слушателя
        WindowAdapter listener = new EventListener(); // WindowAdapter, чтобы менять EventListener и не быть зависимыми от методов EventListener
        addWindowListener(listener); // зададим этот листенер как листенер окна
    }

    protected void onCreate() { // как реагировать при создании окна
    }

}
