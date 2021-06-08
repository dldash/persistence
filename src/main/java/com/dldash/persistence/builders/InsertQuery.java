package com.dldash.persistence.builders;

import com.dldash.persistence.contracts.Query;
import com.dldash.persistence.contracts.BuilderContract;
import com.dldash.persistence.objects.Raw;
import com.dldash.persistence.objects.ConcreteQuery;

import java.util.ArrayList;
import java.util.List;

public final class InsertQuery implements BuilderContract {

    private boolean ignore = false;

    private String table;

    private final List<String> columns = new ArrayList<>();
    private final List<String> params = new ArrayList<>();
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
        params.add("?");
        bindings.add(value);
        return this;
    }

    public InsertQuery insert(String column, Raw raw) {
        columns.add(column);
        params.add(raw.value());
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

    private String columnClause() {
        return "(" + String.join(", ", columns) + ")";
    }

    private String paramClause() {
        return "(" + String.join(", ", params) + ")";
    }

    private String onDuplicateKeyUpdateClause() {
        if (duplicateKeyAssignments.isEmpty()) {
            return "";
        }
        return "ON DUPLICATE KEY UPDATE " + String.join(", ", duplicateKeyAssignments);
    }

    private String sql() {
        return "" +
                " INSERT " + ignoreClause() +
                "   INTO " + table +
                " " + columnClause() +
                " VALUES " + paramClause() +
                " " + onDuplicateKeyUpdateClause();
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
