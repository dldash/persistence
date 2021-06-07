package com.dldash.persistence;

import com.dldash.persistence.builders.WhereQuery;
import com.dldash.persistence.contracts.Query;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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
                .orWhere(x -> x.whereNull("D").whereNotNull("E"))
                .build();

        a("A = ? AND (B = ? OR C = ?)", Arrays.asList(1, 2, 3), where);
    }

}
