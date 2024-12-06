package common;

import java.util.Objects;

public class Triple <A, B, C> {
    public A a;
    public B b;
    public C c;

    public Triple (A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Triple triple)) {
            return false;
        }
        return this.a.equals(triple.a) && this.b.equals(triple.b) && this.c.equals(triple.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }

    public String prettyPrint() {
        return "A: " + this.a + " B: " + this.b + " C: " + this.c;
    }

}
