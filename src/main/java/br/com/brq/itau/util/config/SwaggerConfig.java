package br.com.brq.itau.util.config;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;

import com.fasterxml.classmate.TypeResolver;

import br.com.brq.itau.util.excecao.Problema;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.TagsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

public class SwaggerConfig  {

	public static final String AUTHORIZATION_HEADER = "Authorization";

	@Bean
	public Docket produtoApi() {
		TypeResolver typeResolver = new TypeResolver();

		return new Docket(DocumentationType.OAS_30).select()
				.apis(RequestHandlerSelectors.basePackage("br.com.brq.itau.controlador.api"))
				.paths(PathSelectors.any()).build()
				
				.securitySchemes(Arrays.asList(securityScheme()))
				.securityContexts(Arrays.asList(securityContext()))
				.additionalModels(typeResolver.resolve(Problema.class))
				.apiInfo(metaData())
				.genericModelSubstitutes(Optional.class)
				.genericModelSubstitutes(ResponseEntity.class)
				.directModelSubstitute(LocalDate.class, java.sql.Date.class)
				.directModelSubstitute(OffsetDateTime.class, java.util.Date.class).pathMapping("/")
				.useDefaultResponseMessages(false)
				.tags(new Tag("Endereço", "EndPoint De Endereços"));
	}

	private ApiInfo metaData() {
		return new ApiInfoBuilder().title("Itaú Api").description("API do Itaú").version("0.0.1").build();
	}

	public ApiInfo apiInfo() {
		final ApiInfoBuilder builder = new ApiInfoBuilder();
		builder.title("").version("").license("").description("");
		return builder.build();
	}

	@Bean
	UiConfiguration uiConfig() {
		return UiConfigurationBuilder.builder()
				.deepLinking(true)
				.displayOperationId(false)
				.defaultModelsExpandDepth(1)
				.defaultModelExpandDepth(1)
				.defaultModelRendering(ModelRendering.EXAMPLE)
				.displayRequestDuration(true)
				.docExpansion(DocExpansion.NONE)
				.filter(true)
				.maxDisplayedTags(null)
				.operationsSorter(OperationsSorter.ALPHA)
				.showExtensions(false)
				.showCommonExtensions(false)
				.tagsSorter(TagsSorter.ALPHA)
				.supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
				.validatorUrl(null).build();
	}
	
	private SecurityScheme securityScheme() {
		return new OAuthBuilder()
				.name("Modelo")
				.grantTypes(grantTypes())
				.scopes(scopes())
				.build();
	}
	
	@SuppressWarnings("deprecation")
	private SecurityContext securityContext() {
		var securityReference = SecurityReference.builder()
				.reference("Modelo")
				.scopes(scopes().toArray(new AuthorizationScope[0]))
				.build();
		
		return SecurityContext.builder()
				.securityReferences(Arrays.asList(securityReference))
				.forPaths(PathSelectors.any())
				.build();
	}
	
	private List<GrantType> grantTypes() {
		return Arrays.asList(new ResourceOwnerPasswordCredentialsGrant("/oauth/token"));
	}
	
	private List<AuthorizationScope> scopes() {
		return Arrays.asList(new AuthorizationScope("READ", "Acesso de leitura"),
				new AuthorizationScope("WRITE", "Acesso de escrita"));
	}

}