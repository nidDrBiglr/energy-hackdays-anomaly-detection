package ch.opendata.energy.errors;

import io.opentracing.Span;
import io.opentracing.util.GlobalTracer;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {
  @Override
  public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
    Map<String, Object> map = super.getErrorAttributes(request, includeStackTrace);

    Object errorId = request.exchange().getAttribute(GlobalErrorWebExceptionHandler.ERROR_ID);
    map.put("errorId", errorId);

    Span span = GlobalTracer.get().activeSpan();
    if (span != null) {
      map.put("traceId", span.context().toTraceId());
    }

    Throwable exception = getError(request);
    if (exception instanceof WebExchangeBindException) {
      WebExchangeBindException webExchangeBindException = (WebExchangeBindException) exception;

      BindingResult result = webExchangeBindException.getBindingResult();
      List<FieldError> fieldErrors = result.getFieldErrors();
      List<HashMap<String, String>> errors = new ArrayList<>();
      for (FieldError fieldError : fieldErrors) {
        HashMap<String, String> error = new HashMap<>();
        String validationMessage = fieldError.getDefaultMessage();
        // add the validation message, for example property: "NotNull.a1.name", message: "a should not be null"
        error.put("property", fieldError.getObjectName() + "." + fieldError.getField());
        error.put("message", validationMessage);
        errors.add(error);
      }
      map.put("message", "Validation failed");
      map.put("validationErrors", errors);
      map.put("status", HttpStatus.BAD_REQUEST.value());
      map.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());

      // Remove too detailed error code
      map.remove("errors");
      return map;
    }

    if (exception instanceof ServerWebInputException) {
      ServerWebInputException serverWebInputException = (ServerWebInputException) exception;
      map.put("message", serverWebInputException.getReason());
    }

    if (exception instanceof ResponseStatusException) {
      return map;
    }

    if (exception instanceof WebClientResponseException) {
      map.put("status", ((WebClientResponseException) exception).getRawStatusCode());
      return map;
    }

    //unhandled exception
    map.put("message", "Something went wrong!");
    map.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    map.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

    return map;
  }
}
