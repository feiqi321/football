package org.spring.springboot.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
@Data
public class OrderDto implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.id
     *
     * @mbggenerated
     */
    private Integer id;

    private Integer userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.type
     *类型
     * @mbggenerated
     */
    private Integer type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.startAddress
     *出发地
     * @mbggenerated
     */
    private String startaddress;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.targetAddress
     *目的地
     * @mbggenerated
     */
    private String targetaddress;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.personNum
     *人数
     * @mbggenerated
     */
    private Integer personnum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.startTime
     *出发时间
     * @mbggenerated
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date starttime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.name
     *姓名
     * @mbggenerated
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.sex
     *性别
     * @mbggenerated
     */
    private Integer sex;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.remark
     *备注
     * @mbggenerated
     */
    private String remark;


    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.updator
     *
     * @mbggenerated
     */
    private String updator;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.phone
     *电话
     * @mbggenerated
     */
    private String phone;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table order
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order.id
     *
     * @return the value of order.id
     *
     * @mbggenerated
     */

}