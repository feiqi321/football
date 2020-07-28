package org.spring.springboot.domain;

import lombok.Data;

/**
 * Created by betty on 19/07/2020.
 */
@Data
public class Auth {

    private String username;

    private String password;

    private Integer marketTypeId;

    private Integer sportsType;

    private Integer leagues;

    private String bookies;

    private String gameId;

    private double amount;

    private String bookieOdds;

    private String homeName;//主队信息

    private String guestName;//客队信息

    private String ycDate;//比赛时间




}
