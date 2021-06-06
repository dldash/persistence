package com.dldash.persistence;

import com.dldash.persistence.builders.InsertQuery;
import com.dldash.persistence.contracts.Query;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class InsertQueryTest extends BaseTest {

    @Test
    public void insert() {
        Query query = InsertQuery.builder()
                .table("table")
                .ignore()
                .insert("A", 1)
                .insert("B", 2)
                .build();

        a("INSERT IGNORE INTO table (A, B) VALUES (?, ?)", Arrays.asList(1, 2), query);
    }

    @Test
    public void raw() {
        Query query = InsertQuery.builder()
                .table("table")
                .insert("A", Query.raw("NOW()"))
                .build();

        a("INSERT INTO table (A) VALUES (NOW())", query);
    }

    @Test
    public void insertOrUpdate() {
        Query query = InsertQuery.builder()
                .table("table")
                .insert("A", 1)
                .insertOrUpdate("B", 2)
                .insertOrUpdate("C", Query.raw("NOW()"))
                .build();

        String sql = "INSERT INTO table (A, B, C) VALUES (?, ?, NOW()) ON DUPLICATE KEY UPDATE B = ?, C = NOW()";

        a(sql, Arrays.asList(1, 2, 2), query);
    }

}
