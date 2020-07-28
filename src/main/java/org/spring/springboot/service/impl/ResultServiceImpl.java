package org.spring.springboot.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.spring.springboot.dao.ResultDao;
import org.spring.springboot.domain.Auth;
import org.spring.springboot.domain.Response;
import org.spring.springboot.domain.Result;
import org.spring.springboot.service.ResultService;
import org.spring.springboot.util.LocalCache;
import org.spring.springboot.util.RestTemplateUtil;
import org.spring.springboot.util.UrlUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

/**
 * Created by betty on 18/07/2020.
 */
@Service
public class ResultServiceImpl implements ResultService {

    @Resource
    private ResultDao resultDao;

    @Resource
    private ResultService resultService;

    @Resource
    private RestTemplateUtil restTemplateUtil;

    public List<Result> list(Result result){
        return  resultDao.list(result);
    }

    @PostConstruct
    public void init() {
        System.out.println("初始化登录信息");
        List<Auth> userList = resultDao.userinfo();
        for (int i=0;i<userList.size();i++){
            Auth auth = userList.get(i);
            System.out.println("用户名:"+auth.getUsername()+"@@@密码:"+auth.getPassword());
            resultService.login(auth);
        }
        LocalCache.setObject("userinfo",userList);
    }

    @Override
    public Response login(Auth auth){
        if (auth==null || auth.getUsername()==null){
            auth = new Auth();
            auth.setUsername(UrlUtils.WEBAPIUSERNAME);
            auth.setPassword(UrlUtils.WEBAPIPASSWORD);
        }

        String url = UrlUtils.WEBAPIHOST+":"+UrlUtils.WEBAPIPORT+"/AsianOddsService/Login?username=" +
                auth.getUsername()+"&password="+auth.getPassword();
        Response response = restTemplateUtil.getBody(url,null);
        JSONObject jsonObject = (JSONObject)response.getData();
        LocalCache.setValue(auth.getUsername()+"AOToken",jsonObject.getString("Token"));
        LocalCache.setValue(auth.getUsername()+"AOKey",jsonObject.getString("Key"));
        resultService.register(auth.getUsername());
        return new Response();
    }

    @Override
    public void register(String username){
        String url = UrlUtils.WEBAPIURL+"/Register?username="+username;
        Map paramMap = new HashMap<>();
        paramMap.put("AOToken",LocalCache.getValue(username+"AOToken"));
        paramMap.put("AOKey",LocalCache.getValue(username+"AOKey"));
        Response response = restTemplateUtil.getBody(url,paramMap);

    }

    @Override
    public JSONObject isLoginedIn(String username){
        String url = UrlUtils.WEBAPIURL+"/IsLoggedIn";
        Map paramMap = new HashMap<>();
        paramMap.put("AOToken",LocalCache.getValue(username+"AOToken"));
        paramMap.put("AOKey",LocalCache.getValue(username+"AOKey"));
        Response response = restTemplateUtil.getBody(url,paramMap);
        JSONObject jsonObject = (JSONObject)response.getData();
        return jsonObject;

    }

    @Override
    public Response listSports(Auth auth){
        if (auth.getUsername()==null){
            auth.setUsername(UrlUtils.WEBAPIUSERNAME);
            auth.setPassword(UrlUtils.WEBAPIPASSWORD);
        }
        String url = UrlUtils.WEBAPIURL+"/GetSports";
        Map paramMap = new HashMap<>();
        paramMap.put("AOToken",LocalCache.getValue(auth.getUsername()+"AOToken"));
        paramMap.put("AOKey",LocalCache.getValue(auth.getUsername()+"AOKey"));
        Response response = restTemplateUtil.getBody2(url,paramMap);
        JSONArray jsonArray = (JSONArray)response.getData();
        return new Response(jsonArray);
    }

    @Override
    public Response getLeagues(Auth auth){
        if (auth.getUsername()==null){
            auth.setUsername(UrlUtils.WEBAPIUSERNAME);
            auth.setPassword(UrlUtils.WEBAPIPASSWORD);
        }
        if (auth.getMarketTypeId()==null){
            auth.setMarketTypeId(0);
        }
        String url = UrlUtils.WEBAPIURL+"/GetLeagues?marketTypeId="+auth.getMarketTypeId();
        Map paramMap = new HashMap<>();
        paramMap.put("AOToken",LocalCache.getValue(auth.getUsername()+"AOToken"));
        paramMap.put("AOKey",LocalCache.getValue(auth.getUsername()+"AOKey"));
        Response response = restTemplateUtil.getBody2(url,paramMap);
        JSONArray jsonArray = (JSONArray)response.getData();
        return new Response(jsonArray);
    }
    @Override
    public Response getMatches(Auth auth){
        if (auth.getUsername()==null){
            auth.setUsername(UrlUtils.WEBAPIUSERNAME);
            auth.setPassword(UrlUtils.WEBAPIPASSWORD);
        }
        if (auth.getMarketTypeId()==null){
            auth.setMarketTypeId(1);
        }
        if (auth.getSportsType()==null){
            auth.setSportsType(1);
        }
        if (auth.getLeagues()==null){
            auth.setLeagues(-295013793);
        }
        String url = UrlUtils.WEBAPIURL+"/GetMatches?marketTypeId="+auth.getMarketTypeId()+"&sportsType="+auth.getSportsType()+"&bookies=ALL&leagues="+auth.getLeagues();
        Map paramMap = new HashMap<>();
        paramMap.put("AOToken",LocalCache.getValue(auth.getUsername()+"AOToken"));
        paramMap.put("AOKey",LocalCache.getValue(auth.getUsername()+"AOKey"));
        Response response = restTemplateUtil.getBody(url,paramMap);
        JSONObject jsonObject = (JSONObject)response.getData();
        return new Response(jsonObject);
    }
    @Override
    public Response getFeeds(Auth auth){
        if (auth.getUsername()==null){
            auth.setUsername(UrlUtils.WEBAPIUSERNAME);
            auth.setPassword(UrlUtils.WEBAPIPASSWORD);
        }
        if (auth.getMarketTypeId()==null){
            auth.setMarketTypeId(0);//实时比赛
        }
        if (auth.getSportsType()==null){
            auth.setSportsType(1);
        }
        if (auth.getLeagues()==null){
            auth.setLeagues(-295013793);
        }
        if (auth.getBookies()==null && StringUtils.isEmpty(auth.getBookies())){
            auth.setBookies("MY");
        }
        String url = UrlUtils.WEBAPIURL+"/GetFeeds?marketTypeId="+auth.getMarketTypeId()+"&sportsType="+auth.getSportsType()+"&leagues"+auth.getLeagues()+"&oddsFormat=";
        Map paramMap = new HashMap<>();
        paramMap.put("AOToken",LocalCache.getValue(auth.getUsername()+"AOToken"));
        paramMap.put("AOKey",LocalCache.getValue(auth.getUsername()+"AOKey"));
        Response response = restTemplateUtil.getBody(url,paramMap);
        JSONObject jsonObject = (JSONObject)response.getData();
        return new Response(jsonObject);
    }


    @Override
    public Response getPlacementInfo(Auth auth){
        if (auth.getUsername()==null){
            auth.setUsername(UrlUtils.WEBAPIUSERNAME);
            auth.setPassword(UrlUtils.WEBAPIPASSWORD);
        }
        Map paramMap = new HashMap<>();
        paramMap.put("GameId",auth.getGameId());
        paramMap.put("GameType",0);
        paramMap.put("IsFullTime",1);
        paramMap.put("MarketTypeId",0);
        paramMap.put("OddsFormat","");
        paramMap.put("OddsName","OverOdds");
        paramMap.put("SportsType",1);

        String url = UrlUtils.WEBAPIURL+"/GetPlacementInfo";
        Map headMap = new HashMap<>();
        headMap.put("AOToken",LocalCache.getValue(auth.getUsername()+"AOToken"));
        headMap.put("AOKey",LocalCache.getValue(auth.getUsername()+"AOKey"));
        Response response = restTemplateUtil.postBody(url,paramMap,headMap);
        return response;
    }

    @Override
    public Response getPlaceBet(Auth auth){
        if (auth.getUsername()==null){
            auth.setUsername(UrlUtils.WEBAPIUSERNAME);
            auth.setPassword(UrlUtils.WEBAPIPASSWORD);
        }
        Map paramMap = new HashMap<>();
        paramMap.put("GameId",auth.getGameId());
        paramMap.put("Amount",auth.getAmount());
        paramMap.put("BookieOdds",auth.getBookieOdds());
        paramMap.put("AcceptChangedOdds",0);
        paramMap.put("GameType",0);
        paramMap.put("IsFullTime",1);
        paramMap.put("MarketTypeId",0);
        paramMap.put("OddsFormat","MY");
        paramMap.put("OddsName","OverOdds");
        paramMap.put("SportsType",1);

        String url = UrlUtils.WEBAPIURL+"/PlaceBet";
        Map headMap = new HashMap<>();
        headMap.put("AOToken",LocalCache.getValue(auth.getUsername()+"AOToken"));
        headMap.put("AOKey",LocalCache.getValue(auth.getUsername()+"AOKey"));
        Response response = restTemplateUtil.postBody(url,paramMap,headMap);
        return response;
    }
    @Override
    public List<Auth> listYc(){
        List<Auth> ycGanmeList = resultDao.listYc();
        return ycGanmeList;
    }

}
