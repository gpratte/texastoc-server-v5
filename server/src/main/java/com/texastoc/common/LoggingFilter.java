package com.texastoc.common;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;

/**
 * May be better suited to an API gateway than a servlet filter.
 * <p>Logs http requests/responses</p>
 */
@Order(2)
@Slf4j
public class LoggingFilter implements Filter {

  public static final String CORRELATION_ID = "Correlation-Id";

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;

    final String correlationId =
        req.getHeader(CORRELATION_ID) != null ? req.getHeader(CORRELATION_ID)
            : UUID.randomUUID().toString();

    try {
      MDC.put("correlationId", correlationId);
      res.setHeader(CORRELATION_ID, correlationId);

      log.info(
          "request={action={} uri={} contentType={}}",
          req.getMethod(),
          req.getRequestURI(),
          req.getContentType());

      chain.doFilter(req, res);

      log.info("response={action={} uri={} status={}}",
          req.getMethod(),
          req.getRequestURI(),
          res.getStatus());
    } finally {
      MDC.clear();
    }
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
  }

  @Override
  public void destroy() {
    Filter.super.destroy();
  }
}
