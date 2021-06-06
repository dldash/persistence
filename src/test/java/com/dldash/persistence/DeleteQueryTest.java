package com.dldash.persistence;

import com.dldash.persistence.builders.DeleteQuery;
import com.dldash.persistence.contracts.Query;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class DeleteQueryTest extends BaseTest {

    @Test
    public void delete() {
        Query query = DeleteQuery.builder()
                .table("table")
                .where("A", 1)
                .build();

        a("DELETE FROM table WHERE A = ?", Collections.singletonList(1), query);
    }

}
