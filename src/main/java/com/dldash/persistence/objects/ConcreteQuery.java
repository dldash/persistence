package com.dldash.persistence.objects;

import com.dldash.persistence.contracts.Query;

import java.util.List;

public final class ConcreteQuery implements Query {

    private final String sql;
    private final List<Object> bindings;

    public ConcreteQuery(String sql, List<Object> bindings) {
        this.sql = sql;
        this.bindings = bindings;
    }

    @Override
    public String sql() {
        return sql;
    }

    @Override
    public List<Object> bindings() {
        return bindings;
    }

}
