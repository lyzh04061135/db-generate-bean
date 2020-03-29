package com.demo.dao;

import com.demo.bean.ConstraintColumn;
import com.demo.bean.TableColumn;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DbDao {
    List<TableColumn> getTableColumns(TableColumn tableColumn);

    List<ConstraintColumn> getConstraintColumns(ConstraintColumn constraintColumn);

    Integer executeSql(@Param("sql") String sql);
}
