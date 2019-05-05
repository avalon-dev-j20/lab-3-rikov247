package ru.avalon.java.frames;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

import ru.avalon.java.AbstractFrame;

/**
 * Приложение - простой калькулятор. РЕАЛИЗОВАН GUI.
 */
public class Calculator extends AbstractFrame {

    /**
     * TODO (преподаватель):<n>
     * 1. Почему при увеличении размеров окна больше ограничений, ограничения
     * плохо работают? :) (если быстро изменить размер, то вроде бы работают, а
     * если медленно, то нет).<n>
     * 2. Как работать с клипбордом, в двух словах?
     * 3. Реализация работы калькулятора требует дальнейшей проработки и сложен
     * для меня на данный момент.<n>
     */
    
// Инициализация переменных:
    // - числовые константы
    private int indent = 3; // отступ для всех границ
    // - лэйблы 
    private JLabel resultText = new JLabel("0", JLabel.RIGHT); // лэйбл результата расчетов с выравниванием по правому краю
    // - кнопки взаимодействия:
    private JButton button0 = new JButton("0");
    private JButton button1 = new JButton("1");
    private JButton button2 = new JButton("2");
    private JButton button3 = new JButton("3");
    private JButton button4 = new JButton("4");
    private JButton button5 = new JButton("5");
    private JButton button6 = new JButton("6");
    private JButton button7 = new JButton("7");
    private JButton button8 = new JButton("8");
    private JButton button9 = new JButton("9");
    private JButton buttonCE = new JButton("CE"); // очистка дисплея, сброс последнего введенного операнда
    private JButton buttonPlus = new JButton("+");
    private JButton buttonMinus = new JButton("-");
    private JButton buttonEqually = new JButton("="); // знак равно (подсчет итогового результата)
    private JButton buttonDot = new JButton("."); // знак точки для ввода чисел с плавающей точкой
    private JButton buttonDivision = new JButton("/"); // знак деления
    private JButton buttonMultiplication = new JButton("*"); // знак умножения
    // - панели:
    private JPanel resultPanel = new JPanel(); // панель отображения результатов (промежуточных и итогового)
    private JPanel buttonPanel = new JPanel(); // панель кнопок взаимодействия
    private JPanel equallyPanel = new JPanel(); // панель для знака равно

    // Слушатель для реализации ограничения максимально возможного размера окна
    public class MaximumSizeListener extends ComponentAdapter {

        @Override
        public void componentResized(ComponentEvent e) {
            /**
             * Берем значение окна [getWidth()] и если оно больше ограничения,
             * то устанавливаем в качестве нового значения, значение этого
             * ограничения, если меньше, то оставляем существующее.<n>
             * Width - ширина окна; Height - высота окна.
             */
            int newWidth = getWidth() > 350 ? 350 : getWidth();
            int newHeight = getHeight() > 450 ? 450 : getHeight();
            setSize(newWidth, newHeight);
        }
    };

    // Основной метод
    @Override
    protected void onCreate() { // Действия при создании окна
        setTitle("Calculator"); // устаналиваем титульное название

        // Установка ограничений для окна:
        setMinimumSize(new Dimension(270, 350)); // минимальный размер окна
        addComponentListener(new MaximumSizeListener()); // максимальный размер окна 

        GridBagLayout gridBag = new GridBagLayout(); // layout менеджер
        GridBagConstraints gbc = new GridBagConstraints(); // класс для получения правил размещения компонентов
        setLayout(gridBag); // установка layout менеджера для окна

        // Добавление панели вывода результата
        gbc.weightx = 1.0; // добавление горизонтального веса - для реализации изменения размера кнопок (панелей) при изменении размера окна
        gbc.weighty = 1.0; // добавление вертикального веса      
        gbc.anchor = GridBagConstraints.FIRST_LINE_END; // способ размещения компоненты в ячейке = в конце строки
        gbc.gridwidth = GridBagConstraints.REMAINDER; // количество ячеек в строке, занимаемых компонентом. REMAINDER = компонент занимает остаток строки
        gbc.fill = GridBagConstraints.BOTH; // подгонка компонента (панели) под размер ячейки = расстягивается и горизонтально, и вертикально       
        gridBag.setConstraints(createResultPanel(), gbc); // применение правил размещения для компоненты (панели), описанных выше
        add(createResultPanel()); // добавление панели в окно приложения

        // Добавление панели кнопок взаимодействия
        createButtonPanel(button7, button8, button9, buttonPlus, // создание панели
                button4, button5, button6, buttonMinus,
                button1, button2, button3, buttonMultiplication,
                buttonCE, button0, buttonDot, buttonDivision);
        gridBag.setConstraints(buttonPanel, gbc); // применение правил размещения, созданных выше, к панели
        add(buttonPanel); // добавление панели

        // Добавление панели кнопки равно, при нажатии на которую происходит математический расчет
        gbc.anchor = GridBagConstraints.CENTER; // способ размещения компоненты в ячейке = по центру
        // правила, описанные ранее, тоже применяются
        gridBag.setConstraints(createEquallyButtonPanel(), gbc); // применение правил размещения к панели
        add(createEquallyButtonPanel());

        pack(); // установка размера для окна подходящего под определенные параметры (размеры выбираются самой системой)
    }

    // Создание панели отображения результатов. Здесь можно ее конфигурировать.
    private JPanel createResultPanel() {
        resultPanel.setLayout(new BorderLayout()); // установка layout менеджера = BorderLayout
        resultText.setFont(new Font("Arial", Font.BOLD, 50)); // установка шрифта для лэйбла
        resultPanel.add(resultText); // добавление кнопки "=" на панель

        Border border = BorderFactory.createEmptyBorder(indent, indent, indent, indent); // инициализация пустой границы с отступами
        resultPanel.setBorder(border); // добавление инициализированной границы
        return resultPanel;
    }

    // Создание панели кнопок (неограниченное количество button). Здесь можно ее конфигурировать.
    private JPanel createButtonPanel(JButton... button) {
        buttonPanel.setLayout(new GridLayout(4, 4, 5, 5)); // установка layout менеджера = сетка 4 на 4 с отступами каждой ячейки сетки друг от друга на 5 пикселей по ширине и длине
        for (JButton butt : button) { // для каждой кнопки (butt) из массива кнопок (button) выполнить:
            if (butt.getText().compareTo("*") == 0) { // если текст кнопки это "*", то выполнить:
                butt.setFont(new Font("Arial", Font.BOLD, 24)); // установка шрифта (размер 26 и жирный)
                butt.setMargin(new Insets(10, 0, 0, 0)); // установка отступа для текста от верхнего края границы кнопки на 10 пикселлей
            } else {
                butt.setFont(new Font("Arial", Font.BOLD, 16));
            } // если любая другая кнопка кроме "*", то устанавливаем шрифт (рамзер 16 и жирный)
            buttonPanel.add(butt);
        }

        Border border = BorderFactory.createEmptyBorder(indent, indent, indent, indent); // инициализация отступов вокруг всей панели кнопок
        buttonPanel.setBorder(border); // применение этих отступов к панели
        return buttonPanel;
    }

    // Создание панели кнопки равно. Здесь можно ее конфигурировать.
    private JPanel createEquallyButtonPanel() {
        equallyPanel.setLayout(new BorderLayout());
        buttonEqually.setFont(new Font("Arial", Font.BOLD, 30));
        equallyPanel.add(buttonEqually);

        Border border = BorderFactory.createEmptyBorder(indent, indent, indent, indent);
        equallyPanel.setBorder(border);
        return equallyPanel;
    }

    /**
     * Третье. "Клипборд". Инициализируем объект для взаимодействия с буфером
     * обмена. Чтобы записывать промежуточные значения при расчетах.
     */
    private Clipboard clipboard = Toolkit // инициализация инструмента для для взаимодействия с буфером обмена
            .getDefaultToolkit()
            .getSystemClipboard();

    /**
     * Выполняет копирование тектса в буфер обмена.
     *
     * @param text текстовые данные, которые следует поместить в буфер обмена
     */
    private void copyToClipboard(String text) {
        StringSelection selection = new StringSelection(text);
        clipboard.setContents(selection, selection);
    }
}