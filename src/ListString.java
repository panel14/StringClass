//Основной класс - односвязный список
//Односвязный список - это структура данных, в которой каждый элемент содержит ссылку на следующий элемент
//Позволяет хранить неограниченное количество элементов, а также производить вставку/удаление в любое место структуры
//Также есть возможность редактировать элементы - объединять и разделять (для оптимального хранения)
//В самом простом случае список реализуется через 1 указатель (head) - это первый элемент списка, содержит ссылку (next)
//на все последующие элементы. В нашем случае есть ещё указатель tail - указывает на последний элемент списка. Так тоже
//довольно часто делают, чтобы добавление элементов в конец списка происходило за O(1) (лучшее время)
public class ListString {
    //Внутренний класс - блок списка
    private static class StringItem {
        //Максимальная длина блока
        private final static byte SIZE = 16;
        //Массив символов, где хранятся символы строки
        private char[] symbols;
        //Ссылка на следующий элемент списка
        private StringItem next;
        //Актуальный размер блока (сколько элементов сейчас находится в блоке)
        private byte size;

        //Публичный конструктор класса
        public StringItem(StringItem next, int size) {
            this.next = next;
            this.size = 0;
            symbols = new char[size];
        }

        //Метод добавления следующего символа в массив символов
        public void addSymbol(char ch) {
            symbols[size] = ch;
            size++;
        }

        //Перегруженный метод toString - возвращает строку, составленную из элементов массива символов
        //Создаём массив актуального размера блока и через цикл копируем в него элементы исходного массива
        //Таким образом избавимся от незаполненных элементов исходного массива
        //Метод copyValueOf используется, чтобы преобразовать массив в String
        @Override
        public String toString() {
            char[] actualSymbols = new char[size];
            for (int i = 0; i < size; i++)
                actualSymbols[i] = symbols[i];

            return String.copyValueOf(actualSymbols);
        }
    }

    public static class IndexOutOfListException extends Exception {
        private final int index;

        public int getIndex() {
            return index;
        }

        public IndexOutOfListException(String message, int ind) {
            super(message + ": " + ind);
            index = ind;
        }
    }

    //Публичный конструктор класса списка
    public ListString() {
        head = new StringItem(null, StringItem.SIZE);
        tail = head;
    }

    //Указатель на начало списка
    private StringItem head;
    //Указатель на конец списка
    private StringItem tail;

    //Функция, определяет, можно ли объединить текущий блок и следующий за ним
    //Условие простое - если суммарный размер блоков не превышает максимально допустимый (16) - объединяем
    boolean is_unite(StringItem block) {
        return (block.next != null) && (block.size + block.next.size <= StringItem.SIZE);
    }

    //Функция объединения блоков
    //Создаём один большой массив размером (длина блока) + (длина идущего за ним блока)
    //Туда через циклы записываем символы объединяемых блоков
    //Этот массив и будет содержаться в большом объединённом блоке.
    //Далее просто меняем ссылки списка - следующий элемент после 1-ого блока - это следующий у 2-ого
    //По итогу из 2-ух блоков получаем один объединённый
    private void uniteWithNext(StringItem block) {
        char[] unitedSymbols = new char[block.size + block.next.size];
        for (int i = 0; i < block.size; i++)
            unitedSymbols[i] = block.symbols[i];
        for (int i = block.size; i < block.next.size + block.size; i++)
            unitedSymbols[i] = block.next.symbols[i - block.size];

        block.symbols = unitedSymbols;

        block.size += block.next.size;
        block.next = block.next.next;
    }

    //Функция, делит блок на два подряд идущих - в параметрах блок, который делим и его размер после деления
    //Создаём новый блок размером, (текущий размер блока) - (требуемый размер)
    //В этот новый блок пишем все символы, которые останутся от блока после разделения
    //Создаём новый массив - в нём будут символы со старого блока, в количестве size
    //И меняем ссылки для всего этого - теперь блок, который мы создали идёт после старого блока
    //Размеры (size) тоже меняем
    private StringItem splitItem(StringItem toSplit, int size) {
        StringItem splitItem = initNewItem(toSplit.size - size);

        for (int i = 0; i < toSplit.size - size; i++)
            splitItem.addSymbol(toSplit.symbols[i + size]);

        char[] newSymbols = new char[size];

        for (int i = 0; i < size; i++)
            newSymbols[i] = toSplit.symbols[i];

        toSplit.symbols = newSymbols;
        toSplit.size = (byte) size;

        splitItem.next = toSplit.next;
        toSplit.next = splitItem;

        if (tail == toSplit)
            tail = splitItem;
        return toSplit;
    }

    //Функция, создаёт новый блок размером size
    //Расписать зачем отдельный метод
    private StringItem initNewItem(int size) {
        return new StringItem(null, size);
    }

    //Функция, добавляет новый элемент в конец списка
    private void addItem(StringItem item) {
        tail.next = item;
        tail = tail.next;
    }

    //Функция, проверяет, находится ли индекс вне границ длины. Если да, то выбрасывается исключение
    private void checkIndexOutException(int index) throws IndexOutOfListException {
        if (index > length() || index < 0)
            throw new IndexOutOfListException("Index out of range", index);
    }

    //Функция, находит индекс блока, который содержит в себе символ с номером StringLength.
    //Смысл таков - если разделить StringLength на макс размер блока, то целая часть этого числа и будет номером блока
    private int getItemIndex(int StringLength) {
        return StringLength / StringItem.SIZE;
    }

    //Получить блок по его индексу
    //Просто проходимся в цикле по всем блокам, с головного, пока не дойдём до нужного
    private StringItem getItemForIndex(int index) {
        StringItem currentItem = head;

        for (int i = 0; i < index; i++)
            currentItem = currentItem.next;

        return currentItem;
    }

    //Вычисляет длину списка (количество символов в массивах во всех блоках)
    //Проходимся по всему списку, если возможно объединить элементы - делаем это
    //В переменную записываем Size каждого блока
    public int length() {
        StringItem currentItem = head;
        int len = 0;

        while (currentItem.next != null) {
            if (is_unite(currentItem))
                uniteWithNext(currentItem);

            len += currentItem.size;
            currentItem = currentItem.next;
        }
        len += currentItem.size;

        return len;
    }

    //Получить символ по индексу
    //Проверяем индекс, не выходит ли он за границу длины
    //Если нет, то находим индекс блока, в котором содержится символ и сам блок
    //По индексу в блоке (index % StringItem.SIZE) берём сам символ
    public char charAt(int index) throws IndexOutOfListException {
        checkIndexOutException(index);

        int itemIndex = getItemIndex(index);
        StringItem currentItem = getItemForIndex(itemIndex);

        return currentItem.symbols[index % StringItem.SIZE];
    }

    //Записать символ по индексу
    //Аналогично с charAt проверка индекса, далее находим индекс блока и блок
    //По аналогичному индексу записываем заданный символ в блок
    public void setCharAt(int index, char ch) throws IndexOutOfListException {
        checkIndexOutException(index);

        int itemIndex = getItemIndex(index);
        StringItem currentItem = getItemForIndex(itemIndex);

        currentItem.symbols[index % StringItem.SIZE] = ch;
    }

    //Выделить подстроку
    //Снова проверка индексов
    //Находим индекс начального блока, с которого начнём и сам блок
    //Далее в цикле проходимся по всем блокам в интервале от start до end, записывая их символы в новый лист
    //Если наткнулись на границу блока (строка 207) - меняем блок на следующий
    public ListString substring(int start, int end) throws IndexOutOfListException {
        checkIndexOutException(start);
        checkIndexOutException(end - 1);

        ListString result = new ListString();

        int startItemIndex = getItemIndex(start - 1);
        StringItem currentItem = getItemForIndex(startItemIndex);

        for (int i = start; i < end; i++) {
            int pointer = (i - 1) % StringItem.SIZE ;
            if (pointer == currentItem.size){
                currentItem = currentItem.next;
                continue;
            }
            result.append(currentItem.symbols[pointer]);
        }

        return result;
    }

    //Добавить символ в конец строки
    //Если последний блок заполнен, создадим новый блок
    //Добавим символ в последний блок с помощью функции
    void append(char ch){
        if (tail.size == StringItem.SIZE) {
            StringItem appended = initNewItem(StringItem.SIZE);
            addItem(appended);
        }
        tail.addSymbol(ch);
    }

    //Добавление в конец строки класса ListString
    //Сначала проверим, есть ли незаполненные ячейки в головном массиве
    //Если есть - значит головной элемент единственный в списке и его длину нужно сократить до актуальной
    //Размер головного блока при создании - 16
    //Создаём новый массив актуального размера, всё перезаписываем туда, и делаем его основным
    //Далее таким же образом убираем лишние символы с последнего элемента доабавляемого списка
    //Чтобы склеить списки просто меняем ссылки
    //Конец старого списка склеиваем с началом нового
    //Конец старго списка - это конец добавляемого списка
    void append(ListString string) {
        if (head.size < StringItem.SIZE) {
            char[] formHead = new char[head.size ];

            for (int i = 0; i < head.size; i++)
                formHead[i] = head.symbols[i];

            head.symbols = formHead;
        }
        char[] formTailSymbols = new char[tail.size];
        for (int i = 0; i < tail.size; i++)
            formTailSymbols[i] = tail.symbols[i];
        tail.symbols = formTailSymbols;

        addItem(string.head);
        tail = string.tail;
    }

    //Добавить в конец строку класса String
    //Просто запишем все символы через метод append(char ch)
    void append(String string) {
        for (int i = 0; i < string.length(); i++) {
            append(string.charAt(i));
        }
    }

    //Вставить список в любое место списка по индексу
    void insert(int index, ListString string) throws IndexOutOfListException {
        //Проверяем индекс
        checkIndexOutException(index);

        //Находим индекс блока по индексу вставки
        int itemIndex = getItemIndex(index);
        //Размер вставляемого блока
        int splitSize = index % StringItem.SIZE;

        //Если размер списка, который вставляем кратен 16, значит он состоит из целых блоков, то есть его нужно
        //просто прицепить к старому списку
        if (splitSize == 0) {
            append(string);
        }
        else {
            //Иначе - находим блок, который будем делить
            StringItem itemToSplit = getItemForIndex(itemIndex);
            //Делим его функцией. Результат функции - блок, который был отделён от старого
            StringItem newTail = splitItem(itemToSplit, splitSize);

            //Избавляем хвост добавляемого списка от пустых элементов
            //Как и раньше, создаём новый массив, в него переносим заполненные ячейки старого
            char[] rangedStrTail = new char[string.tail.size];
            for (int i = 0; i < string.tail.size; i++)
                rangedStrTail[i] = string.tail.symbols[i];

            string.tail.symbols = rangedStrTail;

            //Меняем сссылки - отделёную часть цепляем к хвосту добавляемого списка
            string.tail.next = newTail.next;

            //И всю добавляемую часть к блоку, который мы делили
            newTail.next = string.head;
        }
    }

    //Функция вставки строки String на позицию index
    //Преобразуем строку в ListString методом append (в пустой ListString добавляем строку)
    //Теперь используем метод выше
    void insert(int index, String string) throws IndexOutOfListException {
        checkIndexOutException(index);
        ListString listString = new ListString();
        listString.append(string);

        this.insert(index, listString);
    }

    //Перегрузка toString
    //Каждый блок преобразуем к строке (в блоках тоже перегружен метод toString)
    //Эти строки складываем с общей строкой
    @Override
    public String toString() {
        StringItem currentItem = head;
        String result = currentItem.toString();

        while (currentItem.next != null) {
            currentItem = currentItem.next;
            result += currentItem.toString();
        }

        return result;
    }
}
