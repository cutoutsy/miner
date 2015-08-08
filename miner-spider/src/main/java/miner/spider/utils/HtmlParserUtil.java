package miner.spider.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cutoutsy on 7/24/15.
 */
public class HtmlParserUtil {
    public static final String EMPTY_STRING = "";
    public static final String all_text_regex = "[\\s\\S]*?";
    private String title_regex = "<title>" + all_text_regex +"</title>";

    private Pattern title_pattern = Pattern.compile(title_regex);
    private Matcher matcher = null;
    private String temp = null;

    public String getTitleByLine(String line){
        matcher = this.title_pattern.matcher(line);
        if(matcher.find()){
            temp = matcher.group();
            return temp.replace("<title>", EMPTY_STRING).replace("</title>", EMPTY_STRING);
        }
        return null;
    }

}
