package org.spring.springboot.dao;

import org.mapstruct.Mapper;
import org.spring.springboot.domain.Auth;
import org.spring.springboot.domain.Result;
import java.util.List;

/**
 * Created by betty on 18/07/2020.
 */
@Mapper
public interface ResultDao {

    List<Result> list(Result result);

    List<Auth> userinfo();

    List<Auth> listYc();

    List<Auth> listMatchByGameId(Auth auth);

    void saveMatch(Auth auth);

}
