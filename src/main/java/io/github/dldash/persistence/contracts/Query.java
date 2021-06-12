package io.github.dldash.persistence.contracts;

import io.github.dldash.persistence.builders.InsertQuery;
import io.github.dldash.persistence.builders.SelectQuery;
import io.github.dldash.persistence.builders.UpdateQuery;
import io.github.dldash.persistence.objects.Raw;

import java.util.List;

public interface Query {

    static SelectQuery builder() {
        return SelectQuery.builder();
    }

    static SelectQuery table(String table) {
        return SelectQuery.builder().table(table);
    }

    static InsertQuery insert(String table) {
        return InsertQuery.builder().table(table);
    }

    static UpdateQuery update(String table) {
        return UpdateQuery.builder().table(table);
    }

    static Raw raw(String value) {
        return Raw.create(value);
    }

    String sql();

    List<Object> bindings();

}
