package com.dldash.persistence.builders;

import com.dldash.persistence.contracts.BuilderContract;
import com.dldash.persistence.contracts.Query;
import com.dldash.persistence.contracts.WhereContract;
import com.dldash.persistence.enums.Bool;
import com.dldash.persistence.objects.ConcreteQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SelectQuery implements BuilderContract, WhereContract<SelectQuery> {

    private final List<String> select = new ArrayList<>();

    private boolean distinct = false;
    private boolean total = false;

    private String table;
    private String as;

    private final List<String> joins = new ArrayList<>();

    private final WhereQuery where = WhereQuery.builder();

    private final List<String> groupBy = new ArrayList<>();

    private String orderBy;
    private boolean descending;

    private int limit;
    private int offset;

    public static SelectQuery builder() {
        return new SelectQuery();
    }

    private SelectQuery() {

    }

    public SelectQuery select(String... select) {
        this.select.addAll(Arrays.asList(select));
        return this;
    }

    public SelectQuery count(String column) {
        this.select.add("COUNT(" + column + ")");
        return this;
    }

    public SelectQuery count() {
        return count("*");
    }

    public SelectQuery distinct() {
        this.distinct = true;
        return this;
    }

    public SelectQuery total() {
        this.total = true;
        return this;
    }

    public SelectQuery table(String table) {
        this.table = table;
        return this;
    }

    public SelectQuery as(String as) {
        this.as = as;
        return this;
    }

    public SelectQuery join(String table, String first, String operator, String second) {
        this.joins.add("JOIN " + table + " ON " + first + " " + operator + " " + second);
        return this;
    }

    public SelectQuery join(String table, String column) {
        this.joins.add("JOIN " + table + " USING (" + column + ")");
        return this;
    }

    public SelectQuery join(String raw) {
        this.joins.add("JOIN " + raw);
        return this;
    }

    public SelectQuery leftJoin(String table, String first, String operator, String second) {
        this.joins.add("LEFT JOIN " + table + " ON " + first + " " + operator + " " + second);
        return this;
    }

    public SelectQuery leftJoin(String table, String column) {
        this.joins.add("LEFT JOIN " + table + " USING (" + column + ")");
        return this;
    }

    public SelectQuery leftJoin(String raw) {
        this.joins.add("LEFT JOIN " + raw);
        return this;
    }

    public SelectQuery groupBy(String... columns) {
        this.groupBy.addAll(Arrays.asList(columns));
        return this;
    }

    public SelectQuery orderBy(String column) {
        return orderBy(column, "asc");
    }

    public SelectQuery orderBy(String column, String direction) {
        this.orderBy = column;
        this.descending = direction.equalsIgnoreCase("desc");
        return this;
    }

    public SelectQuery limit(int limit) {
        this.limit = limit;
        return this;
    }

    public SelectQuery take(int take) {
        return limit(take);
    }

    public SelectQuery offset(int offset) {
        this.offset = offset;
        return this;
    }

    public SelectQuery skip(int skip) {
        return offset(skip);
    }

    private String select() {
        StringBuilder sql = new StringBuilder(" SELECT ");

        if (total) {
            sql.append(" SQL_CALC_FOUND_ROWS ");
        }

        if (distinct) {
            sql.append(" DISTINCT ");
        }

        sql.append(select.isEmpty() ? "*" : String.join(", ", select));

        return sql.toString();
    }

    private String from() {
        return " FROM " + table + (as != null ? " AS " + as : "");
    }

    private String join() {
        return " " + String.join(" ", joins) + " ";
    }

    private String where() {
        Query query = where.build();

        return !query.sql().isEmpty() ? " WHERE " + query.sql() : "";
    }

    private String groupBy() {
        if (groupBy.isEmpty()) {
            return "";
        }
        return " GROUP BY " + String.join(", ", groupBy);
    }

    private String orderBy() {
        if (orderBy == null) {
            return "";
        }
        return " ORDER BY " + orderBy + (descending ? " DESC " : "");
    }

    private String limit() {
        if (limit <= 0) {
            return "";
        }
        return " LIMIT " + (offset <= 0 ? "" : offset + ", ") + limit;
    }

    private String sql() {
        return select() + from() + join() + where() + groupBy() + orderBy() + limit();
    }

    private List<Object> bindings() {
        return where.build().bindings();
    }

    @Override
    public SelectQuery whereRaw(String sql, List<Object> bindings, Bool bool) {
        where.whereRaw(sql, bindings, bool);
        return this;
    }

    @Override
    public Query build() {
        return new ConcreteQuery(sql(), bindings());
    }

}
