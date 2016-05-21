package miner.parse.data;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 反射解析代码测试
 */
public class reflectionTest {
    public static void  main(String[] args) throws InstantiationException, IllegalAccessException, IOException {
        //控制台输入,类比配置文件。
//        BufferedReader strbuff = new BufferedReader(new InputStreamReader(System.in));
        String str = "add1";
        URL xurl = new URL("http://mailuntai.cn/product/4937.html");
        HttpURLConnection con = (HttpURLConnection) xurl.openConnection();
        InputStream is = con.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, "utf-8");
        String result = "";
        int read;
        while ((read = isr.read()) != -1) {
            result += (char) read;
        }
        isr.close();

//        System.out.println(str);
        try {
            URL url = new File("E:\\spedev\\reflect.jar").toURI().toURL();
//            @SuppressWarnings("resource")
//            URLClassLoader myClassLoader1 = new URLClassLoader(new URL[] { url1 }, Thread.currentThread().getContextClassLoader());
//            Class<?> myClass1 = myClassLoader1.loadClass("cn.cutoutsy.Add");
//
            URLClassLoader ucl = new URLClassLoader(new URL[]{url});
            Class<?> myClass1 = Class.forName("xd.miner.reflect",true,ucl);
//            AbstractAction action1 = (AbstractAction) myClass1.newInstance();
            Method method = myClass1.getMethod("PaseRef", String.class);
//            int str1 = action1.Add(10, 2);
            Object oc = method.invoke(myClass1.newInstance(), result);
            System.out.println(oc.toString());
//            System.out.println(str1);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//        try {
//            Class<?> class1 = null;
//            class1 = Class.forName("miner.parse.data.Add");
//
////            class1 = Class.forName("/Users/cutoutsy/IdeaProjects/miner/miner-parse/target/test-classes/miner/parse/data/Add");
////            System.out.println("Demo7: \n调用无参方法fly()：");
////            Method method = class1.getMethod("fly");
////            method.invoke(class1.newInstance());
//
//            System.out.println("调用有参方法add(int m)：");
//            Method method = class1.getMethod("add", int.class, int.class);
//            Object oc = method.invoke(class1.newInstance(), 100, 120);
//            System.out.println(oc.toString());
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
    }

}
