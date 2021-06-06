package com.dldash.persistence;

import com.dldash.persistence.contracts.Query;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WhereTest extends BaseTest {

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
    public void date() {
        a("SELECT * FROM table WHERE DATE(a) = ?", query().whereDate("a", "").build());
    }

}
