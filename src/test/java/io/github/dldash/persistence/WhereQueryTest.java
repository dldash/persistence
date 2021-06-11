package io.github.dldash.persistence;

import io.github.dldash.persistence.builders.WhereQuery;
import io.github.dldash.persistence.contracts.Query;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WhereQueryTest extends BaseTest {

    @Test
    public void where() {
        Query where = WhereQuery.builder()
                .where("A", 1)
                .where("B", ">", 2)
                .whereNull("C")
                .whereContains("D", "D")
                .build();

        a("A = ? AND B > ? AND C IS NULL AND D LIKE ?", Arrays.asList(1, 2, "%D%"), where);
    }

    @Test
    public void group() {
        Query where = WhereQuery.builder()
                .where("A", 1)
                .where(x -> x.where("B", 2).orWhere("C", 3))
                .build();

        a("A = ? AND (B = ? OR C = ?)", Arrays.asList(1, 2, 3), where);
    }

    @Test
    public void when() {
        Map<String, String> filters = new HashMap<>();
        filters.put("A", "A");
        filters.put("B", "B");

        Query where = WhereQuery.builder()
                .when(filters.get("A"), (b, v) -> b.where("A", v))
                .when(filters.get("B"), (b, v) -> b.where("B", v))
                .when(true, b -> b.where("C", 10))
                .build();

        a("A = ? AND B = ? AND C = ?", Arrays.asList("A", "B", 10), where);
    }

}
