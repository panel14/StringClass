//Класс исключение
public class IndexOutOfListException extends Exception {
    private final int index;

    public int getIndex() {
        return index;
    }

    public IndexOutOfListException(String message, int ind) {
        super(message);
        index = ind;
    }
}
