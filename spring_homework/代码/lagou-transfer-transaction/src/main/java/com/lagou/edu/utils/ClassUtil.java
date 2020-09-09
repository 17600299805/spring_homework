package com.lagou.edu.utils;

import com.lagou.edu.annotation.Autowire;
import com.lagou.edu.annotation.Service;
import org.junit.Test;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @authorAdministrator
 * @date 2020/9/721:10
 * @description
 */
public class ClassUtil {
    private static List<String> beanList = new ArrayList<>();

    public ClassUtil() {
        scanFile();
    }

    @Test
    public void scanFile(){
        URL url = this.getClass().getClassLoader().getResource("");
        File file = new File(url.getFile());
        listFile(file);
    }

    private void listFile(File file) {
        File[] files = file.listFiles();
        for (File file1 : files) {
            if(file1.isDirectory()){
                listFile(file1);
            }else if(file1.getName().endsWith(".class") && !file1.getName().contains("$")){
                putFile(file1);
            }
        }
    }

    private void putFile(File file1) {
//        String fileName = file1.getName();
//        String largeName = fileName.substring(0,fileName.lastIndexOf("."));
//        String name = String.valueOf(largeName.charAt(0)).toLowerCase()+largeName.substring(1);

        String filePath = file1.getPath().replace("\\",".");
        String path = filePath.split(".classes.")[1].replace(".class","");
        beanList.add(path);


//        if(file1.getClass().getDeclaredAnnotation(Autowire.class) !=null){
//            String value = file1.getClass().getDeclaredAnnotation(Autowire.class).value();
//        }
//        Autowire annoService = file1.getClass().getDeclaredAnnotation(Autowire.class);
//        for (Annotation annotation : annotations) {
//            if(annotation.equals("Service")){
//
//            }
//        }
    }

    public List<String> getBeanList(){
        return beanList;
    }
}
