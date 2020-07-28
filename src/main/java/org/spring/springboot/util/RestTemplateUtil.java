package org.spring.springboot.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.springboot.domain.Response;
import org.spring.springboot.exception.GlobalException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Created by caofanCPU on 2018/8/6.
 */
@Component
public class RestTemplateUtil {
    
    private static Logger logger = LoggerFactory.getLogger(RestTemplateUtil.class);
    



    @Resource
    private RestTemplate nRestTemplate;
    
    /**
     * 剔除请求中值为null的参数
     *
     * @param paramsMap
     * @return
     */
    public Map<String, Object> removeNullElement(Map<String, Object> paramsMap) {
        if (paramsMap == null || paramsMap.isEmpty()) {
            return new HashMap<>(1, 0.5f);
        }
        /** 一般请求参数不会太多，故而使用单向顺序流即可 */
        // 1.首先构建流，剔除值为空的元素
        Stream<Map.Entry<String, Object>> tempStream = paramsMap.entrySet().stream()
                .filter((entry) -> entry.getValue() != null);
        // 2.从流中恢复map
        Map<String, Object> resultMap = tempStream.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        logger.info("完成请求参数中null值的剔除");
        return resultMap;
    }
    
    public Response convertResponse(JSONObject responseJson) {
        Response WebResult = new Response();
        boolean msgFlag = (responseJson.get("msg") == null);
        if (!ResponseCode.SUCCESS.getCode().equals(String.valueOf(responseJson.get("code")))) {
            WebResult.setData(null);
            WebResult.setCode(Integer.parseInt(String.valueOf(responseJson.get("code"))));
            if (msgFlag) {
                WebResult.setMsg(ResponseCode.CALL_SERVICE_ERROR.getMsg());
            } else {
                WebResult.setMsg(responseJson.get("msg").toString());
            }
        } else {
            WebResult.setData(responseJson.get("data"));
            WebResult.setCode(ResponseCode.SUCCESS.getCode());
            if (msgFlag) {
                WebResult.setMsg(ResponseCode.SUCCESS.getMsg());
            } else {
                WebResult.setMsg(responseJson.get("msg").toString());
            }
        }
        return WebResult;
    }

    /**
     * 服务调用 返回的数据集的节点名称为Result
     *
     * @param url
     * @return
     */
    public Response getBody(String url,Map paramMap) throws GlobalException {
        ResponseEntity<String> responseEntity = null;
        try {
            if (paramMap !=null) {
                HttpEntity<Map<String, Object>> httpEntity = loadHttpEntity(paramMap);
                responseEntity = nRestTemplate.exchange(url, HttpMethod.GET,httpEntity,String.class);
            }else {
                responseEntity = nRestTemplate.getForEntity(url, String.class);
            }
            if (responseEntity == null) {
                logger.error("调用服务接口失败：{}", url + "\n响应为null");
            }
        } catch (RestClientException e) {
            logger.error("调用服务接口失败：{}", url + "\n" + e);
            throw new GlobalException(ResponseCode.CALL_SERVICE_ERROR);
        }
        if (responseEntity == null) {
            return new Response(new GlobalException(ResponseCode.CALL_SERVICE_ERROR));
        }
        logger.info("调用接口：{},{} ", url, responseEntity);
        String resultCode = responseEntity.getStatusCode().toString();
        if (resultCode.indexOf(ResponseCode.SUCCESS.getCode().toString())>=0) {
            JSONObject data = (JSONObject) JSON.parse(responseEntity.getBody());
            if (data.get("Code")!=null || data.getInteger("Code").equals(0)) {

                return new Response(data.getJSONObject("Result"));
            }else{
                throw new GlobalException(ResponseCode.CALL_WX_ERROR);

            }
        } else {
            throw new GlobalException(ResponseCode.CALL_SERVICE_ERROR);
        }
    }



    /**
     * 服务调用 返回的数据集的节点名称为Data
     *
     * @param url
     * @return
     */
    public Response getBody2(String url,Map paramMap) throws GlobalException {
        ResponseEntity<String> responseEntity = null;
        try {
            if (paramMap !=null) {
                HttpEntity<Map<String, Object>> httpEntity = loadHttpEntity(paramMap);
                responseEntity = nRestTemplate.exchange(url, HttpMethod.GET,httpEntity,String.class);
            }else {
                responseEntity = nRestTemplate.getForEntity(url, String.class);
            }
            if (responseEntity == null) {
                logger.error("调用服务接口失败：{}", url + "\n响应为null");
            }
        } catch (RestClientException e) {
            logger.error("调用服务接口失败：{}", url + "\n" + e.getMessage());
            throw new GlobalException(ResponseCode.CALL_SERVICE_ERROR);
        }
        if (responseEntity == null) {
            return new Response(new GlobalException(ResponseCode.CALL_SERVICE_ERROR));
        }
        logger.info("调用接口：{},{} ", url, responseEntity);
        String resultCode = responseEntity.getStatusCode().toString();
        if (resultCode.indexOf(ResponseCode.SUCCESS.getCode().toString())>=0) {
            JSONObject data = (JSONObject) JSON.parse(responseEntity.getBody());
            if (data.get("Code")!=null || data.getInteger("Code").equals(0)) {

                return new Response(data.getJSONArray("Data"));
            }else{
                throw new GlobalException(ResponseCode.CALL_WX_ERROR);

            }
        } else {
            throw new GlobalException(ResponseCode.CALL_SERVICE_ERROR);
        }
    }

    /**
     * 封装请求对象HttpEntity，主要是token、请求参数
     *
     * @param paramMap
     * @return
     */
    public HttpEntity<Map<String, Object>> loadHttpEntity(Map<String, Object> paramMap) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("AOToken", paramMap.get("AOToken").toString());
        headers.add("AOKey", paramMap.get("AOKey").toString());
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>( headers);
        return httpEntity;
    }

    /**
     * 封装请求对象HttpEntity，主要是token、请求参数
     *
     * @param paramMap
     * @return
     */
    public HttpEntity<Map<String, Object>> loadBodyHttpEntity(Map<String, Object> paramMap,Map<String, Object> headMap) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("AOToken", headMap.get("AOToken").toString());
        headers.add("AOKey", headMap.get("AOKey").toString());
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>( paramMap,headers);
        return httpEntity;
    }


    /**
     * POST方式调用，传body对象
     *
     * @param url
     * @param paramMap
     * @return
     */
    public Response postBody(String url, Map<String, Object> paramMap,Map<String, Object> headMap) throws GlobalException {
        Response response = new Response();

        HttpEntity<Map<String, Object>> httpEntity = loadBodyHttpEntity(paramMap,headMap);
        JSONObject responseJson = new JSONObject();
        try {
            responseJson = nRestTemplate.postForObject(url, httpEntity, JSONObject.class);
            if (responseJson == null) {
                logger.error("调用微服务接口失败：{}", url + "\n响应为null");
            }
        } catch (RestClientException e) {
            logger.error("调用微服务接口失败：{}", url + "\n" + e);
            response.setCode(responseJson.getInteger("code"));
            response.setMsg(responseJson.getString("msg"));
            return response;
        }
        if (responseJson == null) {
            return new Response(new GlobalException(ResponseCode.CALL_SERVICE_ERROR));
        }
        if (responseJson.get("Code")!=null || responseJson.getInteger("Code").equals(0)) {

            return new Response(responseJson.getJSONObject("Result"));
        }else{
            throw new GlobalException(ResponseCode.CALL_WX_ERROR);

        }
    }
    /**
     * 打印请求参数
     *
     * @param url
     * @param paramMap
     */
    public void showRequestLog(String url, Map<String, Object> paramMap) {
        logger.info("\n\n\n"
                + "请求url=" + url + "\n"
                + "请求参数paramsMap=" + JSONUtil.outputJson(new JSONObject(paramMap).toJSONString()) + "\n"
                + "\n\n\n");
    }
    

    /**
     * 打印响应结果
     *
     * @param jsonString
     */
    public void showResponseLog(String jsonString) {
        logger.info("\n\n\n" + "接口响应：" + JSONUtil.outputJson(jsonString) + "\n\n\n");
    }
    public String loadToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getHeader("Authorization");
    }
    
}
