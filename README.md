# 💥 Query Builder

[![Maven Central][ico-maven]][url-maven]

## 💡 Usage

* 👉 [Running Database Queries](#-running-database-queries)
* 👉 [Select Statements](#-select-statements)
* 👉 [Raw Expressions](#-raw-expressions)
* 👉 [Joins](#-joins)
* 👉 [Basic Where Clauses](#-basic-where-clauses)
* 👉 [Ordering, Grouping, Limit & Offset](#-ordering-grouping-limit--offset)
* 👉 [Conditional Clauses](#-conditional-clauses)
* 👉 [Insert Statements](#-insert-statements)
* 👉 [Update Statements](#-update-statements)
* 👉 [Delete Statements](#-delete-statements)

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

## ✨ Raw Expressions

```java
Query.raw("NOW()")
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

### Where Clauses

```java
Query query = Query.builder()
        .table("users")
        .where("votes", 100)
        .where("age", ">", 35)
        .build();
```

### Or Where Clauses

```java
Query query = Query.builder()
        .table("users")
        .where("votes", ">", 100)
        .orWhere("name", "John")
        .build();
```

```java
Query query = Query.builder()
        .table("users")
        .where("votes", ">", 100)
        .orWhere(x -> x.where("name", "Abigail").where("votes", ">", 50))
        .build();
```

The example above will produce the following SQL:

```sql
select * from users where votes > 100 or (name = 'Abigail' and votes > 50)
```

### Additional Where Clauses

#### whereBetween / orWhereBetween

```java
Query query = Query.builder()
        .table("users")
        .whereBetween("votes", 1, 100)
        .build();
```

#### whereIn / whereNotIn / orWhereIn / orWhereNotIn

```java
Query query = Query.builder()
        .table("users")
        .whereIn("id", Arrays.asList(1, 2, 3))
        .build();
```

```java
Query query = Query.builder()
        .table("users")
        .whereNotIn("id", Arrays.asList(1, 2, 3))
        .build();
```

#### whereNull / whereNotNull / orWhereNull / orWhereNotNull

```java
Query query = Query.builder()
        .table("users")
        .whereNull("updated_at")
        .whereNotNull("created_at")
        .build();
```

#### whereDate / whereMonth / whereDay / whereYear / whereTime

```java
Query query = Query.builder()
        .table("users")
        .whereDate("created_at", "2016-12-31")
        .whereMonth("created_at", "12")
        .whereDay("created_at", "31")
        .whereYear("created_at", "2016")
        .whereTime("created_at", "11:20:45")
        .build();
```

### Logical Grouping

```java
Query query = Query.builder()
        .table("users")
        .where("name", "=", "John")
        .where(x -> x.where("votes", ">", 100).orWhere("title", "=", "Admin"))
        .build();
```

The example above will produce the following SQL:

```sql
select * from users where name = 'John' and (votes > 100 or title = 'Admin')
```

## ✨ Ordering, Grouping, Limit & Offset

### Ordering

```java
Query query = Query.builder()
        .table("users")
        .orderBy("name", "desc")
        .build();
```

### Grouping

```java
Query query = Query.builder()
        .table("users")
        .groupBy("account_id", "status")
        .build();
```

### Limit & Offset

```java
Query query = Query.builder()
        .table("users")
        .skip(10)
        .take(5)
        .build();
```

Alternatively, you may use the `limit` and `offset` methods.
These methods are functionally equivalent to the `take` and `skip` methods, respectively:

```java
Query query = Query.builder()
        .table("users")
        .offset(10)
        .limit(5)
        .build();
```

## ✨ Conditional Clauses

Sometimes you may want certain query clauses to apply to a query based on another condition.
For instance, you may only want to apply a `where` statement if a given input value is present on the incoming HTTP request.
You may accomplish this using the `when` method:

```java
String role = request.get("role");

Query query = Query.builder()
        .table("users")
        .when(role, (q, value) -> q.where("role_id", value))
        .build();
```

### Where if not null

```java
Query query = Query.builder()
        .table("users")
        .whereIfPresent("A", 1)
        .whereIfPresent("B", null)
        .build();
```

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