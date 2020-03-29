package com.demo.dao;

import java.util.List;

public interface MdUserDao {
List<MdUserEntity> getPk(java.lang.String userId);

Integer updatePk(MdUserEntity mdUser);

Integer removePk(java.lang.String userId);

Integer insertMdUserEntity(MdUserEntity mdUser);

}
