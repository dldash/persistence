package io.github.dldash.persistence;

import io.github.dldash.persistence.builders.DeleteQuery;
import io.github.dldash.persistence.contracts.Query;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class DeleteQueryTest extends BaseTest {

    @Test
    public void delete() {
        Query query = DeleteQuery.builder()
                .table("table")
                .where("A", 1)
                .whereNotNull("B")
                .build();

        a("DELETE FROM table WHERE A = ? AND B IS NOT NULL", Collections.singletonList(1), query);
    }

}
