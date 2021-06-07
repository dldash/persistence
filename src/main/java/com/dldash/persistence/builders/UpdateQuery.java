package com.dldash.persistence.builders;

import com.dldash.persistence.contracts.BuilderContract;
import com.dldash.persistence.contracts.Query;
import com.dldash.persistence.contracts.WhereContract;
import com.dldash.persistence.enums.Bool;
import com.dldash.persistence.objects.ConcreteQuery;
import com.dldash.persistence.objects.Raw;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public final class UpdateQuery implements BuilderContract, WhereContract<UpdateQuery> {

    private String table;

    private final StringJoiner updates = new StringJoiner(", ");
    private final List<Object> updateBindings = new ArrayList<>();

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
        updates.add(column + " = ?");
        updateBindings.add(value);
        return this;
    }

    public UpdateQuery updateIfPresent(String column, Object value) {
        if (value != null) {
            update(column, value);
        }
        return this;
    }

    public UpdateQuery update(String column, Raw raw) {
        updates.add(column + " = " + (raw != null ? raw.value() : "NULL"));
        return this;
    }

    public UpdateQuery updateBits(String column, int bitsToTurnOn, int bitsToTurnOff) {
        updates.add(column + " = (IFNULL(" + column + ", 0) | " + bitsToTurnOn + ") &~ " + bitsToTurnOff);
        return this;
    }

    public UpdateQuery turnOnBits(String column, int bits) {
        updates.add(column + " = IFNULL(" + column + ", 0) | " + bits);
        return this;
    }

    public UpdateQuery turnOffBits(String column, int bits) {
        updates.add(column + " = IFNULL(" + column + ", 0) &~ " + bits);
        return this;
    }

    private String sql() {
        Query whereClause = where.build();

        String wheres = !whereClause.sql().isEmpty() ? " WHERE " + whereClause.sql() : "";

        return "UPDATE " + table + " SET " + updates + wheres;
    }

    private List<Object> bindings() {
        List<Object> bindings = new ArrayList<>(updateBindings);
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
