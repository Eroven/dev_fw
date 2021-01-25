package me.zhaotb.admin.controller;


import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaotangbo
 * @date 2021/1/22
 */
@RestController
@RequestMapping("csa/interface")
public class DataController {

    private final Logger logger = LoggerFactory.getLogger(DataController.class);

    @RequestMapping(value = "receiveIdcIspData", method = RequestMethod.POST)
    public Map<String, String> receiveIdcIspData(String parameter, HttpServletRequest request) {
        logger.info(parameter);
        HashMap<String, String> map = new HashMap<>();

        Enumeration<String> headerNames = request.getHeaderNames();
        logger.info("headers: ");
        while (headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            logger.info("{} : {}", headerName, request.getHeader(headerName));
        }
        Map<String, String[]> parameterMap = request.getParameterMap();
        logger.info("parameterMap: ");
        for (String key : parameterMap.keySet()) {
            logger.info("{} : {}", key, Arrays.toString(parameterMap.get(key)));
        }

        try {
            JSONArray jsonArray = JSONArray.parseArray(parameter);
            map.put("status_code", "0");
            map.put("status_text", "success");
        } catch (Exception e) {
            map.put("status_code", "1");
            map.put("status_text", e.getMessage());
        }
        return map;
    }

}
