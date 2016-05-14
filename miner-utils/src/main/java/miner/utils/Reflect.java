package miner.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String PaseRef(String res) throws IOException {

        String result = res;
        String price,sale,mydate = "";
        String pattern = "(?<=(price = \\[))\\S+(?=(]))";
        Pattern r = Pattern.compile(pattern);

        Matcher m = r.matcher(result);
        if (m.find( )) {
            price = (String) m.group(0);
//            System.out.println("Found value: " +  m.group(0) );
        } else {
            price = "none";
//            System.out.println("NO MATCH");
        }

        pattern =  "(?<=(sale = \\\"))\\S+(?=(\\\"))";
        r = Pattern.compile(pattern);

        m = r.matcher(result);
        if (m.find( )) {
            sale = (String) m.group(0);

//            System.out.println("Found value: " + m.group(0) );
        } else {
            sale = "none";
//            System.out.println("NO MATCH");
        }
        pattern =  "(?<=(date = \\\"))\\S+(?=(\\\"))";
        r = Pattern.compile(pattern);

        m = r.matcher(result);
        if (m.find( )) {
            mydate = (String) m.group(0);
//            System.out.println("Found value: " + m.group(0) );
        } else {
            mydate = "none";
//            System.out.println("NO MATCH");
        }
        return "{\"price\":\""+price+"\""+","+"\"sale\":"+"\""+sale+"\""+","+"\"date\":"+"\""+mydate+"\""+"}";
    }
}
