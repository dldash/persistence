package com.dldash.persistence.builders;

import com.dldash.persistence.contracts.Query;
import com.dldash.persistence.contracts.QueryBuilderContract;
import com.dldash.persistence.contracts.WhereContract;
import com.dldash.persistence.objects.Raw;
import com.dldash.persistence.queries.ConcreteQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public final class UpdateQuery implements QueryBuilderContract, WhereContract<UpdateQuery> {

    private String table;

    private final StringJoiner updates = new StringJoiner(", ");
    private final List<Object> updateBindings = new ArrayList<>();

    private final StringBuilder wheres = new StringBuilder();
    private final List<Object> whereBindings = new ArrayList<>();

    public static UpdateQuery builder() {
        return new UpdateQuery();
    }

    private UpdateQuery() {

    }

    public UpdateQuery table(String table) {
        this.table = table;
        return this;
    }

    public UpdateQuery updateRaw(String sql, List<Object> bindings) {
        updates.add(sql);
        updateBindings.addAll(bindings);
        return this;
    }

    public UpdateQuery updateRaw(String sql) {
        updates.add(sql);
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

    public UpdateQuery update(String column, Raw value) {
        updates.add(column + " = " + value);
        return this;
    }

    public UpdateQuery updateBits(String column, int bitsToAdd, int bitsToRemove) {
        updates.add(column + " = (IFNULL(" + column + ", 0) | " + bitsToAdd + ") &~ " + bitsToRemove);
        return this;
    }

    private String sql() {
        return "UPDATE " + table + " SET " + updates + wheres;
    }

    private List<Object> bindings() {
        List<Object> bindings = new ArrayList<>(updateBindings);
        bindings.addAll(whereBindings);
        return bindings;
    }

    @Override
    public UpdateQuery whereRaw(String sql, List<Object> bindings, String bool) {
        wheres.append(wheres.length() > 0 ? bool : " WHERE ").append(sql);
        if (bindings != null) {
            whereBindings.addAll(bindings);
        }
        return this;
    }

    @Override
    public Query build() {
        return new ConcreteQuery(sql(), bindings());
    }

}
