public class Main {

    public static void main(String[] args) throws ListString.IndexOutOfListException {
        //Создаём список и выводим его - конструктор работает
        ListString listString = new ListString("во поле берёзка стояла");
        System.out.println(listString);

        //Длина строки
        System.out.println(listString.length());

        //Метод append - смотрим, как работает
        listString.append("во поле кудрявая стояла");
        System.out.println(listString);

        //Смотрим метод вставки - insert
        listString.insert(2, "-вставка-");
        System.out.println(listString);

        //Выведем подстроку
        System.out.println(listString.substring(2, 44));
    }
}
