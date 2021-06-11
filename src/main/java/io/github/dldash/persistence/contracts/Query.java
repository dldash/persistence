package io.github.dldash.persistence.contracts;

import io.github.dldash.persistence.builders.SelectQuery;
import io.github.dldash.persistence.objects.Raw;

import java.util.List;

public interface Query {

    static SelectQuery builder() {
        return SelectQuery.builder();
    }

    static Raw raw(String value) {
        return Raw.create(value);
    }

    String sql();

    List<Object> bindings();

}
