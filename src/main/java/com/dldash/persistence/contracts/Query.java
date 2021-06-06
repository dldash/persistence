package com.dldash.persistence.contracts;

import com.dldash.persistence.builders.SelectQuery;
import com.dldash.persistence.objects.Raw;

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
