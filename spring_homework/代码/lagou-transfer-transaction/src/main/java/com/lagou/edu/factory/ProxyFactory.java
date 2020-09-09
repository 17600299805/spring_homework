package com.lagou.edu.factory;

import com.lagou.edu.annotation.Autowire;
import com.lagou.edu.annotation.Service;
import com.lagou.edu.utils.CollentionUtils;
import com.lagou.edu.utils.TransactionManagerUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @authorAdministrator
 * @date 2020/9/121:25
 * @description
 */
@Service
public class ProxyFactory {
    @Autowire
    private TransactionManagerUtils transactionManagerUtils;

    public void setTransactionManagerUtils(TransactionManagerUtils transactionManagerUtils) {
        this.transactionManagerUtils = transactionManagerUtils;
    }

    public Object getJdkProxy(Object o){
        return Proxy.newProxyInstance(o.getClass().getClassLoader(), o.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                try {
                    transactionManagerUtils.setCommit(false);
                    Object invoke = method.invoke(o, args);
                    transactionManagerUtils.commit();
                    return invoke;
                }catch (Exception e){
                    e.printStackTrace();
                    transactionManagerUtils.rollBack();
                    throw e;
                }
            }
        });
    }

    public Object cglibProxyObject(Object obj){
        return Enhancer.create(obj.getClass(), new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                Object result = null;
                try {
                    transactionManagerUtils.setCommit(false);
                    method.invoke(obj,objects);
                    transactionManagerUtils.commit();
                }catch (Exception e){
                    e.printStackTrace();
                    transactionManagerUtils.rollBack();
                    throw e;
                }
                return result;
            }
        });
    }
}
