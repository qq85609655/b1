/**
 * 
 */
package com.gtafe.framework.swagger;

import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.AbstractPathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * ClassName: ApplicationSwaggerConfig <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2017年7月20日 下午12:54:58 <br/> 
 * 
 * @author liquan.feng 
 * @version  
 * @since JDK 1.7
 */  
/** 
 *  ApplicationSwaggerConfig's history:
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2017年7月20日 下午12:54:58 <br/> 
 * 
 * @author liquan.feng 
 * @version  
 * @since JDK 1.7
 */
@EnableSwagger2
public class ApplicationSwaggerConfig { 
    
    /**
     * 应用路径 
     */
    private String applicationPath = "/";
    
    /**
     * 版本
     */
    private String version = "1.0.0";
    
    /**
     * 
     */
    private String documentationPath = "/";
    
    /**
     * swagger扫描包的根路径
     */
    private String swaggerScanBasePackage = "";
    
    /**
     * 主机地址
     */
    private String host = "localhost:8080";
    
    /**
     * 文档标题
     */
    private String title = "API";
    
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .host(this.host)
                .pathProvider(new BDCPathProvider(this.applicationPath, this.documentationPath))
                .select()
                .apis(RequestHandlerSelectors.basePackage(this.swaggerScanBasePackage))//api接口包扫描路径
                .paths(PathSelectors.any())//可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build();
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title(this.title)//设置文档的标题
            .version(this.version)//设置文档的版本信息-> 1.1 Version information
            .build();
    }
    
    private static class BDCPathProvider extends AbstractPathProvider{

        /**
         * 应用路径
         */
        private String applicationPath = "/";
        
        /**
         * 
         */
        private String documentationPath = "/";
    	
    	public BDCPathProvider(String applicationPath, String documentationPath) {
			this.applicationPath = applicationPath;
			this.documentationPath = documentationPath;
		}
    	
		@Override
		protected String applicationPath() {
			return applicationPath;
		}

		@Override
		protected String getDocumentationPath() {
			return this.documentationPath;
		}
		
    }

	public String getApplicationPath() {
		return applicationPath;
	}

	public void setApplicationPath(String applicationPath) {
		this.applicationPath = applicationPath;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDocumentationPath() {
		return documentationPath;
	}

	public void setDocumentationPath(String documentationPath) {
		this.documentationPath = documentationPath;
	}

	public String getSwaggerScanBasePackage() {
		return swaggerScanBasePackage;
	}

	public void setSwaggerScanBasePackage(String swaggerScanBasePackage) {
		this.swaggerScanBasePackage = swaggerScanBasePackage;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

    
}
