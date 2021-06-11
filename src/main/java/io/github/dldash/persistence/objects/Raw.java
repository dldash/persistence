package io.github.dldash.persistence.objects;

public final class Raw {

    private final String value;

    public static Raw create(String value) {
        return new Raw(value);
    }

    private Raw(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}
