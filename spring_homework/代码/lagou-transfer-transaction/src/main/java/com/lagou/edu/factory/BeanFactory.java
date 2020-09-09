package com.lagou.edu.factory;

import com.lagou.edu.annotation.Autowire;
import com.lagou.edu.annotation.Service;
import com.lagou.edu.annotation.TransactionManager;
import org.reflections.Reflections;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;

import javax.naming.Name;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @authorAdministrator
 * @date 2020/8/3122:14
 * @description
 */
public class BeanFactory {
    private static Map<String,Object> map = new HashMap();

    static {
        try {
            //通过反射获取扫描包的效果
            Reflections reflections = new Reflections("com.lagou.edu");
            //获取所有Service注解的类,将该类存到bean中
            Set<Class<?>> serviceAnnoSet = reflections.getTypesAnnotatedWith(Service.class,false);
            for (Class<?> aClass : serviceAnnoSet) {
                // 通过反射技术实例化对象
                Object bean = aClass.newInstance();
                Service annotation = aClass.getDeclaredAnnotation(Service.class);
                //对象ID在service注解有value时用value，没有时用类名
                if(StringUtils.isEmpty(annotation.value())){
                    //由于getName获取的是全限定类名，所以要分割去掉前面包名部分
                    String[] names = aClass.getName().split("\\.");
                    map.put(names[names.length-1], bean);
                }else{
                    map.put(annotation.value(), bean);
                }
            }

            //检查autoWire，注入对应的对象
            for(Map.Entry<String, Object> set:map.entrySet()){
                Object o = set.getValue();
                Class c = o.getClass();
                //获取所有属性，判断有无autoWire注解
                Field[] fields = c.getDeclaredFields();
                for (Field field : fields) {
                    if(field.isAnnotationPresent(Autowire.class)){
                        Method[] methods = c.getMethods();
                        for (Method method : methods) {
                            String[] names = field.getType().getName().split("\\.");
                            //有autoWire注解，判断有无set方法
                            if(method.getName().equalsIgnoreCase("set"+names[names.length-1])){
                                //第一个参数为实例化对象，第二个参数是set方法的入参
                                method.invoke(o,map.get(names[names.length-1]));
                            }
                        }
                    }

                    //判断类是否有事务注解
                    boolean txFlag = c.isAnnotationPresent(TransactionManager.class);
                    if(txFlag){
                        //判断是否有接口：有使用jdk动态代理，没有使用cglib
                        Class<?>[] interfaces = c.getInterfaces();
                        ProxyFactory proxyFactory = (ProxyFactory) getBean("ProxyFactory");
                        if(interfaces != null &&interfaces.length>0){
                            proxyFactory.getJdkProxy(set.getValue());
                        }else {
                            proxyFactory.cglibProxyObject(set.getValue());
                        }
                    }
                    map.put(set.getKey(),o);
                }
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static Object getBean(String id){
        return map.get(id);
    }
}
