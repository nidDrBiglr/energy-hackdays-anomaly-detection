package ch.opendata.energy.errors;

import io.opentracing.Span;
import io.opentracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

import static net.logstash.logback.argument.StructuredArguments.kv;
import static net.logstash.logback.encoder.org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

@Component
@Order(-2)
@Slf4j
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {
  public static final String ERROR_ID = "errorId";
  @Autowired
  private Environment environment;

  @Autowired
  private Tracer tracer;

  public GlobalErrorWebExceptionHandler(GlobalErrorAttributes errorAttributes, ApplicationContext applicationContext, ServerCodecConfigurer serverCodecConfigurer) {
    super(errorAttributes, new ResourceProperties(), applicationContext);
    super.setMessageWriters(serverCodecConfigurer.getWriters());
    super.setMessageReaders(serverCodecConfigurer.getReaders());
  }

  @Override
  protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
    return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
  }

  private Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {
    final Map<String, Object> errorAttributes = getErrorAttributes(request, false);

    return ServerResponse
      .status(getHttpStatus(errorAttributes))
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue(errorAttributes));
  }

  @Override
  public Mono<Void> handle(@NotNull ServerWebExchange exchange, @NotNull Throwable throwable) {
    Span span = tracer.buildSpan("process exception").start();
    span.log("Exception message: " + throwable.getMessage());

    UUID errorId = UUID.randomUUID();
    exchange.getAttributes().put(ERROR_ID, errorId);
    span.setTag(ERROR_ID, errorId.toString());

    return super.handle(exchange, throwable)
      .doFinally(signalType -> span.finish())
      .then();
  }

  /**
   * Logs the {@code throwable} error for the given {@code request} and {@code response}
   * exchange. The default implementation logs all errors at debug level. Additionally,
   * any internal server error (500) is logged at error level.
   *
   * @param request   the request that was being handled
   * @param response  the response that was being sent
   * @param throwable the error to be logged
   */
  @Override
  protected void logError(ServerRequest request, ServerResponse response, Throwable throwable) {
    //NOTE depending on the application it makes sense to only log errors in debug mode
    //        if (log.isDebugEnabled()) {
    //            log.debug(request.exchange().getLogPrefix() + formatError(throwable, request));
    //        }

    log.info(request.exchange().getLogPrefix() + formatError(throwable, request));

    if (HttpStatus.resolve(response.rawStatusCode()) != null && response.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
      // adds request infos to the error log message
      log.error("{}, 500 Server Error with errorId {} for {} with error '{}'", request.exchange().getLogPrefix(), request.exchange().getAttribute(ERROR_ID), formatRequest(request), formatError(throwable), throwable);
      // adds request as JSON object to the log entry with a separate log message, does not work with non-structured logging output
      log.error("error thrown", kv("request", request), kv("stacktrace", getStackTrace(throwable)));
    }
  }

  /**
   * Get the HTTP error status information from the error map.
   *
   * @param errorAttributes the current error information
   * @return the error HTTP status
   */
  private int getHttpStatus(Map<String, Object> errorAttributes) {
    return (int) errorAttributes.getOrDefault("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  private StringBuilder formatError(@NotNull Throwable throwable) {
    StringBuilder response = new StringBuilder();
    response.append(throwable.getMessage());
    return response;
  }

  private String formatRequest(ServerRequest request) {
    String rawQuery = request.uri().getRawQuery();
    String query = StringUtils.hasText(rawQuery) ? "?" + rawQuery : "";
    return "HTTP " + request.methodName() + " \"" + request.path() + query + "\"";
  }

  private StringBuilder formatError(@NotNull Throwable ex, @NotNull ServerRequest request) {
    String reason = ex.getClass().getSimpleName() + ": " + ex.getMessage();
    StringBuilder formattedError = new StringBuilder();
    formattedError.append("Resolved Exception [");
    formattedError.append(reason);
    formattedError.append("] for HTTP ");
    formattedError.append(request.method());
    formattedError.append(" ");
    formattedError.append(request.path());
    formattedError.append(" ");
    formattedError.append("with errorId");
    formattedError.append(request.exchange().getAttribute(ERROR_ID).toString());
    return formattedError;
  }
}
