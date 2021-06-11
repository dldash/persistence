package io.github.dldash.persistence;

import io.github.dldash.persistence.builders.SelectQuery;
import io.github.dldash.persistence.contracts.Query;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class BaseTest {

    public SelectQuery query() {
        return Query.builder().table("table");
    }

    public String cleanup(Query query) {
        return query.sql().replaceAll("\\s+", " ").trim();
    }

    public void a(String sql, Query query) {
        assertEquals(sql, cleanup(query));
    }

    public void a(String sql, List<Object> bindings, Query query) {
        a(sql, query);
        assertEquals(bindings, query.bindings());
    }

}
