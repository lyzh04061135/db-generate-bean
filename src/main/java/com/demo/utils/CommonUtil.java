package com.demo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class CommonUtil implements ApplicationContextAware {
    public static ApplicationContext applicationContext = null;

    public CommonUtil() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CommonUtil.applicationContext = applicationContext;
    }

    public static String toString(Object object) {
        String string = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            string = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("", e);
        }
        return string;
    }

    /**
     * 将table名转换成bean名字
     *
     * @param string
     * @return
     */
    public static String convert(String string) {
        string = string.toLowerCase();
        String[] strings = string.split("_");
        string = "";
        if (strings.length == 1) {
            string = strings[0].substring(0, 1).toUpperCase() + strings[0].substring(1);
        } else {
            for (int i = 0; i < strings.length; i++) {
                string = string + strings[i].substring(0, 1).toUpperCase() + strings[i].substring(1);
            }
        }
        return string;
    }

    /**
     * 将实体属性转换成数据表字段名
     * @param string
     * @return
     */
    public static String convertField(String string) {
        string=string.substring(0,1).toLowerCase()+string.substring(1);
        String s="";
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0; i<string.length(); i++) {
            if (string.charAt(i)>='A' && string.charAt(i)<='Z') {
                s=s+stringBuilder.toString().toLowerCase()+"_";
                stringBuilder=new StringBuilder();
                stringBuilder.append(string.charAt(i));
            }
            else {
                stringBuilder.append(string.charAt(i));
            }
        }
        s+=stringBuilder.toString().toLowerCase();
        return s;
    }

    public static String pathToPackage(String string) {
        if (string == null) {
            return string;
        }
        string = string.replaceAll("\\\\", ".");
        string = string.replaceAll("/", ".");
        return string;
    }

    public static String packageToPath(String string) {
        if (string == null) {
            return string;
        }
        string = string.replaceAll("\\.", "/");
        return string;
    }

    public static String getUuid() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        uuid = uuid.toUpperCase();
        return uuid;
    }

    public static boolean isBlank(String string) {
        if (string == null) {
            return true;
        }
        string = string.trim();
        if (string.length() == 0) {
            return true;
        }
        return false;
    }

    public static Object getBean(String name) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> cls) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(name, cls);
    }

    public static <T> T getBean(Class<T> cls) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(cls);
    }
}
