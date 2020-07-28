package org.spring.springboot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.spring.springboot.domain.Auth;
import org.spring.springboot.domain.Response;
import org.spring.springboot.domain.Result;
import org.spring.springboot.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
/**
 * Created by betty on 18/07/2020.
 */
@RestController
@RequestMapping("/result")
@Api("预测结果")
public class ResultController {
    @Autowired
    private ResultService resultService;

    @ApiOperation("预测结果查询")
    @RequestMapping(value = "/list/v1", method = RequestMethod.POST)
    public Response findById(@RequestBody Result result, HttpServletRequest request) {
        return new Response(resultService.list(result));
    }

    @ApiOperation("登录")
    @RequestMapping(value = "/login/v1", method = RequestMethod.POST)
    public Response login(@RequestBody Auth auth, HttpServletRequest request) {
        return resultService.login(auth);
    }

    @ApiOperation("查询比赛类型")
    @RequestMapping(value = "/listSports/v1", method = RequestMethod.POST)
    public Response listSports(@RequestBody Auth auth, HttpServletRequest request) {
        return resultService.listSports(auth);
    }

    @ApiOperation("查询类型对应的比赛")
    @RequestMapping(value = "/getLeagues/v1", method = RequestMethod.POST)
    public Response getLeagues(@RequestBody Auth auth, HttpServletRequest request) {
        return resultService.getLeagues(auth);
    }

    @ApiOperation("综合获取比赛的信息")
    @RequestMapping(value = "/getMatches/v1", method = RequestMethod.POST)
    public Response getMatches(@RequestBody Auth auth, HttpServletRequest request) {
        return resultService.getMatches(auth);
    }

    @ApiOperation("综合获取比赛的信息和赔率")
    @RequestMapping(value = "/getFeeds/v1", method = RequestMethod.POST)
    public Response getFeeds(@RequestBody Auth auth, HttpServletRequest request) {
        return resultService.getFeeds(auth);
    }
}
