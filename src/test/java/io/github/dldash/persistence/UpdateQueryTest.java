package io.github.dldash.persistence;

import io.github.dldash.persistence.builders.UpdateQuery;
import io.github.dldash.persistence.contracts.Query;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class UpdateQueryTest extends BaseTest {

    @Test
    public void update() {
        Query query = UpdateQuery.builder()
                .table("table")
                .update("A", 1)
                .update("B", null)
                .update("C", Query.raw("NOW()"))
                .where("D", 2)
                .build();

        a("UPDATE table SET A = ?, B = NULL, C = NOW() WHERE D = ?", Arrays.asList(1, 2), query);
    }

    @Test
    public void updateIfPresent() {
        Query query = UpdateQuery.builder()
                .table("table")
                .updateIfPresent("A", 1)
                .updateIfPresent("B", null)
                .where("C", 2)
                .build();

        a("UPDATE table SET A = ? WHERE C = ?", Arrays.asList(1, 2), query);
    }

    @Test
    public void bits() {
        Query query = UpdateQuery.builder()
                .table("table")
                .updateBits("A", 1 << 1, 1 << 2)
                .turnOnBits("B", 1 << 1)
                .turnOffBits("C", 1 << 2)
                .build();

        a("UPDATE table SET A = (IFNULL(A, 0) | 2) &~ 4, B = IFNULL(B, 0) | 2, C = IFNULL(C, 0) &~ 4", query);
    }

}
