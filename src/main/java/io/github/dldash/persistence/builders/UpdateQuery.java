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

    private final StringJoiner columns = new StringJoiner(", ");
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
        columns.add(column + " = ?");
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
        columns.add(column + " = " + (raw != null ? raw.value() : "NULL"));
        return this;
    }

    public UpdateQuery updateBits(String column, int bitsToTurnOn, int bitsToTurnOff) {
        columns.add(column + " = (IFNULL(" + column + ", 0) | " + bitsToTurnOn + ") &~ " + bitsToTurnOff);
        return this;
    }

    public UpdateQuery turnOnBits(String column, int bits) {
        columns.add(column + " = IFNULL(" + column + ", 0) | " + bits);
        return this;
    }

    public UpdateQuery turnOffBits(String column, int bits) {
        columns.add(column + " = IFNULL(" + column + ", 0) &~ " + bits);
        return this;
    }

    @Override
    public UpdateQuery whereRaw(String sql, List<Object> bindings, Bool bool) {
        where.whereRaw(sql, bindings, bool);
        return this;
    }

    @Override
    public Query build() {
        Query whereQuery = where.build();

        String whereClause = !whereQuery.sql().isEmpty() ? " WHERE " + whereQuery.sql() : "";

        String sql = "UPDATE " + table + " SET " + columns + whereClause;

        List<Object> bindings = new ArrayList<>(this.bindings);

        if (!whereQuery.bindings().isEmpty()) {
            bindings.addAll(whereQuery.bindings());
        }

        return new ConcreteQuery(sql, bindings);
    }

}
