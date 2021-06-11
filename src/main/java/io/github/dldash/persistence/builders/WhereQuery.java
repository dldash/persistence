package io.github.dldash.persistence.builders;

import io.github.dldash.persistence.contracts.BuilderContract;
import io.github.dldash.persistence.contracts.Query;
import io.github.dldash.persistence.contracts.WhereContract;
import io.github.dldash.persistence.enums.Bool;
import io.github.dldash.persistence.objects.ConcreteQuery;

import java.util.ArrayList;
import java.util.List;

public final class WhereQuery implements BuilderContract, WhereContract<WhereQuery> {

    private final StringBuilder sql = new StringBuilder();
    private final List<Object> bindings = new ArrayList<>();

    public static WhereQuery builder() {
        return new WhereQuery();
    }

    private WhereQuery() {

    }

    @Override
    public WhereQuery whereRaw(String sql, List<Object> bindings, Bool bool) {
        if (sql != null && !sql.isEmpty()) {
            if (this.sql.length() > 0) {
                this.sql.append(bool.value());
            }
            this.sql.append(sql);
        }

        if (bindings != null && !bindings.isEmpty()) {
            this.bindings.addAll(bindings);
        }

        return this;
    }

    @Override
    public Query build() {
        return new ConcreteQuery(sql.toString(), bindings);
    }

}
