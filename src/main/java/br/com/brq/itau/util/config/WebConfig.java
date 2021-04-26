package br.com.brq.itau.util.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
@Import(SwaggerConfig.class)
public class WebConfig implements WebMvcConfigurer {
	
	/**
	 * Configuracao do content negotiation.
	 * Define os tipos de midias e cabecalhos
	 * @param configurer the configurer
	 */
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer ) {
		configurer
		.favorParameter(false)
		.ignoreAcceptHeader(false)
		.parameterName("mediaType")
		.useRegisteredExtensionsOnly(false)
		.defaultContentType(MediaType.APPLICATION_JSON)
		.mediaType("json", MediaType.APPLICATION_JSON);
	}
	
	@Bean
    public LocaleResolver localeResolver() {
        return new FixedLocaleResolver(new Locale("pt", "BR"));
    }
	
	@Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
	    registry.addRedirectViewController("/documentation/v2/api-docs", "/v2/api-docs");
	    registry.addRedirectViewController("/documentation/configuration/ui", "/configuration/ui");
	    registry.addRedirectViewController("/documentation/configuration/security", "/configuration/security");
	    registry.addRedirectViewController("/documentation/swagger-resources", "/swagger-resources");
	    registry.addRedirectViewController("/swagger-ui", "/swagger-ui/index.html");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("/swagger-ui").addResourceLocations("classpath:/META-INF/resources/swagger-ui/");
	    registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

}