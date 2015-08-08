package miner.topo.platform;

import com.cutoutsy.utils.MyLogger;
import com.cutoutsy.utils.RedisUtil;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.Set;

/**
 * some PlatformUtils for the platform
 *
 * Created by cutoutsy on 8/4/15.
 */
public class PlatformUtils {

    private static MyLogger logger = new MyLogger(PlatformUtils.class);

    public static Jedis redis;

    //monitor the project, return a project can execute
    public static String monitorProject(){
        String reProject = "";
        redis = RedisUtil.GetRedis();
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
        redis = RedisUtil.GetRedis();
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


    //generate url
    //http://cq.meituan.com/==http://[replace].meituan.com/
    //http://hotel.elong.com/dalian/40801255/==http://hotel.elong.com/dalian/[replace]/
    public static String GenerateUrl(String message,String urlPattern){
        return urlPattern.replace("[replace]", message);
    }

    //register project to schedule
    public static void registerProject(QuartzManager qManager){
        redis = RedisUtil.GetRedis();
//        redis.hkeys("project_cronState");
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
        redis = RedisUtil.GetRedis();
//        redis.hkeys("project_cronState");
        Set<String> projectKeys = redis.hkeys("project_cronstate");
        Iterator it = projectKeys.iterator();
        while(it.hasNext()) {
            String projectKey = it.next().toString();
            QuartzJob qJob = new QuartzJob(projectKey);
            qManager.initJob(qJob, ProjectJob.class);
            redis.hset("project_cronstate", projectKey, "1");
        }
    }

    public static void main(String[] args){
//        String reProject = monitorProject();
//        System.out.println(reProject);

//        String url = GenerateUrl("cq","http://[replace].meituan.com/");
        String url = GenerateUrl("40801255", "http://hotel.elong.com/dalian/[replace]/");
        System.out.println(url);

//        String kk = "";
//        if(kk.isEmpty()){
//            System.out.println("11111111111");
//        }
    }

}
