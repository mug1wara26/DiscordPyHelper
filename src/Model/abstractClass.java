package Model;

public abstract class abstractClass {
    protected String name;

    protected abstractClass(String name) {
        this.name = name;
    }

    abstract String getName();
}
