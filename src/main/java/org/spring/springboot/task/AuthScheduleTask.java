package org.spring.springboot.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.spring.springboot.dao.ResultDao;
import org.spring.springboot.domain.Auth;
import org.spring.springboot.domain.Response;
import org.spring.springboot.service.ResultService;
import org.spring.springboot.util.LocalCache;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by betty on 24/07/2020.
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class AuthScheduleTask {

    @Resource
    private ResultService resultService;
    @Resource
    private ResultDao resultDao;

    //1.添加定时任务 隔180秒  用来保持登录的
    @Scheduled(cron = "0/180 * * * * ?")
    private void authTasks() {

        List<Auth> userList = (ArrayList<Auth>) LocalCache.getObject("userinfo");
        for (int i=0;i<userList.size();i++){
            Auth auth = userList.get(i);
            resultService.isLoginedIn(auth.getUsername());
            //resultService.register(auth.getUsername());
        }

    }




    //2.添加定时任务 隔120秒  用来查询预测，同时和比赛对比的
    @Scheduled(cron = "0/60 * * * * ?")
    private void ycTasks() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            List<Auth> userList = (ArrayList<Auth>) LocalCache.getObject("userinfo");
            Auth auth = userList.get(0);
            auth.setMarketTypeId(1);
            Response response = resultService.getMatches(auth);
            JSONObject jsonObject = (JSONObject) response.getData();
            //获取赛程的信息
            JSONArray matchGames = jsonObject.getJSONArray("EventSportsTypes").getJSONObject(0).getJSONArray("Events");

            if (matchGames.size() > 0) {
                for (int i = 0; i < matchGames.size(); i++) {
                    JSONObject matchGame = matchGames.getJSONObject(i);
                    String gameId = matchGame.getString("MatchId");
                    //客队名称
                    String awayTeamName = matchGame.getString("Away");
                    //主队名称
                    String homeTeamName = matchGame.getString("Home");
                    long startTimeTemp = matchGame.getLong("StartTime");
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(startTimeTemp);
                    String startTime = sdf.format(cal.getTime());
                    Auth saveGame = new Auth();
                    saveGame.setGameId(gameId);
                    saveGame.setHomeName(homeTeamName);
                    saveGame.setGuestName(awayTeamName);
                    saveGame.setYcDate(startTime);
                    System.out.println("将比赛存入数据库");
                    List<Auth> sameList = resultDao.listMatchByGameId(saveGame);
                    if (sameList==null || sameList.size()==0) {
                        resultDao.saveMatch(saveGame);
                    }
                }



        }

    }



    //3.添加定时任务 隔20秒  用来判断比赛信息
    @Scheduled(cron = "0/60 * * * * ?")
    private void gameTasks() {

        List<Auth> userList = (ArrayList<Auth>) LocalCache.getObject("userinfo");
        for (int i=0;i<userList.size();i++){
            Auth auth = userList.get(i);
            Response response = resultService.getFeeds(auth);
            JSONObject jsonObject = (JSONObject)response.getData();
            //获取指定场次的信息
            JSONArray matchGames = jsonObject.getJSONArray("Sports").getJSONObject(0).getJSONArray("MatchGames");

            if (matchGames.size()>0) {
                JSONObject matchGame = jsonObject.getJSONArray("Sports").getJSONObject(0).getJSONArray("MatchGames").getJSONObject(0);
                String gameId = matchGame.getString("GameId");
                JSONObject fullTimeOu = matchGame.getJSONObject("FullTimeOu");
                String goal = fullTimeOu.getString("Goal");
                Boolean isActive = matchGame.getBoolean("IsActive");
                JSONObject awayTeam = matchGame.getJSONObject("AwayTeam");//客队
                //客队得分
                int awayTeamScore = awayTeam.getIntValue("Score");
                JSONObject homeTeam = matchGame.getJSONObject("HomeTeam");//主队
                int homeTeamScore = homeTeam.getIntValue("Score");

                if (isActive && goal.indexOf("-")<0 && awayTeamScore==0 && homeTeamScore==0 && LocalCache.getObject(gameId)!=null) {//比赛没有被封盘且没有进球，且不是范围的赔率
                    double compareGoal = Double.parseDouble("goal");
                    if (compareGoal>=1.5 && compareGoal<=1.7) {
                        auth.setGameId(gameId);
                        response = resultService.getPlacementInfo(auth);
                        JSONObject oddsPlacementData = ((JSONObject) response.getData()).getJSONArray("OddsPlacementData").getJSONObject(0);
                        String bookie = oddsPlacementData.getString("Bookie");
                        String odds = oddsPlacementData.getString("Odds");
                        double amount = oddsPlacementData.getDouble("MinimumAmount");//最少投入的额度
                        auth.setAmount(amount);
                        auth.setBookieOdds(bookie + ":" + odds);
                        LocalCache.setObject(gameId,null);
                        //resultService.getPlaceBet(auth);
                        System.out.println("开始进行投入，对应信息如下:");
                        System.out.println("isActive:"+isActive);
                        System.out.println("goal:"+goal);
                        System.out.println("客队得分:"+awayTeamScore);
                        System.out.println("主队得分:"+homeTeamScore);
                        System.out.println("比赛在预测中compareGoal:"+compareGoal);
                        System.out.println("***********end******");
                    }else{
                        System.out.println("开始无法进行投入，对应信息如下:");
                        System.out.println("isActive:"+isActive);
                        System.out.println("goal:"+goal);
                        System.out.println("客队得分:"+awayTeamScore);
                        System.out.println("主队得分:"+homeTeamScore);
                        System.out.println("比赛在预测中:"+LocalCache.getObject(gameId));
                        System.out.println("赔率:"+compareGoal);
                        System.out.println("***********end******");
                    }

                }else{
                    System.out.println("开始无法进行投入，对应信息如下:");
                    System.out.println("isActive:"+isActive);
                    System.out.println("goal:"+goal);
                    System.out.println("客队得分:"+awayTeamScore);
                    System.out.println("主队得分:"+homeTeamScore);
                    System.out.println("比赛在预测中:"+LocalCache.getObject(gameId));
                    System.out.println("***********end******");
                }
            }
        }
    }


    //3.添加定时任务 隔60秒  用来查询预测
    @Scheduled(cron = "0/60 * * * * ?")
    private void ycLoadTasks() {
        List<Auth> ycList = resultService.listYc();

        for (int j = 0; j < ycList.size(); j++) {
            Auth ycAuth = ycList.get(j);
            LocalCache.setObject(ycAuth.getGameId(), ycAuth);
        }


    }

}
