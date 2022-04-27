public class Main {

    public static void main(String[] args) {
        System.out.println("-----Функция append. Вставка char (2 раза)-----");
        ListString list = new ListString();

        list.append('I');
        System.out.println(list);

        list.append('t');
        System.out.println(list);

        System.out.println("-----Вставка String-----");
        list.append("mo ");
        list.append("University");
        System.out.println(list);

        System.out.println("-----Вставка ListString-----");
        ListString another = new ListString();
        another.append(" more then University");

        list.append(another);
        System.out.println(list);

        System.out.println("-----Вывод на экран (метод toString())-----");
        System.out.println(list);

        try {

            System.out.println("-----Методы charAt и setCharAt-----");
            System.out.println("25 символ:" + list.charAt(25));
            System.out.println("Замена 5 символа на \"1\":");
            list.setCharAt(5, '1');

            System.out.println(list);

            System.out.println("-----Метод length()-----");
            System.out.println(list.length());

            System.out.println("-----Метод substring()-----");
            System.out.println("Подстрока с 6 по 26 символ: " + list.substring(6, 26));

            System.out.println("-----Методы insert-----");
            System.out.println("Вставка строки с индекса 5:");
            list.insert(5, "!!??");
            System.out.println(list);

            System.out.println("-----Исключение IndexOutOfListException:");
            list.setCharAt(50, 'c');

        } catch (ListString.IndexOutOfListException e) {

            e.printStackTrace();
            System.out.println(e.getIndex());
        }
    }
}
