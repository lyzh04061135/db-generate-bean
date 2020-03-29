package com.demo;

import com.demo.annotation.FieldAnnotation;
import com.demo.annotation.TableAnnotation;
import com.demo.bean.ConstraintColumn;
import com.demo.bean.TableColumn;
import com.demo.dao.DbDao;
import com.demo.dao.MdUserDao;
import com.demo.dao.MdUserEntity;
import com.demo.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DbTest {
    @Resource
    DbDao dao;
    @Resource
    MdUserDao userDao;

    @Test
    public void test() throws Exception {
//        log.debug("");
//        log.debug(CommonUtil.convert("db_test"));
//        TableColumn tableColumn=new TableColumn("MDMDB", "md_user");
//        List<TableColumn> tableColumns=dao.getTableColumns(tableColumn);
//        log.debug(CommonUtil.toString(tableColumns));
//        ConstraintColumn constraintColumn=new ConstraintColumn("MDMDB", "md_user");
//        List<ConstraintColumn> constraintColumns=dao.getConstraintColumns(constraintColumn);
//        log.debug(CommonUtil.toString(constraintColumns));

//        MdUserEntity userEntity=new MdUserEntity();
//        userEntity.setUserId(CommonUtil.getUuid());
//        userEntity.setLoginName("generate-name");
//        userEntity.setPassword("123457");
//        userDao.insertMdUserEntity(userEntity);
//        List<MdUserEntity>list=userDao.getPk(userEntity.getUserId());
//        log.debug(CommonUtil.toString(list));
//        userEntity.setRealName("generate-real-name");
//        userDao.updatePk(userEntity);
//        list=userDao.getPk(userEntity.getUserId());
//        log.debug(CommonUtil.toString(list));
//        userDao.removePk(userEntity.getUserId());
//        list=userDao.getPk(userEntity.getUserId());
//        log.debug(CommonUtil.toString(list));

//        Class<?>cls=Class.forName("com.demo.dao.MdUserEntity");
//        Annotation annotation=cls.getAnnotation(TableAnnotation.class);
//        log.debug(((TableAnnotation) annotation).value());
//        Field[] fields=cls.getDeclaredFields();
//        for(Field field:fields) {
//            String fieldName=field.getName();
//            if (fieldName.equals("userId")) {
//                annotation=field.getAnnotation(FieldAnnotation.class);
//                log.debug(String.format("unique: %s, maxLength: %d", ""+((FieldAnnotation) annotation).unique(), ((FieldAnnotation) annotation).maxLength()));
//            }
//        }
    }

}
