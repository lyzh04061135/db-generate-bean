package com.demo.bean;

public class ConstraintColumn {
    private String owner;
    private String tableName;
    private String columnName;
    private String constraintName;

    public ConstraintColumn() {
    }

    public ConstraintColumn(String owner, String tableName) {
        this.owner = owner;
        this.tableName = tableName;
    }

    public ConstraintColumn(String owner, String tableName, String columnName, String constraintName) {
        this.owner = owner;
        this.tableName = tableName;
        this.columnName = columnName;
        this.constraintName=constraintName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getConstraintName() {
        return constraintName;
    }

    public void setConstraintName(String constraintName) {
        this.constraintName = constraintName;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"owner\"");
        stringBuilder.append(":");
        stringBuilder.append("\"");
        stringBuilder.append(owner);
        stringBuilder.append("\",");
        stringBuilder.append("\"tableName\"");
        stringBuilder.append(":");
        stringBuilder.append("\"");
        stringBuilder.append(tableName);
        stringBuilder.append("\",");
        stringBuilder.append("\"columnName\"");
        stringBuilder.append(":");
        stringBuilder.append("\"");
        stringBuilder.append(columnName);
        stringBuilder.append("\",");
        stringBuilder.append("\"constraintName\"");
        stringBuilder.append(":");
        stringBuilder.append("\"");
        stringBuilder.append(constraintName);
        stringBuilder.append("\"");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
