# 🍺 Database: Query Builder

## ✨ Running Database Queries

### Retrieving All Rows From A Table

```java
import com.dldash.persistence.contracts.Query;

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

```
Query query = Query.builder()
        .table("users")
        .count()
        .build();
```

## ✨ Select Statements

### Specifying A Select Clause

```
Query query = Query.builder()
        .table("users")
        .select("name", "email as user_email")
        .build();
```

The `distinct` method allows you to force the query to return distinct results:

```
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

```
Query query = Query.builder()
        .table("users")
        .leftJoin("posts", "users.id", "=", "posts.user_id")
        .build();
```

## ✨ Basic Where Clauses

// @TODO