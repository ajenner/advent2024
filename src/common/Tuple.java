package common;

import java.util.Objects;

public class Tuple <A, B, C> {
    A a;
    B b;
    C c;

    public Tuple (A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tuple)) {
            return false;
        }
        var t = (Tuple) o;
        return this.a.equals(t.a) && this.b.equals(t.b) && this.c.equals(t.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }

}
