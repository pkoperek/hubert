package prototype.data;

/**
 * User: koperek
 * Date: 04.03.13
 * Time: 22:58
 */
public class Pair<T> {

    private final T one;
    private final T two;

    public Pair(T one, T two) {
        this.one = one;
        this.two = two;
    }

    public T getOne() {
        return one;
    }

    public T getTwo() {
        return two;
    }

    @Override
    public String toString() {
        return "Pair{" + one + " " + two + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair that = (Pair) o;

        if (one != null ? !one.equals(that.one) : that.one != null) return false;
        if (two != null ? !two.equals(that.two) : that.two != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = one != null ? one.hashCode() : 0;
        result = 31 * result + (two != null ? two.hashCode() : 0);
        return result;
    }

    public boolean contains(T element) {
        return one.equals(element) || two.equals(element);
    }

    public T getOther(T element) {
        return one.equals(element) ? two : one;
    }
}
