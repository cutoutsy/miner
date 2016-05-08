package miner.parse.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by MaFu on 2016/5/8.
 */
public class Reflect {
    public static void  GetReflect(String path,String resource) throws InstantiationException, IllegalAccessException, IOException {
        //控制台输入,类比配置文件。
//        BufferedReader strbuff = new BufferedReader(new InputStreamReader(System.in));
//        String str = "add1";
//        System.out.println(str);
        try {
            URL url = new File(path).toURI().toURL();
//            @SuppressWarnings("resource")
//            URLClassLoader myClassLoader1 = new URLClassLoader(new URL[] { url1 }, Thread.currentThread().getContextClassLoader());
//            Class<?> myClass1 = myClassLoader1.loadClass("cn.cutoutsy.Add");
//
            URLClassLoader ucl = new URLClassLoader(new URL[]{url});
            Class<?> myClass1 = Class.forName("xd.miner.reflect",true,ucl);
//            AbstractAction action1 = (AbstractAction) myClass1.newInstance();
            Method method = myClass1.getMethod("PaseRef", String.class);
//            int str1 = action1.Add(10, 2);
            Object oc = method.invoke(myClass1.newInstance(), resource);
            System.out.println(oc.toString());
//            System.out.println(str1);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
}
}
