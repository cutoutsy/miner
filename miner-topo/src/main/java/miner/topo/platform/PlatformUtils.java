package miner.topo.platform;

import miner.spider.utils.MysqlUtil;
import miner.utils.MySysLogger;
import miner.utils.RedisUtil;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * some PlatformUtils for the platform
 */
public class PlatformUtils {

    private static MySysLogger logger = new MySysLogger(PlatformUtils.class);

    private static RedisUtil ru;
    public static Jedis redis;

    static {
        ru = new RedisUtil();
        redis = ru.getJedisInstance();
    }

    //monitor the project, return a project can execute
    public static String monitorProject(){
        String reProject = "";
        Set<String> projectKeys = redis.hkeys("project_state");
        Iterator it = projectKeys.iterator();
        while(it.hasNext()){
            String oneProjectKey = it.next().toString();
            Project pj = new Project(oneProjectKey);
            String oneProjectState = redis.hget("project_state", oneProjectKey);
            if(oneProjectState.equals("undo")){
                if(pj.getCondition().equals("alone")){
                    reProject = oneProjectKey;
                    break;
                }else{
                    boolean projectExecute = true;
                    String preCondition = pj.getCondition();
                    String[] preProjectName = preCondition.split(",");
                    for(int i = 0; i < preProjectName.length ; i++){
                        if(redis.hexists("project_state", preProjectName[i])) {
                            String tempProjectState = redis.hget("project_state", preProjectName[i]);
                            if (tempProjectState.equals("undo")) {
                                projectExecute = false;
                            }
                        }else{
                            projectExecute = true;
                        }
                    }
                    if(projectExecute){
                        reProject = oneProjectKey;
                        break;
                    }
                }
            }
        }
        return reProject;
    }

    public static String getProject(){
        String reProject = "";
        long executeProjectNums = redis.llen("project_execute");
        if(executeProjectNums > 0) {
            String projectName = redis.rpop("project_execute");
            int ProjectExecuteNum = Integer.valueOf(redis.hget("project_executenum", projectName));
            Project pj = new Project(projectName);
            if (pj.getCondition().equals("alone")) {
                reProject = projectName;
            } else {
                boolean projectExecute = true;
                String preCondition = pj.getCondition();
                String[] preProjectName = preCondition.split(",");

                for (int i = 0; i < preProjectName.length; i++) {

                    if (redis.hexists("project_executenum", preProjectName[i])) {
                        int tempProjectExecuteNum = Integer.valueOf(redis.hget("project_executenum", preProjectName[i]));
                        if (tempProjectExecuteNum <= ProjectExecuteNum) {
                            projectExecute = false;
                        }
                        break;
                    } else {
                        projectExecute = true;
                    }
                }
                if (projectExecute) {
                    reProject = projectName;
                }

            }
        }

        return reProject;
    }

    //return execute task list
    public static List getProjectList(){
        List<String> reList = new ArrayList<String>();
        List<String> projectList = redis.lrange("project_execute", 0, -1);
        if(projectList.size() > 0) {

            for(int i = 0; i < projectList.size(); i++ ){
                String projectName = projectList.get(i);
                int ProjectExecuteNum = Integer.valueOf(redis.hget("project_executenum", projectName));
                Project pj = new Project(projectName);

                if (pj.getCondition().equals("alone")) {
                    if(redis.hget("project_state", projectName).equals("die")) {
                        reList.add(projectName);
                        redis.lrem("project_execute", 1, projectName);
                    }
                } else {
                    boolean projectExecute = true;
                    String preCondition = pj.getCondition();
                    String[] preProjectName = preCondition.split(",");

                    for (int j = 0; j < preProjectName.length; j++) {

                        if (redis.hexists("project_executenum", preProjectName[j])) {
                            int tempProjectExecuteNum = Integer.valueOf(redis.hget("project_executenum", preProjectName[j]));
                            if (tempProjectExecuteNum <= ProjectExecuteNum) {
                                projectExecute = false;
                            }
                            break;
                        } else {
                            projectExecute = true;
                        }
                    }
                    if (projectExecute) {
                        if(redis.hget("project_state", projectName).equals("die")) {
                            reList.add(projectName);
                            redis.lrem("project_execute", 1, projectName);
                        }
                    }

                }
            }
        }
        return reList;
    }

    //generate url
    //http://cq.meituan.com/==http://[replace].meituan.com/
    //http://hotel.elong.com/dalian/40801255/==http://hotel.elong.com/dalian/[replace]/
    public static String GenerateUrl(String message,String urlPattern){
        String reGenerateUrl = "";
        boolean flag = true;
        String[] sts = urlPattern.split("\\[replace\\]");
        for(int i = 0; i < sts.length; i++ ){
            String tempString = sts[i];
            if(message.contains(tempString)){
                flag = false;
            }
        }
        if(flag){
            reGenerateUrl = urlPattern.replace("[replace]", message);
        }
        return reGenerateUrl;
    }

    //register project to schedule
    public static void registerProject(QuartzManager qManager){
        Set<String> projectKeys = redis.hkeys("project_cronstate");
        Iterator it = projectKeys.iterator();
        while(it.hasNext()) {
            String projectKey = it.next().toString();
            String projectCronState = redis.hget("project_cronstate", projectKey);
            if(projectCronState.equals("3")){
                QuartzJob qJob = new QuartzJob(projectKey);
                qManager.initJob(qJob, ProjectJob.class);
                redis.hset("project_cronstate", projectKey, "1");
            }
        }
    }

    public static void initRegisterProject(QuartzManager qManager){
        Set<String> projectKeys = redis.hkeys("project_cronstate");
        Iterator it = projectKeys.iterator();
        while(it.hasNext()) {
            String projectKey = it.next().toString();
            QuartzJob qJob = new QuartzJob(projectKey);
            qManager.initJob(qJob, ProjectJob.class);
            redis.hset("project_cronstate", projectKey, "1");
        }
    }

    //get all task of a project
    public static List getTaskByProject(Project pj){
        List<String> reList = new ArrayList<String>();
        reList = MysqlUtil.getTaskByProject(pj.getWid(), pj.getPid());
        return reList;
    }

    public static List getTaskByProject(String wid, String pid){
        List<String> reList = new ArrayList<String>();
        reList = MysqlUtil.getTaskByProject(wid, pid);
        return reList;
    }

    public static List getTaskByProject(String projectName){
        List<String> reList = new ArrayList<String>();
        reList = MysqlUtil.getTaskByProject(projectName.split("-")[0], projectName.split("-")[1]);
        return reList;
    }
    //return emit url in the GenerateUrlBolt
    public static String getEmitUrl(String globalInfo, String message){
        if(message.equals("none")){
            return "";
        }
        String emitUrl = "";
        String taskName = globalInfo;
        Task ta = new Task(taskName);

        Boolean isGenerate = Boolean.valueOf(ta.getUrlgenerate()).booleanValue();
        if (isGenerate) {
            //isGenerate=true, need to generate url
            emitUrl = PlatformUtils.GenerateUrl(message, ta.getUrlpattern());
        } else {
            //isgenerate=flase, do not generate url;
            emitUrl = message;
        }

        return emitUrl;
    }

    public static String getUUID(){
        String s = UUID.randomUUID().toString();
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
    }

    public static void main(String[] args){
//        System.out.println(getUUID());
//        QuartzManager q = new QuartzManager();
//        registerProject(q);
        String kk = getEmitUrl("1-1-1", "none");
        if(kk.isEmpty()){
            System.out.println("==========");
        }
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
