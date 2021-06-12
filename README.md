# 💥 Query Builder

[![Maven Central][ico-maven]][url-maven]

## 💡 Usage

* 👉 [Running Database Queries](#-running-database-queries)
* 👉 [Select Statements](#-select-statements)

## ✨ Running Database Queries

### Retrieving All Rows From A Table

```java
import io.github.dldash.persistence.contracts.Query;

class UserController {
    public void index() {
        Query query = Query.builder()
                .table("users")
                .where("name", "John")
                .build();

        System.out.println(query.sql());
        System.out.println(query.bindings());
    }
}
```

### Aggregates

```java
Query query = Query.builder()
        .table("users")
        .count()
        .build();
```

## ✨ Select Statements

### Specifying A Select Clause

```java
Query query = Query.builder()
        .table("users")
        .select("name", "email as user_email")
        .build();
```

The `distinct` method allows you to force the query to return distinct results:

```java
Query query = Query.builder()
        .table("users")
        .distinct()
        .build();
```

## ✨ Joins

### Inner Join Clause

```java
Query query = Query.builder()
        .table("users")
        .join("contacts", "users.id", "=", "contacts.user_id")
        .join("orders", "users.id", "=", "orders.user_id'")
        .select("users.*", "contacts.phone", "orders.price")
        .build();
```

### Left Join Clause

```java
Query query = Query.builder()
        .table("users")
        .leftJoin("posts", "users.id", "=", "posts.user_id")
        .build();
```

## ✨ Basic Where Clauses

// @TODO

## ✨ Insert Statements

```java
import io.github.dldash.persistence.builders.InsertQuery;

Query query = InsertQuery.builder()
        .table("users")
        .insert("email", "kayla@example.com")
        .insert("votes", 0)
        .build();
```

The `ignore()` method will ignore duplicate record errors while inserting records into the database:

```java
Query query = InsertQuery.builder()
        .table("users")
        .ignore()
        .insert("id", 1)
        .insert("email", "archer@example.com")
        .build();
```

### Upserts (on duplicate key update)

```java
Query query = InsertQuery.builder()
        .table("users")
        .insert("email", "kayla@example.com")
        .insertOrUpdate("votes", 10)
        .insertOrUpdate("updated_at", Query.raw("NOW()"))
        .build();
```

## ✨ Update Statements

```java
import io.github.dldash.persistence.builders.UpdateQuery;

Query query = UpdateQuery.builder()
        .table("users")
        .update("votes", 1)
        .where("id", 1)
        .build();
```

### Update if not null

```java
Query query = UpdateQuery.builder()
        .table("users")
        .update("votes", 1)
        .updateIfPresent("name", null)
        .where("id", 1)
        .build();
```

## ✨ Delete Statements

```java
import io.github.dldash.persistence.builders.DeleteQuery;

Query query = DeleteQuery.builder()
        .table("users")
        .where("votes", ">", 100)
        .build();
```

[ico-maven]: https://img.shields.io/maven-central/v/io.github.dldash/persistence.svg?label=Maven%20Central&style=flat-square
[url-maven]: https://search.maven.org/search?q=g:%22io.github.dldash%22%20AND%20a:%22persistence%22