package com.demo.controller;

import com.demo.sevice.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/db")
@Slf4j
public class DbController {
    @Autowired
    FileService service;

    @RequestMapping(path = "/generateTable")
    public Map<String, Object> generateTable(@RequestParam("package") String pkg,
                                             @RequestParam("entityName") String entityName, String tableName) {
        Map<String, Object> result=new HashMap<>();
        try {
            service.generateTable(pkg, entityName, tableName);
        } catch (Exception e) {
            log.error("", e);
            result.put("msg", "fail");
            result.put("code", "1");
            return result;
        }
        result.put("msg", "success");
        result.put("code", "0");
        return result;
    }

    @RequestMapping(path = "/generate")
    public Map<String, Object> generate(@RequestParam("path") String path, @RequestParam("package") String pkg,
                                        @RequestParam("tableName") String tableName) {
        log.debug(String.format("generate path: %s, package: %s, tableName: %s", path, pkg, tableName));

        Map<String, Object> result=new HashMap<>();
        try {
            service.generate(path, pkg, tableName);
        } catch (Exception e) {
            log.error("", e);
            result.put("msg", "fail");
            result.put("code", "1");
            return result;
        }

        result.put("msg", "success");
        result.put("code", "0");
        return result;
    }
}
