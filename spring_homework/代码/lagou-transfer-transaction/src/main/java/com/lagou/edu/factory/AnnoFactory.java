package com.lagou.edu.factory;

import com.lagou.edu.annotation.Autowire;
import com.lagou.edu.annotation.Service;
import com.lagou.edu.annotation.TransactionManager;
import com.lagou.edu.pojo.Configuration;
import com.lagou.edu.utils.ClassUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @authorAdministrator
 * @date 2020/9/820:01
 * @description
 */
public class AnnoFactory {
    private static Map<String,Object> map = new HashMap<>();

    public void parseAnno() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        List<String> beanList = new ClassUtil().getBeanList();
        //获取每一个bean
        for (String s : beanList) {
            if(!Class.forName(s).isAnnotation()){
                Class clazz = getInstance(s);
                Annotation[] annotations = getAnnotations(clazz);
                //遍历每个bean的所有注解
                for (Annotation annotation : annotations) {
                    //如果是事务管理,返回代理对象
                    if(annotation.getClass().isInstance(TransactionManager.class)){
                        Object o = new ProxyFactory().getJdkProxy(clazz);
                        map.put(getLowClassName(clazz.getName(),clazz),o);
                        //解决service动态代理后autowired获取不到问题（通过接口获取）
                        Class<?> anInterface = clazz.getClass().getInterfaces()[0];
                        String interfaceName = clazz.getInterfaces().getClass().getName();
                        map.put(getLowClassName(interfaceName,anInterface),anInterface.newInstance());
                    }
                    //如果是service，按名称装配
                    if(annotation.getClass().isInstance(Service.class)){
                        String value = annotation.getClass().getAnnotation(Service.class).value();
                        map.put(getLowClassName(value,clazz),clazz);
                    }
                }
                //遍历属性，查看有没有autoWired注解
                Field[] fields = clazz.getFields();
                for (Field field : fields) {
                    if(field.isAnnotationPresent(Autowire.class)){
                        String name = field.getType().getName();
                        field.setAccessible(true);
                        Object o = field.get(Class.forName(name).newInstance());
                    }
                }
            }
        }
    }

    //bean实例化
    public Class getInstance(String s) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return (Class) Class.forName(s).newInstance();
    }

    public Annotation[] getAnnotations(Class clazz){
        return clazz.getDeclaredAnnotations();
    }

    public String getLowClassName(String value,Class clazz){
        if(value == null || "".equals(value)){
            String name = clazz.getName();
            return String.valueOf(name.charAt(0)).toLowerCase()+name.substring(1);
        }else{
            return value;
        }
    }
}
