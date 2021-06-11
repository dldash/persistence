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
    private final List<String> assignments = new ArrayList<>();
    private final List<Object> bindings = new ArrayList<>();

    private final List<String> duplicateKeyAssignments = new ArrayList<>();
    private final List<Object> duplicateKeyBindings = new ArrayList<>();

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
        assignments.add("?");
        bindings.add(value);
        return this;
    }

    public InsertQuery insert(String column, Raw raw) {
        columns.add(column);
        assignments.add(raw.value());
        return this;
    }

    public InsertQuery insertOrUpdate(String column, Object value) {
        insert(column, value);
        duplicateKeyAssignments.add(column + " = ?");
        duplicateKeyBindings.add(value);
        return this;
    }

    public InsertQuery insertOrUpdate(String column, Raw raw) {
        insert(column, raw);
        duplicateKeyAssignments.add(column + " = " + raw.value());
        return this;
    }

    private String ignoreClause() {
        return ignore ? "IGNORE" : "";
    }

    private String columns() {
        return "(" + String.join(", ", columns) + ")";
    }

    private String assignments() {
        return "(" + String.join(", ", assignments) + ")";
    }

    private String duplicateKeyUpdateClause() {
        if (duplicateKeyAssignments.isEmpty()) {
            return "";
        }
        return "ON DUPLICATE KEY UPDATE " + String.join(", ", duplicateKeyAssignments);
    }

    private String sql() {
        return "" +
                " INSERT " + ignoreClause() +
                "   INTO " + table +
                " " + columns() +
                " VALUES " + assignments() +
                " " + duplicateKeyUpdateClause();
    }

    private List<Object> bindings() {
        List<Object> values = new ArrayList<>(bindings);
        values.addAll(duplicateKeyBindings);
        return values;
    }

    @Override
    public Query build() {
        return new ConcreteQuery(sql(), bindings());
    }

}
