package com.demo.sevice;

import com.demo.bean.ConstraintColumn;
import com.demo.bean.TableColumn;
import com.demo.dao.DbDao;
import com.demo.utils.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FileService {
    private static final String lineSeparate = "\r\n";
    private static final String db = "MDMDB";
    private static final String jsonMap = "{\"CHAR\":\"String\"," +
            "\"VARCHAR\":\"String\"," +
            "\"LONGVARCHAR\":\"String\"," +
            "\"NUMERIC\":\"java.math.BigDecimal\"," +
            "\"DECIMAL\":\"java.math.BigDecimal\"," +
            "\"BIT\":\"Boolean\"," +
            "\"TINYINT\":\"Byte\"," +
            "\"SMALLINT\":\"Short\"," +
            "\"INTEGER\":\"Integer\"," +
            "\"INT\":\"Integer\"," +
            "\"BIGINT\":\"Long\"," +
            "\"REAL\":\"Float\"," +
            "\"FLOAT\":\"Double\"," +
            "\"DOUBLE\":\"Double\"," +
            "\"BINARY\":\"byte[]\"," +
            "\"VARBINARY\":\"byte[]\"," +
            "\"LONGVARBINARY\":\"byte[]\"," +
            "\"DATE\":\"java.sql.Date\"," +
            "\"TIME\":\"java.sql.Time\"," +
            "\"TIMESTAMP\":\"java.sql.Timestamp\"," +
            "\"DATETIME\":\"java.sql.Timestamp\"," +
            "\"BLOB\":\"java.sql.Blob\"," +
            "\"CLOB\":\"java.sql.Clob\"," +
            "\"STRUCT\":\"java.sql.Struct\"," +
            "\"REF\":\"java.sql.Ref\"," +
            "\"ARRAY\":\"java.sql.Array\"," +
            "\"CURSOR\":\"java.sql.ResultSet\"," +
            "\"TIMESTAMPTZ\":\"java.sql.Timestamp\"," +
            "\"TIMESTAMPLTZ\":\"java.sql.Timestamp\"}";
    private static Map<String, String> typeMap = null;

    static {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            typeMap = objectMapper.readValue(jsonMap, Map.class);
        } catch (IOException e) {
            log.error("", e);
        }
    }

    @Resource
    private DbDao dao;

    public boolean generateTable(String pkg, String entityName, String tableName) throws Exception {
        boolean flag = true;
        Class<?>cls=Class.forName(String.format("%s.%s", pkg, entityName));
        Field[]fields=cls.getDeclaredFields();
        StringBuilder stringBuilder=new StringBuilder();
        String string=String.format("drop TABLE \"%s\".\"%s\"", db, tableName);
        try {
            dao.executeSql(string);
        } catch (Exception e) {
            log.warn("", e);
        }
        string=String.format("CREATE TABLE \"%s\".\"%s\"(", db, tableName);
        stringBuilder.append(string);
        for(int i=0; i<fields.length; i++) {
            Field field=fields[i];
            String columnName=CommonUtil.convertField(field.getName());
            String javaType=field.getType().getName();
            String dbType=getDbType(typeMap, javaType);
            if (javaType.endsWith("String")) {
                if (i==fields.length-1) {
                    string=String.format("\"%s\" %s(32))", columnName, dbType);
                }
                else {
                    string=String.format("\"%s\" %s(32),", columnName, dbType);
                }
            } else {
                if (i==fields.length-1) {
                    string=String.format("\"%s\" %s)", columnName, dbType);
                }
                else {
                    string=String.format("\"%s\" %s,", columnName, dbType);
                }
            }
            stringBuilder.append(string);
        }
        dao.executeSql(stringBuilder.toString());
        return flag;
    }

    public boolean generate(String path, String pkg, String tableName) throws Exception {
        boolean flag = generateEntity(path, pkg, tableName);
        if (!flag) {
            return flag;
        }
        flag = generateMapper(path, pkg, tableName);
        if (!flag) {
            return flag;
        }
        flag = generateDao(path, pkg, tableName);
        if (!flag) {
            return flag;
        }
        return true;
    }

    public boolean generateDao(String path, String pkg, String tableName) throws Exception {
        String pathWithoutFileName = mkdirs(path, CommonUtil.packageToPath(pkg));
        if (pathWithoutFileName == null) {
            return false;
        }
        String fileNameWithoutSuffix = CommonUtil.convert(tableName);
        String fileName = fileNameWithoutSuffix+"Dao";
        String fullPath = createFile(pathWithoutFileName, fileName, ".java");
        if (fullPath == null) {
            return false;
        }

        List<String> strings = new ArrayList<>();
        String string = String.format("package %s;", pkg);
        strings.add(string);
        strings.add("");
        strings.add("import java.util.List;");
        strings.add("");
        string = String.format("public interface %s {", fileNameWithoutSuffix+"Dao");
        strings.add(string);

        TableColumn tableColumn = new TableColumn(db, tableName);
        List<TableColumn> tableColumns = dao.getTableColumns(tableColumn);

        ConstraintColumn constraintColumn = new ConstraintColumn(db, tableName);
        List<ConstraintColumn> constraintColumns = dao.getConstraintColumns(constraintColumn);
        getPk(constraintColumns);
        if (constraintColumns != null) {
            for (int i = 0; i < constraintColumns.size(); i++) {
                ConstraintColumn column = constraintColumns.get(i);
                String upperFieldName = CommonUtil.convert(column.getColumnName());
                String dataType=getDataType(tableColumns, column.getColumnName());
                dataType=convertDataType(dataType);
                String fieldName = upperFieldName.substring(0, 1).toLowerCase() + upperFieldName.substring(1);
                string = String.format("List<%s> getPk(%s %s);", fileNameWithoutSuffix+"Entity", dataType, fieldName);
                strings.add(string);
                strings.add("");

                string = String.format("Integer updatePk(%s %s);", fileNameWithoutSuffix+"Entity",
                        fileNameWithoutSuffix.substring(0, 1).toLowerCase()+fileNameWithoutSuffix.substring(1));
                strings.add(string);
                strings.add("");

                string = String.format("Integer removePk(%s %s);", dataType, fieldName);
                strings.add(string);
                strings.add("");
            }
        }

        string = String.format("Integer insert%s(%s %s);", fileNameWithoutSuffix+"Entity", fileNameWithoutSuffix+"Entity",
                fileNameWithoutSuffix.substring(0, 1).toLowerCase()+fileNameWithoutSuffix.substring(1));
        strings.add(string);
        strings.add("");

        strings.add("}");
        stringsToFile(fullPath, strings);
        return true;
    }

    private boolean generateMapper(String path, String pkg, String tableName) throws Exception {
        String pathWithoutFileName = mkdirs(path, CommonUtil.packageToPath(pkg));
        if (pathWithoutFileName == null) {
            return false;
        }
        String fileNameWithoutSuffix = CommonUtil.convert(tableName);
        String fileName = fileNameWithoutSuffix+"_mapper";
        String fullPath = createFile(pathWithoutFileName, fileName, ".xml");
        if (fullPath == null) {
            return false;
        }

        List<String> strings = new ArrayList<>();
        strings.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        strings.add("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
        String string = String.format("<mapper namespace=\"%s.%sDao\">", pkg, fileNameWithoutSuffix);
        strings.add(string);

        TableColumn tableColumn = new TableColumn(db, tableName);
        List<TableColumn> tableColumns = dao.getTableColumns(tableColumn);
        ConstraintColumn constraintColumn = new ConstraintColumn(db, tableName);
        List<ConstraintColumn> constraintColumns = dao.getConstraintColumns(constraintColumn);
        getPk(constraintColumns);

        for (int i = 0; i < tableColumns.size(); i++) {
            if (i == 0) {
                string = String.format("<resultMap id=\"%s\" type=\"%s.%s\">", fileNameWithoutSuffix+"Entity", pkg, fileNameWithoutSuffix+"Entity");
                strings.add(string);
            }
            TableColumn column = tableColumns.get(i);
            String upperFieldName = CommonUtil.convert(column.getColumnName());
            String fieldName = upperFieldName.substring(0, 1).toLowerCase() + upperFieldName.substring(1);
            string = String.format("<result column=\"%s\" property=\"%s\"></result>", column.getColumnName(), fieldName);
            strings.add(string);
            if (i == tableColumns.size() - 1) {
                strings.add("</resultMap>");
                strings.add("");
            }
        }

        if (constraintColumns != null && constraintColumns.size()>0) {
            String columnName=constraintColumns.get(0).getColumnName();
            String dataType=getDataType(tableColumns, columnName);
            dataType=convertDataType(dataType);
            String upperFieldName = CommonUtil.convert(columnName);
            String fieldName = upperFieldName.substring(0, 1).toLowerCase() + upperFieldName.substring(1);
            string = String.format("<select id=\"getPk\" parameterType=\"object\" resultMap=\"%s\">", fileNameWithoutSuffix+"Entity");
            strings.add(string);
            string = String.format("select * from \"%s\".\"%s\" where %s=#{%s}", db, tableName, columnName, fieldName);
            strings.add(string);
            strings.add("</select>");
            strings.add("");

            string=String.format("<update id=\"updatePk\" parameterType=\"%s.%s\">", pkg, fileNameWithoutSuffix+"Entity");
            strings.add(string);
            string=String.format("update \"%s\".\"%s\" set", db, tableName);
            strings.add(string);
            for(int j=0; j<tableColumns.size(); j++) {
                TableColumn tableColumn2 = tableColumns.get(j);
                if (tableColumn2.getColumnName().equals(columnName)) {
                    continue;
                }
                String upperFieldName2 = CommonUtil.convert(tableColumn2.getColumnName());
                String fieldName2 = upperFieldName2.substring(0, 1).toLowerCase() + upperFieldName2.substring(1);
                String dataType2 = typeMap.get(tableColumn2.getDataType());
                if (dataType2.endsWith("String")) {
                    string = String.format("<if test=\"%s==null\">%s=null</if>", fieldName2, tableColumn2.getColumnName());
                    strings.add(string);
                    string = String.format("<if test=\"%s==''.toString()\">%s=''</if>", fieldName2, tableColumn2.getColumnName());
                    strings.add(string);
                    if (j==tableColumns.size()-2) {
                        if (tableColumns.get(tableColumns.size()-1).getColumnName().equals(columnName)) {
                            string = String.format("<if test=\"%s!=null and %s!=''.toString()\">%s=#{%s}</if>",
                                    fieldName2, fieldName2, tableColumn2.getColumnName(), fieldName2);
                            strings.add(string);
                        }
                        else {
                            string = String.format("<if test=\"%s!=null and %s!=''.toString()\">%s=#{%s}</if>,",
                                    fieldName2, fieldName2, tableColumn2.getColumnName(), fieldName2);
                            strings.add(string);
                        }
                    } else if (j==tableColumns.size()-1) {
                        string = String.format("<if test=\"%s!=null and %s!=''.toString()\">%s=#{%s}</if>",
                                fieldName2, fieldName2, tableColumn2.getColumnName(), fieldName2);
                        strings.add(string);
                    }
                    else {
                        string = String.format("<if test=\"%s!=null and %s!=''.toString()\">%s=#{%s}</if>,",
                                fieldName2, fieldName2, tableColumn2.getColumnName(), fieldName2);
                        strings.add(string);
                    }
                }
                else {
                    string = String.format("<if test=\"%s==null\">%s=null</if>", fieldName2, tableColumn2.getColumnName());
                    strings.add(string);
                    if (j==tableColumns.size()-2) {
                        if (tableColumns.get(tableColumns.size()-1).getColumnName().equals(columnName)) {
                            string = String.format("<if test=\"%s!=null\">%s=#{%s}</if>",
                                    fieldName2, tableColumn2.getColumnName(), fieldName2);
                            strings.add(string);
                        }
                        else {
                            string = String.format("<if test=\"%s!=null\">%s=#{%s}</if>,",
                                    fieldName2, tableColumn2.getColumnName(), fieldName2);
                            strings.add(string);
                        }
                    } else if (j==tableColumns.size()-1) {
                        string = String.format("<if test=\"%s!=null\">%s=#{%s}</if>",
                                fieldName2, tableColumn2.getColumnName(), fieldName2);
                        strings.add(string);
                    }
                    else {
                        string = String.format("<if test=\"%s!=null\">%s=#{%s}</if>,",
                                fieldName2, tableColumn2.getColumnName(), fieldName2);
                        strings.add(string);
                    }
                }
            }
            string=String.format("where %s=#{%s}", columnName, fieldName);
            strings.add(string);
            strings.add("</update>");
            strings.add("");

            strings.add("<delete id=\"removePk\" parameterType=\"object\">");
            string=String.format("delete from \"%s\".\"%s\" where %s=#{%s}", db, tableName, columnName, fieldName);
            strings.add(string);
            strings.add("</delete>");
            strings.add("");
        }

        string=String.format("<insert id=\"insert%s\" parameterType=\"%s.%s\">", fileNameWithoutSuffix+"Entity", pkg, fileNameWithoutSuffix+"Entity");
        strings.add(string);
        StringBuilder stringBuilder=new StringBuilder();
        string=String.format(" insert into \"%s\".\"%s\"(", db, tableName);
        stringBuilder.append(string);
        for (int i = 0; i < tableColumns.size(); i++) {
            TableColumn column = tableColumns.get(i);
            if (i==tableColumns.size()-1) {
                stringBuilder.append(column.getColumnName());
            }
            else {
                stringBuilder.append(column.getColumnName()+",");
            }
        }
        stringBuilder.append(")values(");
        for (int i = 0; i < tableColumns.size(); i++) {
            TableColumn column = tableColumns.get(i);
            String dataType=getDataType(tableColumns, column.getColumnName());
            dataType=convertDataType(dataType);
            String upperFieldName = CommonUtil.convert(column.getColumnName());
            String fieldName = upperFieldName.substring(0, 1).toLowerCase() + upperFieldName.substring(1);
            if (dataType.equals("String")) {
                string = String.format("<if test=\"%s==null\">null</if>", fieldName);
                stringBuilder.append(string);
                string = String.format("<if test=\"%s==''.toString()\">''</if>", fieldName);
                stringBuilder.append(string);
                if (i==tableColumns.size()-1) {
                    string = String.format("<if test=\"%s!=null and %s!=''.toString()\">#{%s}</if>)",
                            fieldName, fieldName, fieldName);
                    stringBuilder.append(string);
                }
                else {
                    string = String.format("<if test=\"%s!=null and %s!=''.toString()\">#{%s}</if>,",
                            fieldName, fieldName, fieldName);
                    stringBuilder.append(string);
                }
            }
            else {
                string = String.format("<if test=\"%s==null\">null</if>", fieldName);
                stringBuilder.append(string);
                if (i==tableColumns.size()-1) {
                    string = String.format("<if test=\"%s!=null\">#{%s}</if>)",
                            fieldName, fieldName);
                    stringBuilder.append(string);
                }
                else {
                    string = String.format("<if test=\"%s!=null\">#{%s}</if>,",
                            fieldName, fieldName);
                    stringBuilder.append(string);
                }
            }
        }
        strings.add(stringBuilder.toString());
        strings.add("</insert>");
        strings.add("");

        strings.add("</mapper>");
        stringsToFile(fullPath, strings);
        return true;
    }

    private boolean generateEntity(String path, String pkg, String tableName) throws Exception {
        String pathWithoutFileName = mkdirs(path, CommonUtil.packageToPath(pkg));
        if (pathWithoutFileName == null) {
            return false;
        }
        String fileNameWithoutSuffix = CommonUtil.convert(tableName);
        String fileName = fileNameWithoutSuffix+"Entity";
        String fullPath = createFile(pathWithoutFileName, fileName, ".java");
        if (fullPath == null) {
            return false;
        }

        List<String> strings = new ArrayList<>();
        String string = String.format("package %s;", pkg);
        strings.add(string);
        strings.add("");
        string = String.format("public class %s {", fileName);
        strings.add(string);

        List<String> fieldList = new ArrayList<>();
        List<String> methodList = new ArrayList<>();
        List<String> bodyList = new ArrayList<>();
        List<String> defaultList = new ArrayList<>();
        List<String> stringList = new ArrayList<>();

        stringList.add("public String toString() {");
        stringList.add("StringBuilder stringBuilder = new StringBuilder();");

        string = String.format("public %s() {", fileName);
        defaultList.add(string);
        defaultList.add("}");

        TableColumn tableColumn = new TableColumn(db, tableName);
        List<TableColumn> tableColumns = dao.getTableColumns(tableColumn);

        StringBuilder paramBuilder = new StringBuilder(String.format("public %s(", fileName));
        for (int i = 0; i < tableColumns.size(); i++) {
            TableColumn column = tableColumns.get(i);
            String upperFieldName = CommonUtil.convert(column.getColumnName());
            String fieldName = upperFieldName.substring(0, 1).toLowerCase() + upperFieldName.substring(1);
            String dataType = typeMap.get(column.getDataType());
            dataType=convertDataType(dataType);
            string = String.format("private %s %s;", dataType, fieldName);
            fieldList.add(string);

            string = String.format("public %s get%s() {", dataType, upperFieldName);
            methodList.add(string);
            string = String.format("return %s;", fieldName);
            methodList.add(string);
            methodList.add("}");
            methodList.add("");

            string = String.format("public void set%s(%s %s) {", upperFieldName, dataType, fieldName);
            methodList.add(string);
            string = String.format("this.%s = %s;", fieldName, fieldName);
            methodList.add(string);
            methodList.add("}");
            if (i != tableColumns.size() - 1) {
                methodList.add("");
                paramBuilder.append(String.format("%s %s, ", dataType, fieldName));
            }
            else {
                paramBuilder.append(String.format("%s %s", dataType, fieldName));
            }

            if(i==0) {
                stringList.add("stringBuilder.append(\"{\");");
            }
            string = String.format("stringBuilder.append(\"\\\"%s\\\"\");", fieldName);
            stringList.add(string);
            stringList.add("stringBuilder.append(\":\");");
            string=String.format("if(%s!=null) {", fieldName);
            stringList.add(string);
            stringList.add("stringBuilder.append(\"\\\"\");");
            string = String.format(" stringBuilder.append(%s);", fieldName);
            stringList.add(string);
            stringList.add("} else {");
            stringList.add(" stringBuilder.append(\"null\");");
            stringList.add("}");
            if (i == tableColumns.size() - 1) {
                string=String.format("if(%s!=null) {", fieldName);
                stringList.add(string);
                stringList.add("stringBuilder.append(\"\\\"\");");
                stringList.add("}");
                stringList.add("stringBuilder.append(\"}\");");
                stringList.add("return stringBuilder.toString();");
                stringList.add("}");
                log.debug(CommonUtil.toString(stringList));
            } else {
                string=String.format("if(%s!=null) {", fieldName);
                stringList.add(string);
                stringList.add("stringBuilder.append(\"\\\",\");");
                stringList.add("} else {");
                stringList.add(" stringBuilder.append(\",\");");
                stringList.add("}");
            }

            string = String.format("this.%s=%s;", fieldName, fieldName);
            bodyList.add(string);
        }
        paramBuilder.append(") {");
        bodyList.add("}");

        for (String s : fieldList) {
            strings.add(s);
        }
        strings.add("");

        for (String s : defaultList) {
            strings.add(s);
        }
        strings.add("");

        strings.add(paramBuilder.toString());
        for (String s : bodyList) {
            strings.add(s);
        }
        strings.add("");

        for (String s : methodList) {
            strings.add(s);
        }
        strings.add("");

        for (String s : stringList) {
            strings.add(s);
        }
        strings.add("");

        strings.add("}");
        stringsToFile(fullPath, strings);
        return true;
    }

    private String convertDataType(String dataType) {
        if (dataType==null) {
            log.error("");
            return "unknownDataType";
        }
        int index=dataType.lastIndexOf(".");
        if (index>-1) {
            String string=dataType.substring(0, index);
            if (string.equals("java.lang")) {
                dataType=dataType.substring(index+1);
            }
        }
        return dataType;
    }

    private String getDbType(Map<String, String>typeMap, String javaType) {
        int index=javaType.lastIndexOf(".");
        if (index>0) {
            javaType=javaType.substring(index+1);
        }
        String dbType=null;
        for(Map.Entry<String, String>entry: typeMap.entrySet()) {
            String value=entry.getValue();
            index=value.lastIndexOf(".");
            if (index>-1) {
                value=value.substring(index+1);
            }
            if (value.equals(javaType)) {
                dbType=entry.getKey();
                break;
            }
        }
        return dbType;
    }

    private String getDataType(List<TableColumn> tableColumns, String columnName) {
        for (int i = 0; i < tableColumns.size(); i++) {
            TableColumn column = tableColumns.get(i);
            if (column.getColumnName().equals(columnName)) {
                return typeMap.get(column.getDataType());
            }
        }
        return null;
    }

    private void getPk(List<ConstraintColumn> constraintColumns) {
//        if (constraintColumns != null) {
//            Iterator<ConstraintColumn> iterator = constraintColumns.iterator();
//            while (iterator.hasNext()) {
//                ConstraintColumn column = iterator.next();
//                if (!column.getConstraintName().toLowerCase().endsWith("_pk")) {
//                    iterator.remove();
//                }
//            }
//        }
    }

    private Map<String, String> getMap(String jsonString) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = objectMapper.readValue(jsonString, Map.class);
        return map;
    }

    private List<String> fileToStrings(String path) {
        List<String> list = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(path);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "utf8");
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        ) {
            while (true) {
                String line = bufferedReader.readLine();
                list.add(line);
            }
        } catch (Exception e) {
            log.debug("", e);
        }
        return list;
    }

    private boolean stringsToFile(String path, List<String> list) {
        boolean flag = false;
        if (list == null) {
            return flag;
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(path);
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "utf8");
             BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)
        ) {
            for (String line : list) {
                bufferedWriter.write(line);
                bufferedWriter.write(lineSeparate);
            }
            flag = true;
        } catch (Exception e) {
            log.debug("", e);
        }
        return flag;
    }

    private String mkdirs(String path, String pkg) {
        path = path.replaceAll("\\\\", "/");
        String fullPath = path + "/" + CommonUtil.packageToPath(pkg);
        File file = new File(fullPath);
        if (file.exists()) {
            if (!file.isDirectory()) {
                log.error(String.format("%s is not directory!", fullPath));
            } else {
                return fullPath;
            }
        } else {
            file.mkdirs();
            return fullPath;
        }
        return null;
    }

    private String createFile(String path, String fileName, String suffix) {
        String fullPath = path + "/" + fileName + suffix;
        File file = new File(fullPath);
        if (file.exists()) {
            if (file.isDirectory()) {
                log.error(String.format("%s is not file!", fullPath));
            } else {
                return fullPath;
            }
        } else {
            try {
                file.createNewFile();
                return fullPath;
            } catch (IOException e) {
                log.error("", e);
            }
        }
        return null;
    }

}
