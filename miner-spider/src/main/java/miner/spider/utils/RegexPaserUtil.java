package miner.spider.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cutoutsy on 7/24/15.
 */
public class RegexPaserUtil {
    private String beginRegex;
    private String endRegex;
    private Matcher matcher;

    public final static String N = "";

    private List<String> filterRegexList = new ArrayList<String>();

    // 是否为全正常中英文、符号的情况验证
    // public static String All_Chinese_Char =
    // "[·！/|“”？：（）()—\\s、,;.，。;!?\\-_A-Za-z\\d\\u4E00-\\u9FA5 ^ :>~&'\\=>%@+\\pP\\pZ\\pM\\pS]";
    public static String All_Chinese_Char = "[\\sA-Za-z\\d\\u4E00-\\u9FA5\\pP\\pZ\\pM\\pN\u3040-\u309F\u30A0-\u30FF+\\-*/\\\\$●=><|\\[\\]]";

    public Pattern All_Chinese_Char_Pattern = Pattern.compile(All_Chinese_Char);

    // 最常用汉字，自编
    public static String Frequency_Chinese_Char = "[的与是中网]";
    public Pattern Frequency_Chinese_Char_Pattern = Pattern.compile(Frequency_Chinese_Char);

    // 此处的中文判断，包括中文、英文、数字、中英文符号等
    public boolean isAllChineseChar(String source) {
        if (source == null || source.trim().length() == 0) {
            return true;
        } else {
            char[] charArray = source.toCharArray();
            for (char c : charArray) {
                if (!(All_Chinese_Char_Pattern.matcher(c + "").find())) {
                    return false;
                }
            }
            return true;
        }
    }


    public boolean isContainFreqChineseChar(String source) {
        if (source == null) {
            return false;
        }
        if (Frequency_Chinese_Char_Pattern.matcher(source).find()) {
            return true;
        }
        return false;
    }

    public RegexPaserUtil(){

    }

    public RegexPaserUtil(String beginRegex,String endRegex,String content,String textRegex){
        this.beginRegex = beginRegex;
        this.endRegex = endRegex;
        StringBuilder sb = new StringBuilder();
        sb.append(beginRegex);
        sb.append(textRegex);
        sb.append(endRegex);
        matcher = Pattern.compile(sb.toString()).matcher(content);
    }

    public RegexPaserUtil(String beginRegex, String endRegex, String textRegex) {

        this.beginRegex = beginRegex;

        this.endRegex = endRegex;

        StringBuilder sb = new StringBuilder();

        sb.append(beginRegex);

        sb.append(textRegex);

        sb.append(endRegex);
        matcher = Pattern.compile(sb.toString()).matcher(N);
    }

    public RegexPaserUtil reset(String content) {
        this.matcher.reset(content);
        return this;
    }

    public String getSimpleText(){
        if(matcher.find()){
            String str = matcher.group().trim();
            return str;
        }
        return null;
    }

    public String getText(){
        if(matcher.find()){
            String str = matcher.group().trim().replaceFirst(beginRegex, N).replaceAll(endRegex, N);
            Iterator<String> it = filterRegexList.iterator();
            while(it.hasNext()){
                str = str.replaceAll(it.next(), N);
            }
            return str;
        }
        return null;
    }

    public String getLastText(){
        String str = null;
        while(matcher.find()){
            str = matcher.group().trim().replaceFirst(beginRegex, N).replaceAll(endRegex, N);
        }
        return str;
    }
}
