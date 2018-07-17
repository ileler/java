package org.ileler.utils.swagger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Author: kerwin612
 */
@EnableSwagger2
@Profile({ "dev", "test" })
@ConditionalOnClass(WebMvcConfigurerAdapter.class)
public class SwaggerMVCConfiguration extends WebMvcConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerMVCConfiguration.class);

    @Value("${spring.application.name}")
    private String appName;

    @Value("${swagger.description:}")
    private String description;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        LOGGER.info("add swagger controller");
        registry.addViewController("/").setViewName("redirect:/swagger-ui.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Bean
    public Docket docket() {
        LOGGER.info("build swagger docket");
        return new Docket(DocumentationType.SWAGGER_2).groupName(appName).genericModelSubstitutes(DeferredResult.class)
                // .genericModelSubstitutes(ResponseEntity.class)
                .useDefaultResponseMessages(false).forCodeGeneration(false).pathMapping("/").select()
                .apis(RequestHandlerSelectors.basePackage(appName + ".controller")).paths(PathSelectors.any()).build()
                .apiInfo(new ApiInfoBuilder().title(appName + " API")// 大标题
                        .description(description)// 详细描述
                        .build());
    }

}

