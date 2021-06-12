package io.github.dldash.persistence.builders;

import io.github.dldash.persistence.contracts.BuilderContract;
import io.github.dldash.persistence.contracts.Query;
import io.github.dldash.persistence.objects.ConcreteQuery;
import io.github.dldash.persistence.objects.Raw;

import java.util.ArrayList;
import java.util.List;

public final class InsertQuery implements BuilderContract {

    private boolean ignore = false;

    private String table;

    private final List<String> columns = new ArrayList<>();
    private final List<String> parameters = new ArrayList<>();
    private final List<Object> bindings = new ArrayList<>();

    private final List<String> duplicateColumns = new ArrayList<>();
    private final List<Object> duplicateBindings = new ArrayList<>();

    public static InsertQuery builder() {
        return new InsertQuery();
    }

    private InsertQuery() {

    }

    public InsertQuery table(String table) {
        this.table = table;
        return this;
    }

    public InsertQuery ignore() {
        ignore = true;
        return this;
    }

    public InsertQuery insert(String column, Object value) {
        columns.add(column);
        parameters.add("?");
        bindings.add(value);
        return this;
    }

    public InsertQuery insert(String column, Raw raw) {
        columns.add(column);
        parameters.add(raw.value());
        return this;
    }

    public InsertQuery insertOrUpdate(String column, Object value) {
        insert(column, value);
        duplicateColumns.add(column + " = ?");
        duplicateBindings.add(value);
        return this;
    }

    public InsertQuery insertOrUpdate(String column, Raw raw) {
        insert(column, raw);
        duplicateColumns.add(column + " = " + raw.value());
        return this;
    }

    private String ignoreClause() {
        return ignore ? " IGNORE" : "";
    }

    private String columns() {
        return "(" + String.join(", ", columns) + ")";
    }

    private String parameters() {
        return "(" + String.join(", ", parameters) + ")";
    }

    private String updates() {
        return !duplicateColumns.isEmpty() ? " ON DUPLICATE KEY UPDATE " + String.join(", ", duplicateColumns) : "";
    }

    private String sql() {
        return "INSERT" + ignoreClause() + " INTO " + table + " " + columns() + " VALUES " + parameters() + updates();
    }

    private List<Object> bindings() {
        List<Object> values = new ArrayList<>(bindings);

        if (!duplicateBindings.isEmpty()) {
            values.addAll(duplicateBindings);
        }

        return values;
    }

    @Override
    public Query build() {
        return new ConcreteQuery(sql(), bindings());
    }

}
