package io.github.dldash.persistence.contracts;

import io.github.dldash.persistence.builders.WhereQuery;
import io.github.dldash.persistence.enums.Bool;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public interface WhereContract<T> {

    T whereRaw(String sql, List<Object> bindings, Bool bool);

    default T whereRaw(String sql, List<Object> bindings) {
        return whereRaw(sql, bindings, Bool.AND);
    }

    default T orWhereRaw(String sql, List<Object> bindings) {
        return whereRaw(sql, bindings, Bool.OR);
    }

    default T whereRaw(String sql) {
        return whereRaw(sql, null);
    }

    default T orWhereRaw(String sql) {
        return orWhereRaw(sql, null);
    }

    default T where(String column, String operator, Object value) {
        return whereRaw(column + " " + operator + " ?", Collections.singletonList(value));
    }

    default T orWhere(String column, String operator, Object value) {
        return orWhereRaw(column + " " + operator + " ?", Collections.singletonList(value));
    }

    default T where(String column, Object value) {
        return value != null ? where(column, "=", value) : whereNull(column);
    }

    default T orWhere(String column, Object value) {
        return orWhere(column, "=", value);
    }

    default T where(String column, boolean value) {
        return whereRaw(column + " = " + (value ? "1" : "0"));
    }

    default T orWhere(String column, boolean value) {
        return orWhereRaw(column + " = " + (value ? "1" : "0"));
    }

    default T whereNull(String column) {
        return whereRaw(column + " IS NULL");
    }

    default T orWhereNull(String column) {
        return orWhereRaw(column + " IS NULL");
    }

    default T whereNotNull(String column) {
        return whereRaw(column + " IS NOT NULL");
    }

    default T orWhereNotNull(String column) {
        return orWhereRaw(column + " IS NOT NULL");
    }

    default T whereIn(String column, List<Object> values) {
        List<String> bindings = Collections.nCopies(values.size(), "?");
        return whereRaw(column + " IN (" + String.join(", ", bindings) + ")", values);
    }

    default T whereNotIn(String column, List<Object> values) {
        List<String> bindings = Collections.nCopies(values.size(), "?");
        return whereRaw(column + " IN NOT (" + String.join(", ", bindings) + ")", values);
    }

    default T whereBetween(String column, Number from, Number to, Bool bool) {
        if (from != null && to != null) {
            return whereRaw("(" + column + " >= ? AND " + column + " <= ?)", Arrays.asList(from, to), bool);
        }

        if (from != null) {
            return whereRaw(column + " >= ?", Collections.singletonList(from), bool);
        }

        if (to != null) {
            return whereRaw(column + " <= ?", Collections.singletonList(to), bool);
        }

        return whereRaw(null);
    }

    default T whereBetween(String column, Number from, Number to) {
        return whereBetween(column, from, to, Bool.AND);
    }

    default T orWhereBetween(String column, Number from, Number to) {
        return whereBetween(column, from, to, Bool.OR);
    }

    default T whereDate(String column, Object value) {
        return whereRaw("DATE(" + column + ") = ?", Collections.singletonList(value));
    }

    default T whereMonth(String column, Object value) {
        return whereRaw("MONTH(" + column + ") = ?", Collections.singletonList(value));
    }

    default T whereDay(String column, Object value) {
        return whereRaw("DAY(" + column + ") = ?", Collections.singletonList(value));
    }

    default T whereYear(String column, Object value) {
        return whereRaw("YEAR(" + column + ") = ?", Collections.singletonList(value));
    }

    default T whereTime(String column, Object value) {
        return whereRaw("TIME(" + column + ") = ?", Collections.singletonList(value));
    }

    default T whereContains(String column, Object value) {
        return whereRaw(column + " LIKE ?", Collections.singletonList("%" + value + "%"));
    }

    default T whereOnBits(String column, int bits) {
        return whereRaw(column + " & " + bits + " > 0");
    }

    default T whereOffBits(String column, int bits) {
        return whereRaw(column + " & " + bits + " = 0");
    }

    default T where(UnaryOperator<WhereQuery> operator, Bool bool) {
        Query query = operator.apply(WhereQuery.builder()).build();
        if (query.sql() != null && !query.sql().isEmpty()) {
            return whereRaw("(" + query.sql() + ")", query.bindings(), bool);
        }
        return whereRaw(null);
    }

    default T where(UnaryOperator<WhereQuery> operator) {
        return where(operator, Bool.AND);
    }

    default T orWhere(UnaryOperator<WhereQuery> operator) {
        return where(operator, Bool.OR);
    }

    default T whereIfPresent(String column, Object value) {
        return value != null ? where(column, value) : whereRaw(null);
    }

    default T orWhereIfPresent(String column, Object value) {
        return value != null ? orWhere(column, value) : whereRaw(null);
    }

    default T when(boolean condition, Consumer<WhereContract<T>> operator) {
        if (condition) {
            operator.accept(this);
        }
        return whereRaw(null);
    }

    default T when(String o, BiConsumer<WhereContract<T>, String> operator) {
        if (o != null && !o.isEmpty()) {
            operator.accept(this, o);
        }
        return whereRaw(null);
    }

    default T when(Integer o, BiConsumer<WhereContract<T>, Integer> operator) {
        if (o != null && o > 0) {
            operator.accept(this, o);
        }
        return whereRaw(null);
    }

    default <U> T when(U o, BiConsumer<WhereContract<T>, U> operator) {
        if (o != null) {
            operator.accept(this, o);
        }
        return whereRaw(null);
    }

}
