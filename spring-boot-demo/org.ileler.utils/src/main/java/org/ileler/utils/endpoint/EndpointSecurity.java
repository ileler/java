/**
 * @(#)ActuatorSecurity 1.0 2018/7/25
 * @Copyright: Copyright 2007 - 2018 MPR Tech. Co. Ltd. All Rights Reserved.
 * @Description: Modification History:
 * Date:        2018/7/25
 * Author:      sunsz
 * Version:     1.0.0.0
 * Description: (Initialize)
 * Reviewer:
 * Review Date:
 */
package org.ileler.utils.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Author: kerwin612
 */
@EnableWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class EndpointSecurity extends WebSecurityConfigurerAdapter {

    @Value("${security.endpoint.exclud:}")
    String[] excluds;

    @Value("${security.user.name:ops_manager}")
    String username;

    @Value("${security.user.password:ops_microservice}")
    String password;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().requestMatchers(EndpointRequest.toAnyEndpoint().excluding(excluds)).authenticated().and().csrf().disable().httpBasic();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser(username).password("{noop}" + password).roles("ENDPOINT_ADMIN");
    }

}
