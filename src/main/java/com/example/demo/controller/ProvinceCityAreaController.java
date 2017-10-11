package com.example.demo.controller;


import com.example.demo.utils.ReverseGeocodingUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author yongsheng.he
 * @describe
 * @date 2017/10/10 11:11
 */
@RestController
public class ProvinceCityAreaController {

    @RequestMapping("/getProvinceCityArea")
    public Map<String, String> getProvinceCityArea(@RequestParam(value = "longitude") String longitude,
                                                   @RequestParam(value = "latitude") String latitude) {
        try {
            String str = ReverseGeocodingUtil.reverseGeocoding(longitude, latitude);
            Map<String, String> map = ReverseGeocodingUtil.getProvinceCityArea(str);
            return map;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
