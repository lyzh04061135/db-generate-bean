package com.demo.dao;

import com.demo.bean.ConstraintColumn;
import com.demo.bean.TableColumn;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DbDao {
    List<Map<String, Object>> getTableInfo(Map<String, Object> param);

    List<TableColumn> getTableColumns(TableColumn tableColumn);

    List<ConstraintColumn> getConstraintColumns(ConstraintColumn constraintColumn);

    Integer executeSql(@Param("sql") String sql);

    List<Map<String, Object>> getNameAges();

    List<Map<String, Object>> getDatas();
}
