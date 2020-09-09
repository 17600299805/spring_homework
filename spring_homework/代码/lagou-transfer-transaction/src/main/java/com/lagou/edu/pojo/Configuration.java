package com.lagou.edu.pojo;

/**
 * @authorAdministrator
 * @date 2020/9/820:02
 * @description
 */
public class Configuration {
    private String name;
    private Object obj;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "name='" + name + '\'' +
                ", obj=" + obj +
                '}';
    }
}
