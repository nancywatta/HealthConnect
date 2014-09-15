package aucklanduni.ece.hc.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import com.wordnik.swagger.model.ApiInfo;
 /**
  * 
 * @ClassName: HCSpringSwaggerConfig 
 * @Description: This is the configuration for swagger ui
 * This defines all the services we want to expose:/service/.*
 * @author Zhao Yuan
 * @date 2014年9月15日 下午9:23:13 
 *
  */
@Configuration
@EnableSwagger
public class HCSpringSwaggerConfig {
	private SpringSwaggerConfig springSwaggerConfig;

	   /**
	    * Required to autowire SpringSwaggerConfig
	    */
	   @Autowired
	   public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
	      this.springSwaggerConfig = springSwaggerConfig;
	   }

	   /**
	    * Every SwaggerSpringMvcPlugin bean is picked up by the swagger-mvc framework - allowing for multiple
	    * swagger groups i.e. same code base multiple swagger resource listings.
	    */
	   @Bean
	   public SwaggerSpringMvcPlugin customImplementation(){
	      return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
	              .includePatterns("/service/.*");
	   }
	   
	    @SuppressWarnings("unused")
		private ApiInfo apiInfo() {
		      ApiInfo apiInfo = new ApiInfo(
		              "HealthConnect API",
		              "API for HealthConnect",
		              "HealthConnect API terms of service",
		              "zyua826@aucklanduni.ac.nz",
		              "UoA API Licence Type",
		              "UoA API License URL"
		        );
		      return apiInfo;
		    }
}
