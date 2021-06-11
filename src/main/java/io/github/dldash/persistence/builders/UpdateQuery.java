package io.github.dldash.persistence.builders;

import io.github.dldash.persistence.contracts.BuilderContract;
import io.github.dldash.persistence.contracts.Query;
import io.github.dldash.persistence.contracts.WhereContract;
import io.github.dldash.persistence.enums.Bool;
import io.github.dldash.persistence.objects.ConcreteQuery;
import io.github.dldash.persistence.objects.Raw;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public final class UpdateQuery implements BuilderContract, WhereContract<UpdateQuery> {

    private String table;

    private final StringJoiner assignments = new StringJoiner(", ");
    private final List<Object> bindings = new ArrayList<>();

    private final WhereQuery where = WhereQuery.builder();

    public static UpdateQuery builder() {
        return new UpdateQuery();
    }

    private UpdateQuery() {

    }

    public UpdateQuery table(String table) {
        this.table = table;
        return this;
    }

    public UpdateQuery update(String column, Object value) {
        assignments.add(column + " = ?");
        bindings.add(value);
        return this;
    }

    public UpdateQuery updateIfPresent(String column, Object value) {
        if (value != null) {
            update(column, value);
        }
        return this;
    }

    public UpdateQuery update(String column, Raw raw) {
        assignments.add(column + " = " + (raw != null ? raw.value() : "NULL"));
        return this;
    }

    public UpdateQuery updateBits(String column, int bitsToTurnOn, int bitsToTurnOff) {
        assignments.add(column + " = (IFNULL(" + column + ", 0) | " + bitsToTurnOn + ") &~ " + bitsToTurnOff);
        return this;
    }

    public UpdateQuery turnOnBits(String column, int bits) {
        assignments.add(column + " = IFNULL(" + column + ", 0) | " + bits);
        return this;
    }

    public UpdateQuery turnOffBits(String column, int bits) {
        assignments.add(column + " = IFNULL(" + column + ", 0) &~ " + bits);
        return this;
    }

    private String sql() {
        Query whereQuery = where.build();

        String whereClause = !whereQuery.sql().isEmpty() ? " WHERE " + whereQuery.sql() : "";

        return "UPDATE " + table + " SET " + assignments + whereClause;
    }

    private List<Object> bindings() {
        List<Object> bindings = new ArrayList<>(this.bindings);
        bindings.addAll(where.build().bindings());
        return bindings;
    }

    @Override
    public UpdateQuery whereRaw(String sql, List<Object> bindings, Bool bool) {
        where.whereRaw(sql, bindings, bool);
        return this;
    }

    @Override
    public Query build() {
        return new ConcreteQuery(sql(), bindings());
    }

}
