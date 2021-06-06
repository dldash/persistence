package com.dldash.persistence.builders;

import com.dldash.persistence.contracts.Query;
import com.dldash.persistence.contracts.BuilderContract;
import com.dldash.persistence.contracts.WhereContract;
import com.dldash.persistence.objects.ConcreteQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SelectQuery implements BuilderContract, WhereContract<SelectQuery> {

    private final List<String> select = new ArrayList<>();

    private boolean distinct = false;
    private boolean calcFoundRows = false;

    private String table;
    private String as;

    private final List<String> joins = new ArrayList<>();

    private final StringBuilder wheres = new StringBuilder();
    private final List<Object> whereBindings = new ArrayList<>();

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

    public SelectQuery max(String column) {
        this.select.add("MAX(" + column + ")");
        return this;
    }

    public SelectQuery distinct() {
        this.distinct = true;
        return this;
    }

    public SelectQuery calcFoundRows() {
        this.calcFoundRows = true;
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

    public SelectQuery join(String table, String condition) {
        this.joins.add("JOIN " + table + " ON " + condition);
        return this;
    }

    public SelectQuery leftJoin(String table, String first, String operator, String second) {
        this.joins.add("LEFT JOIN " + table + " ON " + first + " " + operator + " " + second);
        return this;
    }

    public SelectQuery leftJoin(String table, String condition) {
        this.joins.add("LEFT JOIN " + table + " ON " + condition);
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

    private String selectClause() {
        StringBuilder sql = new StringBuilder(" SELECT ");

        if (calcFoundRows) {
            sql.append(" SQL_CALC_FOUND_ROWS ");
        }

        if (distinct) {
            sql.append(" DISTINCT ");
        }

        sql.append(select.isEmpty() ? "*" : String.join(", ", select));

        return sql.toString();
    }

    private String fromClause() {
        return " FROM " + table + (as != null ? " AS " + as : "");
    }

    private String joinClause() {
        return " " + String.join(" ", joins) + " ";
    }

    private String whereClause() {
        return wheres.toString();
    }

    private String groupByClause() {
        if (groupBy.isEmpty()) {
            return "";
        }
        return " GROUP BY " + String.join(", ", groupBy);
    }

    private String orderByClause() {
        if (orderBy == null) {
            return "";
        }
        return " ORDER BY " + orderBy + (descending ? " DESC " : "");
    }

    private String limitClause() {
        if (limit <= 0) {
            return "";
        }
        return " LIMIT " + (offset <= 0 ? "" : offset + ", ") + limit;
    }

    private String sql() {
        return "" +
                selectClause() +
                fromClause() +
                joinClause() +
                whereClause() +
                groupByClause() +
                orderByClause() +
                limitClause();
    }

    private List<Object> bindings() {
        return whereBindings;
    }

    @Override
    public SelectQuery whereRaw(String sql, List<Object> bindings, String bool) {
        wheres.append(wheres.length() > 0 ? bool : " WHERE ").append(sql);
        if (bindings != null) {
            this.whereBindings.addAll(bindings);
        }
        return this;
    }

    @Override
    public Query build() {
        return new ConcreteQuery(sql(), bindings());
    }

}
