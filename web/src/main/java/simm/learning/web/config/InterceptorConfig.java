package simm.learning.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import simm.learning.web.interceptors.FirstInterceptor;
import simm.learning.web.interceptors.SecondInterceptor;

@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private FirstInterceptor firstInterceptor;
    @Autowired
    private SecondInterceptor secondInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(firstInterceptor).addPathPatterns("/**");
        registry.addInterceptor(secondInterceptor).addPathPatterns("/**");
    }
}
