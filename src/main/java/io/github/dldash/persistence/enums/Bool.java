package io.github.dldash.persistence.enums;

public enum Bool {

    AND(" AND "),
    OR(" OR ");

    private final String value;

    Bool(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}
