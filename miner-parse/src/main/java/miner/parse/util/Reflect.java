package miner.parse.util;

import miner.utils.MySysLogger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 反射Reflect类
 */
public class Reflect {

    private static MySysLogger logger = new MySysLogger(Reflect.class);

    //通过反射得到所需字段的json格式的字符串
    public static String GetReflect(String path, String resource){
        Object oc = null;
        try {
            URL url = new File(path).toURI().toURL();
            URLClassLoader ucl = new URLClassLoader(new URL[]{url});
            Class<?> myClass1 = Class.forName("xd.miner.reflect", true, ucl);
            Method method = myClass1.getMethod("PaseRef", String.class);
            oc = method.invoke(myClass1.newInstance(), resource);
        } catch (Exception ex) {
            logger.error("parse error!"+ex);
            ex.printStackTrace();
        }
        return oc.toString();
    }
}
