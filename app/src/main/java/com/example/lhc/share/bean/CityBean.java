package com.example.lhc.share.bean;

import java.util.List;

/**
 * Created by a1 on 2017/6/29.
 */
public class CityBean {
    /**
     * name : 城市
     * area : ["东城区","西城区","崇文区","昌平区"]
     */

    private String name;
    private List<String> area;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getArea() {
        return area;
    }

    public void setArea(List<String> area) {
        this.area = area;
    }
    public String getPickerViewText() {
        return this.name;
    }
}
