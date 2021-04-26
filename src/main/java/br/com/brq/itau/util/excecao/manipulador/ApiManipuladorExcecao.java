package br.com.brq.itau.util.excecao.manipulador;

import java.nio.file.AccessDeniedException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

import br.com.brq.itau.util.excecao.EntidadeEmUsoExcecao;
import br.com.brq.itau.util.excecao.EntidadeNaoEncontradaExcecao;
import br.com.brq.itau.util.excecao.NegocioExcecao;
import br.com.brq.itau.util.excecao.Problema;
import br.com.brq.itau.util.excecao.TipoProblema;

@ControllerAdvice
public class ApiManipuladorExcecao extends ResponseEntityExceptionHandler {

	public static final String MSG_ERRO_GENERICA_USUARIO_FINAL
		= "Ocorreu um erro interno inesperado no sistema. Tente novamente e se "
				+ "o problema persistir, entre em contato com o administrador do sistema.";
	
	@Autowired
	private MessageSource recursoMensagem;
	
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return ResponseEntity.status(status).headers(headers).build();
	}
	
	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		
		return handleValidationInternal(ex, headers, status, request, ex.getBindingResult());
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

	    return handleValidationInternal(ex, headers, status, request, ex.getBindingResult());
	}

	private ResponseEntity<Object> handleValidationInternal(Exception ex, HttpHeaders headers,
			HttpStatus status, WebRequest request, BindingResult bindingResult) {
		TipoProblema problemType = TipoProblema.DADOS_INVALIDOS;
	    String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";
	    
	    List<Problema.Object> problemObjects = bindingResult.getAllErrors().stream()
	    		.map(objectError -> {
	    			String message = recursoMensagem.getMessage(objectError, LocaleContextHolder.getLocale());
	    			
	    			String name = objectError.getObjectName();
	    			
	    			if (objectError instanceof FieldError) {
	    				name = ((FieldError) objectError).getField();
	    			}
	    			
	    			return Problema.Object.builder()
	    				.nome(name)
	    				.mensagemUsuario(message)
	    				.build();
	    		})
	    		.collect(Collectors.toList());
	    
	    Problema problem = criaProblemaBuilder(status, problemType, detail)
	        .mensagemUsuario(detail)
	        .objetos(problemObjects)
	        .build();
	    
	    return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	@ExceptionHandler({ InvalidDataAccessApiUsageException.class })
	public ResponseEntity<Object> handleConflict(final InvalidDataAccessApiUsageException ex,
			final WebRequest requisicao) {
		logger.error("Código 409", ex);
		final String corpoResposta = "Não pode fazer uso dos dados da API!";
		return handleExceptionInternal(ex, corpoResposta, new HttpHeaders(), HttpStatus.CONFLICT, requisicao);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;		
		TipoProblema problemType = TipoProblema.ERRO_DE_SISTEMA;
		String detail = MSG_ERRO_GENERICA_USUARIO_FINAL;

		logger.error(ex.getMessage(), ex);
		
		Problema problem = criaProblemaBuilder(status, problemType, detail)
				.mensagemUsuario(detail)
				.build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, 
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		TipoProblema problemType = TipoProblema.RECURSO_NAO_ENCONTRADO;
		String detail = String.format("O recurso %s, que você tentou acessar, é inexistente.", 
				ex.getRequestURL());
		
		Problema problem = criaProblemaBuilder(status, problemType, detail)
				.mensagemUsuario(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		if (ex instanceof MethodArgumentTypeMismatchException) {
			return handleMethodArgumentTypeMismatch(
					(MethodArgumentTypeMismatchException) ex, headers, status, request);
		}
	
		return super.handleTypeMismatch(ex, headers, status, request);
	}
	
	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
			MethodArgumentTypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		TipoProblema problemType = TipoProblema.PARAMETRO_INVALIDO;

		String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', "
				+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

		Problema problem = criaProblemaBuilder(status, problemType, detail)
				.mensagemUsuario(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Throwable rootCause = ExceptionUtils.getRootCause(ex);
		
		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
		} else if (rootCause instanceof PropertyBindingException) {
			return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request); 
		}
		
		TipoProblema problemType = TipoProblema.MENSAGEM_INCOMPREENSIVEL;
		String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";
		
		Problema problem = criaProblemaBuilder(status, problemType, detail)
				.mensagemUsuario(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String path = joinPath(ex.getPath());
		
		TipoProblema problemType = TipoProblema.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format("A propriedade '%s' não existe. "
				+ "Corrija ou remova essa propriedade e tente novamente.", path);

		Problema problem = criaProblemaBuilder(status, problemType, detail)
				.mensagemUsuario(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String path = joinPath(ex.getPath());
		
		TipoProblema problemType = TipoProblema.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format("A propriedade '%s' recebeu o valor '%s', "
				+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				path, ex.getValue(), ex.getTargetType().getSimpleName());
		
		Problema problem = criaProblemaBuilder(status, problemType, detail)
				.mensagemUsuario(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
//	@ExceptionHandler({ ConstraintViolationException.class })
//	public ResponseEntity<Object> manipuladorViolacaoConstraint(final ConstraintViolationException ex,
//			final WebRequest requisicao) {
//		logger.error(ex.getClass().getName());
//		final List<String> erros = new ArrayList<>();
//		for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
//			erros.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": "
//					+ violation.getMessage());
//		}
//		final Problema apiErro = new Problema(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), erros, dataHora);
//		return new ResponseEntity<>(apiErro, new HttpHeaders(), apiErro.getEstadoHttp());
//	}
	
	@ExceptionHandler({ DataAccessException.class })
	protected ResponseEntity<Object> manipuladorConflito(final DataAccessException ex, final WebRequest requisicao) {
		logger.error("Código 409", ex);
		final String corpoResposta = "Não pode fazer uso dos dados (DTException):" + ex.getLocalizedMessage();
		return handleExceptionInternal(ex, corpoResposta, new HttpHeaders(), HttpStatus.CONFLICT, requisicao);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontrada(AccessDeniedException ex, WebRequest request) {

		HttpStatus status = HttpStatus.FORBIDDEN;
		TipoProblema problemType = TipoProblema.ACESSO_NEGADO;
		String detail = ex.getMessage();

		Problema problem = criaProblemaBuilder(status, problemType, detail)
				.mensagemUsuario(detail)
				.mensagemUsuario("Você não possui permissão para executar essa operação.")
				.build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
//	@Override
//	public ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex,
//			final HttpHeaders cabecalho, final HttpStatus estadoHttp, final WebRequest requisicao) {
//		logger.error(ex.getClass().getName());
//		final StringBuilder construtor = new StringBuilder();
//		construtor.append(ex.getContentType());
//		construtor.append("Tipo de media nao suportado.O tipo suportado e: ");
//		ex.getSupportedMediaTypes().forEach(t -> construtor.append(t + " "));
//
//		final ApiErro apiErro = new ApiErro(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getLocalizedMessage(),
//				construtor.substring(0, construtor.length() - 1), dataHora);
//		return new ResponseEntity<>(apiErro, new HttpHeaders(), apiErro.getEstadoHttp());
//	}
	
	@ExceptionHandler(EntidadeNaoEncontradaExcecao.class)
	public ResponseEntity<?> handleEntidadeNaoEncontrada(EntidadeNaoEncontradaExcecao ex,
			WebRequest request) {
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		TipoProblema problemType = TipoProblema.RECURSO_NAO_ENCONTRADO;
		String detail = ex.getMessage();
		
		Problema problem = criaProblemaBuilder(status, problemType, detail)
				.mensagemUsuario(detail)
				.build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(EntidadeEmUsoExcecao.class)
	public ResponseEntity<?> handleEntidadeEmUso(EntidadeEmUsoExcecao ex, WebRequest request) {
		
		HttpStatus status = HttpStatus.CONFLICT;
		TipoProblema problemType = TipoProblema.ENTIDADE_EM_USO;
		String detail = ex.getMessage();
		
		Problema problem = criaProblemaBuilder(status, problemType, detail)
				.mensagemUsuario(detail)
				.build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(NegocioExcecao.class)
	public ResponseEntity<?> handleNegocio(NegocioExcecao ex, WebRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		TipoProblema problemType = TipoProblema.ERRO_NEGOCIO;
		String detail = ex.getMessage();
		
		Problema problem = criaProblemaBuilder(status, problemType, detail)
				.mensagemUsuario(detail)
				.build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		if (body == null) {
			body = Problema.builder()
				.dataHora(OffsetDateTime.now())
				.titulo(status.getReasonPhrase())
				.estadoHttp(status.value())
				.mensagemUsuario(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();
		} else if (body instanceof String) {
			body = Problema.builder()
				.dataHora(OffsetDateTime.now())
				.titulo((String) body)
				.estadoHttp(status.value())
				.mensagemUsuario(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();
		}
		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
	
	private Problema.ProblemaBuilder criaProblemaBuilder(HttpStatus status,
			TipoProblema problemType, String detail) {
		
		return Problema.builder()
			.dataHora(OffsetDateTime.now())
			.estadoHttp(status.value())
			.tipo(problemType.getUri())
			.titulo(problemType.getTitulo())
			.detalhe(detail);
	}

	private String joinPath(List<Reference> references) {
		return references.stream()
			.map(ref -> ref.getFieldName())
			.collect(Collectors.joining("."));
	}
	
}
