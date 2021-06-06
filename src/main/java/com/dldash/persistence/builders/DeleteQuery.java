package com.dldash.persistence.builders;

import com.dldash.persistence.contracts.Query;
import com.dldash.persistence.contracts.BuilderContract;
import com.dldash.persistence.contracts.WhereContract;
import com.dldash.persistence.queries.ConcreteQuery;

import java.util.ArrayList;
import java.util.List;

public final class DeleteQuery implements BuilderContract, WhereContract<DeleteQuery> {

    private String table;

    private final StringBuilder wheres = new StringBuilder();
    private final List<Object> whereBindings = new ArrayList<>();

    public static DeleteQuery builder() {
        return new DeleteQuery();
    }

    private DeleteQuery() {

    }

    public DeleteQuery table(String table) {
        this.table = table;
        return this;
    }

    private String sql() {
        return "DELETE FROM " + table + wheres;
    }

    private List<Object> bindings() {
        return whereBindings;
    }

    @Override
    public DeleteQuery whereRaw(String sql, List<Object> bindings, String bool) {
        wheres.append(wheres.length() > 0 ? bool : " WHERE ").append(sql);
        if (bindings != null) {
            this.whereBindings.addAll(bindings);
        }
        return this;
    }

    @Override
    public Query build() {
        return new ConcreteQuery(sql(), bindings());
    }

}
