package com.demo.bean;

public class TableColumn {
    private String owner;
    private String tableName;
    private String columnName;
    private String dataType;

    public TableColumn() {
    }

    public TableColumn(String owner, String tableName) {
        this.owner = owner;
        this.tableName = tableName;
    }

    public TableColumn(String owner, String tableName, String columnName, String dataType) {
        this.owner = owner;
        this.tableName = tableName;
        this.columnName = columnName;
        this.dataType = dataType;
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

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
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
        stringBuilder.append("\"dataType\"");
        stringBuilder.append(":");
        stringBuilder.append("\"");
        stringBuilder.append(dataType);
        stringBuilder.append("\"");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
