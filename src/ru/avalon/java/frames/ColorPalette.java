package ru.avalon.java.frames;

import ru.avalon.java.AbstractFrame;

import java.awt.*;
import java.awt.datatransfer.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;

/**
 * Реализация оконного приложения представляющего собой инструмент, позволяющий
 * пользователю выбирать цвет, с использованием графического пользовательского
 * интерфейса.
 */
public class ColorPalette extends AbstractFrame {

    /**
     * TODO (преподаватель):<n>
     * 1. Как задать выравнивание лэйбла относительно слайдера, если они
     * находятся внутри своей панельки с менеджером BorderLayout?<n>
     * 2. createSliderPanel(x,x,x): Как задать разбиение слайдера на области не
     * равные int. Пример: длина слайдера 255 - нужно разбить на области по 25.5
     * (или можно указать на какое количество областей можно разбить
     * слайдер?).<n>
     * 3. createSliderPanel(x,x,x): Как установить месторасположение маркера
     * слайдера ровно под слайдером и на уровне соседних лэйблов (0 и 255)?<n>
     */
    // Второе. Создание трех слайдеров, трех лэйблов слайдеров и трех лэйблов маркеров
    private JLabel textRedMarker = new JLabel("0"); // лэйблы маркеров каждого слайдера
    private JLabel textGreenMarker = new JLabel("0");
    private JLabel textBlueMarker = new JLabel("0");

    private JSlider redSlider = new JSlider(0, 255, 125); // создание слайдера с начальным значение 0, конечным значением 255, и начальной позицией 125
    private JLabel redLabel = new JLabel("Red:"); // создание надписи к слайдеру 

    private JSlider greenSlider = new JSlider(0, 255, 125);
    private JLabel greenLabel = new JLabel("Green:");

    private JSlider blueSlider = new JSlider(0, 255, 125);
    private JLabel blueLabel = new JLabel("Blue:");

    // Четвертое. Создаем ссылки на панели внутри окна
    private JPanel controlsPanel = new JPanel(); // панель со слайдерами
    private JPanel colorPanel = new JPanel(); // панель со слайдерами

    /**
     * Третье. "Клипборд". Инициализируем объект для взаимодействия с буфером
     * обмена. Чтобы при изменении позиций ползунков, цвет, соответствующий их
     * позициям, был записан в буфер обмена в виде строки, содержащей его
     * шестнадцатеричный код.
     */
    private Clipboard clipboard = Toolkit // инициализация инструмента для для взаимодействия с буфером обмена
            .getDefaultToolkit()
            .getSystemClipboard();

    // Первое, с чего начать: переопределить onCreate() - чтобы мы могли обработать наши интерфейсы. САМЫЙ ГЛАВНЫЙ ИСПОЛНЯЕМЫЙ МЕТОД.
    @Override
    protected void onCreate() { // Действия при создании окна
        setTitle("Color Picker"); // устаналиваем титульное название
        setSize(600, 300); // устанавливаем размер окна приложения

        setLayout(new GridLayout(1, 2)); // назначаем менеджер компоновки, который разделит область на цветовую палетку и слайдеры

        // Панель с палеткой выбора цвета
        JPanel panel = new JPanel(new BorderLayout()); // создаем панель с менеджером компоновки Border
        add(panel); // добавляем панель на стартовое окно
        panel.setBorder(new EmptyBorder(10, 10, 10, 10)); // устанавливаем пустую границу для созданной панели
        panel.add(createColorPanel()); // добавляем в эту панель палетку с выбором цвета (панель выбора цвета)

        // Панель со слайдерами
        add(createSlidersPanel());

        redSlider.addChangeListener(this::onSliderChange); // Десятое. В случае изменения положения ползунка в слайдере - вызывается метод в скобках
        greenSlider.addChangeListener(this::onSliderChange);
        blueSlider.addChangeListener(this::onSliderChange);

        updateSliderMarker_AndColor_AndToolTip(); // Изменение маркера возле каждого слайдера, цвета палетки и текста всплывающей подсказки
    }

    //Изменение маркера возле каждого слайдера и цвета палетки и, к тому же, всплывающей подсказки
    private void updateSliderMarker_AndColor_AndToolTip() {

        //1. updateSliderMarker - метод изменяющий текст маркера каждого слайдера
        // Слайдер с красным цветом
        int red = redSlider.getValue();  // записываем значения слайдера
        String redText = Integer.toString(red); // переводим в текст
        textRedMarker.setText(redText); // устанавливаем текст около слайдера

        // Слайдер с зеленым цветом
        int green = greenSlider.getValue();  // записываем значения слайдера
        String greenText = Integer.toString(green); // переводим в текст
        textGreenMarker.setText(greenText); // устанавливаем текст около слайдера
        // Слайдер с синим цветом
        int blue = blueSlider.getValue();  // записываем значения слайдера
        String blueText = Integer.toString(blue); // переводим в текст
        textBlueMarker.setText(blueText); // устанавливаем текст около слайдера

        //2. updateColor - метод изменяющий цвет холста и значение всплывающей подсказки в зависимости от положений трех слайдеров
        Color color = new Color(red, green, blue); // инициализация переменной, хранящей цвет, получающийся из значений с каждого из трех слайдеров
        colorPanel.setBackground(color); // устанавливаем цвет панели (на самом деле ее фона, но она пустая, поэтому как бы панели) = как цвет, получающийся при изменении положения слайдеров

        //3. update ToolTip
        /**
         * Возвращает строковое представление числа, полученного из итогового
         * цвета в 16-тиричной кодировке, подрезает получаемую строку из числа
         * (substring(2) - со второго символа..для того, чтобы отображать коды
         * только для трех цветов (red, green, blue)) и перевод все символы в
         * верхний регистр (toUpperCase).
         */
        String hexColor = "#" + Integer.toHexString(color.getRGB()).substring(2).toUpperCase();
        colorPanel.setToolTipText(hexColor); // появление всплывающей подсказки при наведении на панель цветов (палетку)
        if (!isBlank(hexColor)) {  // если строка не пустая, то:
            copyToClipboard(hexColor); // копируем текст в буфер обмена
        }
    }

    //Реализация смены текста маркера слайдера при движении ползунка, смена цвета и смена текста всплывающей подсказки.
    private void onSliderChange(ChangeEvent e) {
        updateSliderMarker_AndColor_AndToolTip();
    }

    // Пятое. Создание панели, на которой будeт слайдеры. Здесь можно будет ее настраивать (конфигурировать) и добавлять новые слайдеры или еще что либо
    private JPanel createSlidersPanel() {
        // Седьмое. Заполняем
        /**
         * GridLayout - это сложный Layout (менеджер компоновки), который
         * позволяет просто реализовать центровку и по вертикали и по
         * горизонтали.<n>
         * Передаем GridLayout. Как бы проводим операцию создания переменной от
         * LayoutManager ... = new GridLayout()
         */
        controlsPanel.setLayout(new GridLayout(3, 1)); // добавляем layout (менеджер компоновки).

        controlsPanel.add(createSliderPanel(redLabel, textRedMarker, redSlider)); // добавляем конфигурированную панель для красного слайдера
        controlsPanel.add(createSliderPanel(greenLabel, textGreenMarker, greenSlider)); // добавляем конфигурированную панель для зеленого слайдера
        controlsPanel.add(createSliderPanel(blueLabel, textBlueMarker, blueSlider)); // добавляем конфигурированную панель для синего слайдера

        return controlsPanel; // возвращаем конфигурированную панель
    }

    //Создаем панель для каждого слайдера. Здесь можно будет ёё настраивать (конфигурировать)
    private JPanel createSliderPanel(JLabel nameLabel, JLabel nameSliderMarker, JSlider slider) {
        JPanel sliderPanel = new JPanel();

        sliderPanel.setLayout(new BorderLayout()); // устанавливаем менеджер компоновки для данной панели (каждого слайдера)
        // Пытаемся задать отступы от окна для панели со сладйером и текстом возле него
        Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10); // Cоздаем пустую рамку с заданными отступами
        sliderPanel.setBorder(border); // устанавливаем созданный border для данного панели слайдера

        // описание слайдера
        slider.setPaintLabels(true); // установка лэйблов начала и конца
        slider.setMinimum(0);
        slider.setMaximum(255);

        slider.setPaintTicks(true);    // показываем шкалу на слайдере
        slider.setMinorTickSpacing(25); // устанавливаем значение между указателями шкалы на слайдере 
        slider.setMajorTickSpacing(255); // устанавливаем первый и последний маркер на слайдере
        sliderPanel.add(slider); // добавляем слайдер в панель

        // описание лэйбла маркера возле слайдера
        slider.setLayout(new GridLayout(1, 1));
        slider.add(nameSliderMarker);
        nameSliderMarker.setHorizontalAlignment(SwingConstants.CENTER);
        nameSliderMarker.setVerticalAlignment(SwingConstants.BOTTOM);

        // описание лэйбла слайдера
        nameLabel.setPreferredSize(new Dimension(40, 0)); // пишем предпочтительный размер для лэйбла (этим можно задать отступ лэйбла от слайдера). 0 = нет предпочтительного размера
        nameLabel.setVerticalAlignment(JLabel.TOP);

        sliderPanel.add(nameLabel, BorderLayout.LINE_START); // добавляем label в панель

        // описание лэйбла маркера слайдера
        //nameSliderMarker.setPreferredSize(new Dimension(25, 0)); // пишем предпочтительный размер для лэйбла (этим можно задать отступ лэйбла от слайдера). 0 = нет предпочтительного размера
        //nameSliderMarker.setVerticalAlignment(JLabel.TOP);
        //sliderPanel.add(nameSliderMarker, BorderLayout.SOUTH); // добавляем label в панель
        return sliderPanel;
    }

    // Пятое. Создаем панель для отображения цветовой палетки. Здесь ее будем конфигурировать
    private JPanel createColorPanel() {
        // Седьмое. Заполянем
        colorPanel.setLayout(new BorderLayout()); // добавляем layout (менеджер компоновки).

        Color color = new Color(redSlider.getX(), greenSlider.getX(), blueSlider.getX()); // берем начальный цвет из изначального положения всех трех слайдеров
        colorPanel.setBackground(color); // устанавливаем цвет панели (на самом деле ее фона, но она пустая, поэтому как бы панели) = как цвет, получающийся при изменении положения слайдеров
        /**
         * Возвращает строковое представление числа, полученного из итогового
         * цвета в 16-тиричной кодировке, подрезает получаемую строку из числа
         * (substring(2) - со второго символа..для того, чтобы отображать коды
         * только для трех цветов (red, green, blue)) и перевод все символы в
         * верхний регистр (toUpperCase).
         */
        colorPanel.setToolTipText("#" + Integer.toHexString(color.getRGB()).substring(2).toUpperCase());
        return colorPanel;
    }

// Методы для взаимодействия с буфером обмена
    /**
     * Выполняет копирование текста в буфер обмена.
     *
     * @param text текстовые данные, которые следует поместить в буфер обмена
     */
    private void copyToClipboard(String text) {
        StringSelection selection = new StringSelection(text);
        clipboard.setContents(selection, selection);
    }

    /**
     * Проверяет, что строка не пустая.
     *
     * @param text текстовые данные
     * @returns ture, если переданная строка null, не содержит символов, или
     * содержит только пробельные символы. В обратном случае - false.
     */
    private boolean isBlank(String text) {
        return text == null || text.trim().isEmpty(); // возвращает true, если текста нет (ссылки на него не существует) или, если ссылка есть, но строка не имеет символов кроме пробела
    }
}
