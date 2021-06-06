package com.dldash.persistence;

import com.dldash.persistence.builders.SelectQuery;
import com.dldash.persistence.contracts.Query;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SelectQueryTest extends BaseTest {

    private SelectQuery query() {
        return Query.builder().table("table");
    }

    @Test
    public void testSelectAllColumns() {
        a("SELECT * FROM table", query().build());
    }

    @Test
    public void testSelectSpecificColumns() {
        Query query = query()
                .select("Id", "Name")
                .select("CreatedAt")
                .build();

        a("SELECT Id, Name, CreatedAt FROM table", query);
    }

    @Test
    public void testSelectDistinct() {
        Query query = query()
                .distinct()
                .build();

        a("SELECT DISTINCT * FROM table", query);
    }

    @Test
    public void testSelectCalcFoundRows() {
        Query query = query()
                .calcFoundRows()
                .build();

        a("SELECT SQL_CALC_FOUND_ROWS * FROM table", query);
    }

    @Test
    public void testSelectCount() {
        a("SELECT COUNT(*) FROM table", query().count().build());
        a("SELECT COUNT(id) FROM table", query().count("id").build());
    }

    @Test
    public void testTableAlias() {
        a("SELECT * FROM table AS t", query().as("t").build());
    }

    @Test
    public void testWhereNull() {
        Query query = query()
                .whereNull("UpdatedAt")
                .build();

        a("SELECT * FROM table WHERE UpdatedAt IS NULL", query);
    }

    @Test
    public void testWhereNotNull() {
        Query query = query()
                .whereNotNull("UpdatedAt")
                .build();

        a("SELECT * FROM table WHERE UpdatedAt IS NOT NULL", query);
    }

    @Test
    public void testWhereNow() {
        Query query = query()
                .whereRaw("PublishedAt <= NOW()")
                .build();

        a("SELECT * FROM table WHERE PublishedAt <= NOW()", query);
    }

    @Test
    public void testWhereBoolean() {
        Query query = query()
                .where("Active", true)
                .build();

        a("SELECT * FROM table WHERE Active = 1", query);
    }

    @Test
    public void testWhereBits() {
        Query queryBitOn = query().whereBit("Flags", 32).build();
        Query queryBitOff = query().whereNotBit("Flags", 32).build();
        a("SELECT * FROM table WHERE Flags & 32 > 0", queryBitOn);
        a("SELECT * FROM table WHERE Flags & 32 = 0", queryBitOff);
    }

    @Test
    public void testWhereIn() {
        List<Object> ids = Arrays.asList(1, 5, 10);
        Query arrayQuery = query()
                .whereIn("Id", ids)
                .build();

        a("SELECT * FROM table WHERE Id IN (?, ?, ?)", ids, arrayQuery);

        Query listQuery = query()
                .whereIn("Id", Arrays.asList(10, 100))
                .build();

        a("SELECT * FROM table WHERE Id IN (?, ?)", Arrays.asList(10, 100), listQuery);
    }

    @Test
    public void testContains() {
        Query query = query()
                .whereContains("Name", "Name")
                .build();

        a("SELECT * FROM table WHERE Name LIKE ?", Collections.singletonList("%Name%"), query);
    }

    @Test
    public void testGroupBy() {
        Query query = query()
                .select("Id", "MAX(Amount)")
                .groupBy("Id")
                .build();

        a("SELECT Id, MAX(Amount) FROM table GROUP BY Id", query);
    }

    @Test
    public void testOrderBy() {
        Query queryAsc = query().select("Id", "Amount").orderBy("Amount").build();
        Query queryDesc = query().select("Id", "Amount").orderBy("Amount", "desc").build();
        a("SELECT Id, Amount FROM table ORDER BY Amount", queryAsc);
        a("SELECT Id, Amount FROM table ORDER BY Amount DESC", queryDesc);
    }

    @Test
    public void testOffsetAndLimit() {
        a("SELECT * FROM table LIMIT 10, 5", query().offset(10).limit(5).build());
        a("SELECT * FROM table LIMIT 10, 5", query().skip(10).take(5).build());
    }

    @Test
    public void testDate() {
        a("SELECT * FROM table WHERE DATE(a) = ?", query().whereDate("a", "").build());
    }

}
