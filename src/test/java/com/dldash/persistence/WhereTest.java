package com.dldash.persistence;

import com.dldash.persistence.contracts.Query;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WhereTest extends BaseTest {

    @Test
    public void where() {
        Query query = query()
                .where("A", 1)
                .orWhere("B", 2)
                .build();

        a("SELECT * FROM table WHERE A = ? OR B = ?", Arrays.asList(1, 2), query);
    }

    @Test
    public void whereIfPresent() {
        Query query = query()
                .whereIfPresent("A", 1)
                .whereIfPresent("B", null)
                .build();

        a("SELECT * FROM table WHERE A = ?", Collections.singletonList(1), query);
    }

    @Test
    public void whereNull() {
        Query query = query()
                .whereNull("UpdatedAt")
                .build();

        a("SELECT * FROM table WHERE UpdatedAt IS NULL", query);
    }

    @Test
    public void whereNotNull() {
        Query query = query()
                .whereNotNull("UpdatedAt")
                .build();

        a("SELECT * FROM table WHERE UpdatedAt IS NOT NULL", query);
    }

    @Test
    public void whereRaw() {
        Query query = query()
                .whereRaw("PublishedAt <= NOW()")
                .build();

        a("SELECT * FROM table WHERE PublishedAt <= NOW()", query);
    }

    @Test
    public void whereBoolean() {
        Query query = query()
                .where("Active", true)
                .build();

        a("SELECT * FROM table WHERE Active = 1", query);
    }

    @Test
    public void bits() {
        Query on = query()
                .whereOnBits("Flags", 32)
                .build();

        a("SELECT * FROM table WHERE Flags & 32 > 0", on);

        Query off = query()
                .whereOffBits("Flags", 32)
                .build();

        a("SELECT * FROM table WHERE Flags & 32 = 0", off);
    }

    @Test
    public void whereIn() {
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
    public void contains() {
        Query query = query()
                .whereContains("Name", "Name")
                .build();

        a("SELECT * FROM table WHERE Name LIKE ?", Collections.singletonList("%Name%"), query);
    }

    @Test
    public void date() {
        a("SELECT * FROM table WHERE DATE(a) = ?", query().whereDate("a", "").build());
    }

    @Test
    public void between() {
        Query query = query()
                .whereBetween("A", 1, 100)
                .orWhereBetween("B", 10, null)
                .build();

        a("SELECT * FROM table WHERE (A >= ? AND A <= ?) OR B >= ?", Arrays.asList(1, 100, 10), query);
    }

}
