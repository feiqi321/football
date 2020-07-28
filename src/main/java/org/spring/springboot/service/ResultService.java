package org.spring.springboot.service;

import com.alibaba.fastjson.JSONObject;
import org.spring.springboot.domain.Auth;
import org.spring.springboot.domain.Response;
import org.spring.springboot.domain.Result;

import java.util.List;

/**
 * Created by betty on 18/07/2020.
 */
public interface ResultService {

    List<Result> list(Result result);

    Response login(Auth auth);

    void register(String username);

    JSONObject isLoginedIn(String username);

    Response listSports(Auth auth);

    Response getLeagues(Auth auth);

    Response getMatches(Auth auth);

    Response getFeeds(Auth auth);

    Response getPlacementInfo(Auth auth);

    Response getPlaceBet(Auth auth);

    List<Auth> listYc();

}
