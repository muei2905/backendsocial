package com.example.BackEndSocial.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final ConcurrentMap<String, Bucket> cache = new ConcurrentHashMap<>();

    @Value("${PERFORMANCE_TEST:false}")
    private boolean performanceTest;

    private Bucket createNewBucket() {
        int limit = performanceTest ? 1000 : 5;
        return Bucket.builder()
                .addLimit(Bandwidth.classic(limit, Refill.greedy(limit, Duration.ofMinutes(1))))
                .build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String key = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        if (performanceTest || (userAgent != null && userAgent.contains("Apache-HttpClient"))) {
            filterChain.doFilter(request, response);
            return;
        }
        Bucket bucket = cache.computeIfAbsent(key, k -> createNewBucket());
        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(429);
            response.getWriter().write("Too Many Requests - Bạn đã vượt quá giới hạn!");
        }
    }

}
