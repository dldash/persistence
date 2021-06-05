package com.dldash.persistence.contracts;

import java.util.Collections;
import java.util.List;

public interface WhereContract<T> {

    T whereRaw(String sql, List<Object> bindings, String bool);

    default T whereRaw(String sql, List<Object> bindings) {
        return whereRaw(sql, bindings, " AND ");
    }

    default T whereRaw(String sql) {
        return whereRaw(sql, null);
    }

    default T orWhereRaw(String sql, List<Object> bindings) {
        return whereRaw(sql, bindings, " OR ");
    }

    default T orWhereRaw(String sql) {
        return orWhereRaw(sql, null);
    }

    default T where(String column, String operator, Object value) {
        return whereRaw(column + " " + operator + " ?", Collections.singletonList(value));
    }

    default T where(String column, Object value) {
        return where(column, "=", value);
    }

    default T where(String column, boolean value) {
        return whereRaw(column + " = " + (value ? "1" : "0"));
    }

    default T orWhere(String column, String operator, Object value) {
        return orWhereRaw(column + " " + operator + " ?", Collections.singletonList(value));
    }

    default T orWhere(String column, Object value) {
        return orWhere(column, "=", value);
    }

    default T whereNull(String column) {
        return whereRaw(column + " IS NULL", null);
    }

    default T whereNotNull(String column) {
        return whereRaw(column + " IS NOT NULL", null);
    }

    default T orWhereNull(String column) {
        return orWhereRaw(column + " IS NULL", null);
    }

    default T orWhereNotNull(String column) {
        return orWhereRaw(column + " IS NOT NULL", null);
    }

    default T whereIn(String column, List<Object> values) {
        List<String> bindings = Collections.nCopies(values.size(), "?");
        return whereRaw(column + " IN (" + String.join(", ", bindings) + ")", values);
    }

    default T whereNotIn(String column, List<Object> values) {
        List<String> bindings = Collections.nCopies(values.size(), "?");
        return whereRaw(column + " IN NOT (" + String.join(", ", bindings) + ")", values);
    }

    default T whereDate(String column, Object value) {
        return whereRaw("DATE(" + column + ") = ?", Collections.singletonList(value));
    }

    default T whereContains(String column, Object value) {
        return whereRaw(column + " LIKE ?", Collections.singletonList("%" + value + "%"));
    }

    default T whereBit(String column, int bit) {
        return whereRaw(column + " & " + bit + " > 0", null);
    }

    default T whereNotBit(String column, int bit) {
        return whereRaw(column + " & " + bit + " = 0", null);
    }

}
