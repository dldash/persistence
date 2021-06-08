package com.dldash.persistence;

import com.dldash.persistence.contracts.Query;
import org.junit.jupiter.api.Test;

public class SelectQueryTest extends BaseTest {

    @Test
    public void all() {
        a("SELECT * FROM table", query().build());
    }

    @Test
    public void columns() {
        Query query = query()
                .select("Id", "Name")
                .select("CreatedAt")
                .build();

        a("SELECT Id, Name, CreatedAt FROM table", query);
    }

    @Test
    public void distinct() {
        Query query = query()
                .distinct()
                .build();

        a("SELECT DISTINCT * FROM table", query);
    }

    @Test
    public void total() {
        Query query = query()
                .total()
                .build();

        a("SELECT SQL_CALC_FOUND_ROWS * FROM table", query);
    }

    @Test
    public void count() {
        a("SELECT COUNT(*) FROM table", query().count().build());
        a("SELECT COUNT(A) FROM table", query().count("A").build());
    }

    @Test
    public void alias() {
        a("SELECT * FROM table AS t", query().as("t").build());
    }

    @Test
    public void groupBy() {
        Query query = query()
                .select("Id", "MAX(Amount) AS Amount")
                .groupBy("Id")
                .build();

        a("SELECT Id, MAX(Amount) AS Amount FROM table GROUP BY Id", query);
    }

    @Test
    public void orderBy() {
        Query asc = query()
                .select("Id", "Amount")
                .orderBy("Amount")
                .build();

        a("SELECT Id, Amount FROM table ORDER BY Amount", asc);

        Query desc = query()
                .select("Id", "Amount")
                .orderBy("Amount", "desc")
                .build();

        a("SELECT Id, Amount FROM table ORDER BY Amount DESC", desc);
    }

    @Test
    public void limit() {
        a("SELECT * FROM table LIMIT 10, 5", query().offset(10).limit(5).build());
        a("SELECT * FROM table LIMIT 10, 5", query().skip(10).take(5).build());
    }

    @Test
    public void join() {
        Query join = query()
                .join("users", "users.Id", "=", "table.UserId")
                .build();

        a("SELECT * FROM table JOIN users ON users.Id = table.UserId", join);

        Query using = query()
                .join("users", "UserId")
                .build();

        a("SELECT * FROM table JOIN users USING (UserId)", using);

        Query raw = query()
                .join("users ON users.Id = table.UserId AND users.Active = 1")
                .build();

        a("SELECT * FROM table JOIN users ON users.Id = table.UserId AND users.Active = 1", raw);
    }

    @Test
    public void leftJoin() {
        Query join = query()
                .leftJoin("users", "users.Id", "=", "table.UserId")
                .build();

        a("SELECT * FROM table LEFT JOIN users ON users.Id = table.UserId", join);

        Query using = query()
                .leftJoin("users", "UserId")
                .build();

        a("SELECT * FROM table LEFT JOIN users USING (UserId)", using);

        Query raw = query()
                .leftJoin("users ON users.Id = table.UserId AND users.Active = 1")
                .build();

        a("SELECT * FROM table LEFT JOIN users ON users.Id = table.UserId AND users.Active = 1", raw);
    }

}
