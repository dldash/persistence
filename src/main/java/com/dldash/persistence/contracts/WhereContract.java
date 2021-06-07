package com.dldash.persistence.contracts;

import com.dldash.persistence.builders.WhereQuery;
import com.dldash.persistence.enums.Bool;

import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

public interface WhereContract<T> {

    T whereRaw(String sql, List<Object> bindings, Bool bool);

    default T empty() {
        return whereRaw(null);
    }

    default T whereRaw(String sql, List<Object> bindings) {
        return whereRaw(sql, bindings, Bool.AND);
    }

    default T whereRaw(String sql) {
        return whereRaw(sql, null);
    }

    default T orWhereRaw(String sql, List<Object> bindings) {
        return whereRaw(sql, bindings, Bool.OR);
    }

    default T orWhereRaw(String sql) {
        return orWhereRaw(sql, null);
    }

    default T where(String column, String operator, Object value) {
        return whereRaw(column + " " + operator + " ?", Collections.singletonList(value));
    }

    default T where(String column, Object value) {
        return value != null ? where(column, "=", value) : whereNull(column);
    }

    default T whereIfPresent(String column, Object value) {
        return value != null ? where(column, value) : empty();
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

    default T whereOnBits(String column, int bits) {
        return whereRaw(column + " & " + bits + " > 0", null);
    }

    default T whereOffBits(String column, int bits) {
        return whereRaw(column + " & " + bits + " = 0", null);
    }

    default T where(UnaryOperator<WhereQuery> operator, Bool bool) {
        Query query = operator.apply(WhereQuery.builder()).build();
        if (query.sql() != null && !query.sql().isEmpty()) {
            return whereRaw("(" + query.sql() + ")", query.bindings(), bool);
        }
        return empty();
    }

    default T where(UnaryOperator<WhereQuery> operator) {
        return where(operator, Bool.AND);
    }

    default T orWhere(UnaryOperator<WhereQuery> operator) {
        return where(operator, Bool.OR);
    }

}
