package server.utilities;
/**
 * 
 * Утилитарный класс для хранения двух элементов
 * @author Piromant
 */
public class Pair<X, Y> {
    private X first;
    private Y second;
    public Pair(X first, Y second){
        this.first = first;
        this.second = second;
    }

    public X getFirst() {
        return first;
    }

    public void setFirst(X first) {
        this.first = first;
    }

    public void setSecond(Y second) {
        this.second = second;
    }

    public Y getSecond() {
        return second;
    }
}
