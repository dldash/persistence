package com.dldash.persistence.builders;

import com.dldash.persistence.contracts.BuilderContract;
import com.dldash.persistence.contracts.Query;
import com.dldash.persistence.contracts.WhereContract;
import com.dldash.persistence.enums.Bool;
import com.dldash.persistence.objects.ConcreteQuery;

import java.util.List;

public final class DeleteQuery implements BuilderContract, WhereContract<DeleteQuery> {

    private String table;

    private final WhereQuery where = WhereQuery.builder();

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
        Query query = where.build();

        String whereClause = !query.sql().isEmpty() ? " WHERE " + query.sql() : "";

        return "DELETE FROM " + table + whereClause;
    }

    private List<Object> bindings() {
        return where.build().bindings();
    }

    @Override
    public DeleteQuery whereRaw(String sql, List<Object> bindings, Bool bool) {
        where.whereRaw(sql, bindings, bool);
        return this;
    }

    @Override
    public Query build() {
        return new ConcreteQuery(sql(), bindings());
    }

}
