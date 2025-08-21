package br.com.fiap.msestoque.adapter.exception;

import br.com.fiap.msestoque.core.exception.EstoqueException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(
            jakarta.validation.ConstraintViolationException ex, WebRequest request) {

        ProblemType problemType = ProblemType.DADOS_INVALIDOS;
        String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";

        List<Problem.Field> problemFields = ex.getConstraintViolations().stream()
                .map(violation -> Problem.Field.builder()
                        .name(violation.getPropertyPath().toString())
                        .userMessage(violation.getMessage())
                        .build())
                .toList();

        Problem problem = createProblemBuilder(HttpStatus.BAD_REQUEST, problemType, detail)
                .userMessage(detail)
                .fields(problemFields)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ProblemType problemType = ProblemType.DADOS_INVALIDOS;
        String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";

        BindingResult bindingResult = ex.getBindingResult();
        List<Problem.Field> problemFilds = bindingResult
                .getFieldErrors()
                .stream()
                .map(problemFild -> Problem.Field.builder()
                        .name(problemFild.getField())
                        .userMessage(problemFild.getDefaultMessage())
                        .build()).toList();


        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .fields(problemFilds)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }
//
    @ExceptionHandler(EstoqueException.class)
    public ResponseEntity<Object> handleEstoqueExceptionException(EstoqueException e, WebRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;

        ProblemType problemaType = ProblemType.RECURSO_NAO_ENCONTRADO;
        String detail = e.getMessage();
        Problem problema = createProblemBuilder(status,problemaType, detail)
                .userMessage(detail)
                .build();
        return handleExceptionInternal(e, problema, new HttpHeaders(), status, request);
    }

    private Problem.ProblemBuilder createProblemBuilder(HttpStatusCode status, ProblemType problemaType, String detail){

        return Problem.builder()
                .status(status.value())
                .type(problemaType.getUri())
                .title(problemaType.getTitle())
                .timestamp(LocalDateTime.now())
                .detail(detail);
    }

    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ProblemType problemaType = ProblemType.RECURSO_NAO_ENCONTRADO;
        String detail = String.format("O recurso %s, que você tentou acessar, é inexistente.",ex.getRequestURL());

        Problem problem = createProblemBuilder(status, problemaType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

}