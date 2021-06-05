package com.dldash.persistence;

import com.dldash.persistence.builders.DatabaseQuery;
import com.dldash.persistence.contracts.Query;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersistenceTest {

    private String cleanup(Query query) {
        return query.sql().replaceAll("\\s+", " ").trim();
    }

    private DatabaseQuery table() {
        return DatabaseQuery.builder().table("table");
    }

    @Test
    public void testSelectAllColumns() {
        Query query = table().build();
        assertEquals("SELECT * FROM table", cleanup(query));
    }

    @Test
    public void testSelectSpecificColumns() {
        Query query = table().select("Id", "Name").select("CreatedAt").build();
        assertEquals("SELECT Id, Name, CreatedAt FROM table", cleanup(query));
    }

    @Test
    public void testSelectDistinct() {
        Query query = table().distinct().build();
        assertEquals("SELECT DISTINCT * FROM table", cleanup(query));
    }

    @Test
    public void testSelectCalcFoundRows() {
        Query query = table().calcFoundRows().build();
        assertEquals("SELECT SQL_CALC_FOUND_ROWS * FROM table", cleanup(query));
    }

    @Test
    public void testSelectCount() {
        assertEquals("SELECT COUNT(*) FROM table", cleanup(table().count().build()));
        assertEquals("SELECT COUNT(id) FROM table", cleanup(table().count("id").build()));
    }

    @Test
    public void testTableAlias() {
        Query query = table().as("t").build();
        assertEquals("SELECT * FROM table AS t", cleanup(query));
    }

    @Test
    public void testWhereNull() {
        Query query = DatabaseQuery.builder()
                .table("table")
                .whereNull("UpdatedAt")
                .build();
        assertEquals("SELECT * FROM table WHERE UpdatedAt IS NULL", cleanup(query));
    }

    @Test
    public void testWhereNotNull() {
        Query query = DatabaseQuery.builder()
                .table("table")
                .whereNotNull("UpdatedAt")
                .build();
        assertEquals("SELECT * FROM table WHERE UpdatedAt IS NOT NULL", cleanup(query));
    }

    @Test
    public void testWhereNow() {
        Query query = DatabaseQuery.builder()
                .table("table")
                .whereRaw("PublishedAt <= NOW()")
                .build();
        assertEquals("SELECT * FROM table WHERE PublishedAt <= NOW()", cleanup(query));
    }

    @Test
    public void testWhereBoolean() {
        Query query = DatabaseQuery.builder()
                .table("table")
                .where("Active", true)
                .build();
        assertEquals("SELECT * FROM table WHERE Active = 1", cleanup(query));
    }

    @Test
    public void testWhereBits() {
        Query queryBitOn = table().whereBit("Flags", 32).build();
        Query queryBitOff = table().whereNotBit("Flags", 32).build();
        assertEquals("SELECT * FROM table WHERE Flags & 32 > 0", cleanup(queryBitOn));
        assertEquals("SELECT * FROM table WHERE Flags & 32 = 0", cleanup(queryBitOff));
    }

    @Test
    public void testWhereIn() {
        List<Object> ids = Arrays.asList(1, 5, 10);
        Query arrayQuery = DatabaseQuery.builder()
                .table("table")
                .whereIn("Id", ids)
                .build();
        assertEquals("SELECT * FROM table WHERE Id IN (?, ?, ?)", cleanup(arrayQuery));
        assertEquals(ids, arrayQuery.bindings());

        Query listQuery = DatabaseQuery.builder()
                .table("table")
                .whereIn("Id", Arrays.asList(10, 100))
                .build();
        assertEquals("SELECT * FROM table WHERE Id IN (?, ?)", cleanup(listQuery));
        assertEquals(Arrays.asList(10, 100), listQuery.bindings());
    }

    @Test
    public void testContains() {
        Query query = table().whereContains("Name", "Name").build();
        assertEquals("SELECT * FROM table WHERE Name LIKE ?", cleanup(query));
        assertEquals(Collections.singletonList("%Name%"), query.bindings());
    }

    @Test
    public void testGroupBy() {
        Query query = DatabaseQuery.builder()
                .table("table")
                .select("Id", "MAX(Amount)")
                .groupBy("Id")
                .build();
        assertEquals("SELECT Id, MAX(Amount) FROM table GROUP BY Id", cleanup(query));
    }

    @Test
    public void testOrderBy() {
        Query queryAsc = table().select("Id", "Amount").orderBy("Amount").build();
        Query queryDesc = table().select("Id", "Amount").orderBy("Amount", "desc").build();
        assertEquals("SELECT Id, Amount FROM table ORDER BY Amount", cleanup(queryAsc));
        assertEquals("SELECT Id, Amount FROM table ORDER BY Amount DESC", cleanup(queryDesc));
    }

    @Test
    public void testOffsetAndLimit() {
        assertEquals("SELECT * FROM table LIMIT 10, 5", cleanup(table().offset(10).limit(5).build()));
        assertEquals("SELECT * FROM table LIMIT 10, 5", cleanup(table().skip(10).take(5).build()));
    }

    @Test
    public void testDate() {
        assertEquals("SELECT * FROM table WHERE DATE(a) = ?", cleanup(table().whereDate("a", "").build()));
    }

}
