package io.github.dldash.persistence.builders;

import io.github.dldash.persistence.contracts.BuilderContract;
import io.github.dldash.persistence.contracts.Query;
import io.github.dldash.persistence.contracts.WhereContract;
import io.github.dldash.persistence.enums.Bool;
import io.github.dldash.persistence.objects.ConcreteQuery;

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

    @Override
    public DeleteQuery whereRaw(String sql, List<Object> bindings, Bool bool) {
        where.whereRaw(sql, bindings, bool);
        return this;
    }

    @Override
    public Query build() {
        Query whereQuery = where.build();

        String whereClause = !whereQuery.sql().isEmpty() ? " WHERE " + whereQuery.sql() : "";

        String sql = "DELETE FROM " + table + whereClause;

        List<Object> bindings = whereQuery.bindings();

        return new ConcreteQuery(sql, bindings);
    }

}
