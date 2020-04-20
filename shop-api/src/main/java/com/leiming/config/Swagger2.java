package com.leiming.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Create by LovelyLM
 * 2020/4/1 0:26
 * V 1.0
 * @author Leiming
 * api文档框架
 */

@Configuration
@EnableSwagger2
public class Swagger2 {
    @Bean
     public Docket createRestApi() {

         return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                 .apis(RequestHandlerSelectors.basePackage("com.leiming.controller"))
                 .paths(PathSelectors.any()).build();
     }

     private ApiInfo apiInfo() {
         return new ApiInfoBuilder().title("平台接口api")
                 .contact(new Contact("雷鸣","https://www.leiming.com",
                         "1079626899@qq.com"))
                 .description("api文档")
                 .version("1.0.0")
                 .termsOfServiceUrl("https://www.leiming.com")
                 .build();
     }
}
