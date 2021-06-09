# 🍺 Database: Query Builder

## ✨ Running Database Queries

### Retrieving All Rows From A Table

```java
import com.dldash.persistence.contracts.Query;

class UserController {
    public void index() {
        Query query = Query.builder()
                .table("users")
                .build();
    }
}
```

## ✨ Select Statements

...