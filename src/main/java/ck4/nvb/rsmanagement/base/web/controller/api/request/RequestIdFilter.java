package ck4.nvb.rsmanagement.base.web.controller.api.request;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestIdFilter implements Filter {
  public static final String HEADER = "X-Request-Id";
  public static final String MDC_KEY = "traceId";

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    String id = request.getHeader(HEADER);
    if (id == null || id.isBlank()) {
      id = UUID.randomUUID().toString();
    }

    MDC.put(MDC_KEY, id);
    response.setHeader(HEADER, id);

    try {
      chain.doFilter(req, res);
    } finally {
      MDC.remove(MDC_KEY);
    }
  }
}
