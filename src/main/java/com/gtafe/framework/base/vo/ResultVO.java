/** 
 * Project Name:GTADataCenter 
 * File Name:ResultVO.java 
 * Package Name:com.gtafe.framework.base.vo 
 * Date:2017年7月21日上午10:57:42 
 * Copyright (c) 2017, GTA All Rights Reserved. 
 * 
 */  
package com.gtafe.framework.base.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/** 
 * ClassName: ResultVO <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2017年7月21日 上午10:57:42 <br/> 
 * 
 * @author liquan.feng 
 * @version  
 * @since JDK 1.7
 */
/** 
 *  ResultVO's history:
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2017年7月21日 上午10:57:42 <br/> 
 * 
 * @author liquan.feng 
 * @version  
 * @since JDK 1.7
 */
@ApiModel
public class ResultVO<T> implements Serializable{
	/** 
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
	 * @since JDK 1.7
	 */  
	private static final long serialVersionUID = 1L;

	/**
	 * 返回吗：错误状态码为负数，正确状态码为正数
	 */
	@ApiModelProperty(value = "返回码", required = false)
	private int code;
	
	/**
	 * 信息：状态码对应的简短描述，通常可以作为用户提示使用
	 */
	@ApiModelProperty(value = "返回码提示", required = false)
	private String msg;
	
	/**
	 * 描述：结果描述，通常较为详细，可包含错误解决方案描述
	 */
	@ApiModelProperty(value = "返回码描述", required = false)
	private String desc;
	
	/**
	 * 数据：结果包含的数据，通常当code为负数（错误）时，data为null
	 */
	@ApiModelProperty(value = "返回数据，通常在返回码为0时方存在", required = false)
	private T data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	
}
