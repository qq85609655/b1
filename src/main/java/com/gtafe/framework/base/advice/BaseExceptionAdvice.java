/**
 * 项目名称:   GTADataCenter        	<br>
 * 包  名 称:   com.gtafe.framework.base.advice   	<br>
 * 文件名称:   BasePackageAdvice.java     <br>
 *
 * 修改履历:
 *       日期                            修正者        主要内容   <br>
 *       2017年9月14日            张中伟        初版做成    <br>
 *
 */
package com.gtafe.framework.base.advice;


import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.datasource.DataSourceException;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.executor.BatchExecutorException;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.result.ResultMapException;
import org.apache.ibatis.logging.LogException;
import org.apache.ibatis.parsing.ParsingException;
import org.apache.ibatis.plugin.PluginException;
import org.apache.ibatis.scripting.ScriptingException;
import org.apache.ibatis.session.SqlSessionException;
import org.apache.ibatis.transaction.TransactionException;
import org.apache.ibatis.type.TypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gtafe.framework.base.controller.WebResult;
import com.gtafe.framework.base.exception.BaseException;


/**
 * 名称：BasePackageAdvice <br>
 * 描述：〈功能详细描述〉<br>
 * @author 张中伟
 * @version 1.0
 * @since 1.0.0
 */
// @ControllerAdvice(value = {"com.joyintech", "com.ibw.report"}) // 包名不能使用通配符
// 该配置对所有com.joyintech 包下的类适用。 如果某项目需要单独配置，可以单独添加一个类，使用 @ControllerAdvice(value={"packagename"})
// 方式处理。
@ControllerAdvice(value = {"com.gtafe"})
public class BaseExceptionAdvice {

    /**
     * 日志信息
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(
        BaseExceptionAdvice.class);

    /**
     * 异常处理 待细化处理<br>
     * 这里处理一个所有异常信息，以保证未知异常可以被系统拦截<br>
     * @param exception 所有未知异常信息
     * @return 未知异常及异常信息
     */
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = Exception.class)
    public WebResult errorResponse(Exception exception) {
        LOGGER.error("Exception:" + exception.getMessage(), exception);
        WebResult wb = null;

        // 默认为未知异常
        wb = new WebResult();
        wb.setSuccess(false);
        wb.setException(true);
        wb.setExpCode(BaseException.DEF_EXPCODE);
        wb.setExpInfo(wb.getExpInfo());
        return wb;

    }

    /**
     * 
     * 主要功能:    处理自定义异常（未细化的自定义异常） <br>
     * 注意事项:无  <br>
     * 
     * @param exception  自定义的异常
     * @return  标准WebResult
     */
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = BaseException.class)
    public WebResult baseExceptionResponse(BaseException exception) {

        WebResult wb = new WebResult();

        wb.setSuccess(false);
        wb.setException(true);
        wb.setExpCode(exception.getExpCode());
        wb.setExpInfo(exception.getExpInfo());
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("BaseException:" + exception.getMessage(), exception);
        }
        return wb;
    }

    /**
     * 
     * 主要功能:   处理 MyBatis 已封装的异常  <br>
     * 注意事项:  拦截MyBatis 异常基类，并处理为标准结果结果。 <br>
     * 
     * @param exception  异常
     * @return 标准结果
     */
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = PersistenceException.class)
    public WebResult baseExceptionResponse(PersistenceException exception) {
        LOGGER.error("PersistenceException:" + exception.getMessage(), exception);
        return handlerDbException(exception);
    }

    /**
     * 主要功能: 处理数据库异常（仅处理mybatis 异常）<br>
     * 注意事项:无  <br>
     * 
     * @param pe
     * @return
     */
    private static WebResult handlerDbException(PersistenceException pe) {

        /**
         * 按PersistenceException的子类  从下往上分析类，转换异常信息
         */
        if (pe instanceof TypeException) {
            return genExceptionInfo("类型错误", "GTA-02003", "mybatis的类型转换异常", pe);
        }

        if (pe instanceof TransactionException) {
            return genExceptionInfo("事务异常", "GTA-02004", "mybatis的事务异常", pe);

        }

        if (pe instanceof TooManyResultsException) {
            return genExceptionInfo("返回结果过多", "GTA-02005", "返回结果多于预期结果", pe);
        }

        if (pe instanceof SqlSessionException) {
            return genExceptionInfo("sql会话异常", "GTA-02006", "sql会话出现异常", pe);
        }

        if (pe instanceof PluginException) {
            return genExceptionInfo("反射异常", "GTA-02007", "JAVA反射异常", pe);
        }

        if (pe instanceof ParsingException) {
            return genExceptionInfo("解析异常", "GTA-02008", "结果解析异常", pe);
        }

        if (pe instanceof LogException) {
            return genExceptionInfo("记录异常", "GTA-02009", "mybatis Log异常", pe);
        }

        // 是 ExecutorException 的子类
        if (pe instanceof BatchExecutorException) {
            return genExceptionInfo("批量执行异常", "GTA-02010", "批量执行异常", pe);
        }

        if (pe instanceof ExecutorException) {
            return genExceptionInfo("执行异常", "GTA-02011", "执行异常", pe);
        }

        // 理论上数据源异常包括网络断开、连接池已满等 （mybatis 未细分）
        if (pe instanceof DataSourceException) {
            return genExceptionInfo("数据源异常", "GTA-02012", "数据源异常", pe);
        }

        // 未配置mybatis 缓存，所以缓存异常理论上不存在。
        if (pe instanceof CacheException) {
            return genExceptionInfo("缓存异常", "GTA-02013", "mybatis缓存异常", pe);
        }

        if (pe instanceof IncompleteElementException) {
            return genExceptionInfo("元素不完整", "GTA-02014", "元素不完整异常", pe);
        }

        if (pe instanceof BuilderException) {
            return genExceptionInfo("SQL构建异常", "GTA-02015", "SQL构建异常", pe);
        }

        if (pe instanceof BindingException) {
            return genExceptionInfo("参数绑定异常", "GTA-02016", "参数绑定异常", pe);
        }

        if (pe instanceof ScriptingException) {
            return genExceptionInfo("脚本异常", "GTA-02017", "脚本异常", pe);
        }

        if (pe instanceof ResultMapException) {
            return genExceptionInfo("结果映射异常", "GTA-02018", "结果映射异常", pe);
        }

        // 如果没处理到，生成一个未知异常。
        return genExceptionInfo("mybatis异常", "GTA-02999", "mybatis的其他异常", pe);
    }

    /**
     * 
     * 主要功能:   生成一个异常结果  <br>
     * 注意事项:无  <br>
     * 
     * @param title  异常标题 
     * @param code 异常编码
     * @param message 异常信息
     * @param exception  原抛出异常
     * @return  控制器结果
     */
    private static WebResult genExceptionInfo(String title, String code,
                                              String message,
                                              Throwable exception) {
        WebResult result = new WebResult(code, title);
        result.setExpCode(code);
        result.setExpInfo(message);
        result.setExpDetail(title);
        result.setSuccess(false);
        result.setException(true);
        return result;
    }
}
