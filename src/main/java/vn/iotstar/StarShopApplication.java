package vn.iotstar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import vn.iotstar.config.MySiteMeshFilter;

@SpringBootApplication
public class StarShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarShopApplication.class, args);
	}
	@Bean
	public FilterRegistrationBean<MySiteMeshFilter> siteMeshFilter(){
		FilterRegistrationBean<MySiteMeshFilter> registrationBean = new FilterRegistrationBean<MySiteMeshFilter>();
		registrationBean.setFilter(new MySiteMeshFilter());
		registrationBean.addUrlPatterns("/*");
				
		return registrationBean;
	}
}
