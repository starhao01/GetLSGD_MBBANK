//package com.viettelpost.core.cfgs;
//
//import com.viettelpost.core.services.domains.UserInfo;
//import com.viettelpost.core.utils.BaseSecurity;
//import com.viettelpost.core.utils.Utils;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.MalformedJwtException;
//import io.jsonwebtoken.UnsupportedJwtException;
//import org.springframework.core.env.Environment;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.UUID;
//
////@Component
//public class AuthorizationFilter extends OncePerRequestFilter {
//
//    private final String HEADER = "Authorization";
//    private final String HEADER2 = "x-access-token";
//
//    Environment env;
//
//
//    public AuthorizationFilter() {
//    }
//
//    public AuthorizationFilter(Environment env) {
//        this.env = env;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
//        try {
//            if (checkJWTToken(request, response)) {
//                UserInfo user = validateToken(request);
//                setUpSpringAuthentication(user);
//            } else {
//                SecurityContextHolder.clearContext();
//            }
//            chain.doFilter(request, response);
//        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
//        }
//    }
//
//    private UserInfo validateToken(HttpServletRequest request) {
//        try {
//            String jwtToken = getHeader(request);
//            if (jwtToken != null) {
//                jwtToken = jwtToken.replace("Bearer ", "").replace("bearer ", "");
//                UserInfo info = BaseSecurity.evtpToken(jwtToken);
//                RequestInfo requestInfo = new RequestInfo();
//                requestInfo.setReqID(UUID.randomUUID().toString());
//                requestInfo.setReqURI(request.getRequestURI());
//                info.setRequestInfo(requestInfo);
//                return info;
//            }
//        } catch (Exception e) {
//
//        }
//        return null;
//    }
//
//    /**
//     * Authentication method in Spring flow
//     *
//     * @param user
//     */
//    private void setUpSpringAuthentication(UserInfo user) {
//        if (user != null) {
//            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null,
//                    Arrays.asList());
//            SecurityContextHolder.getContext().setAuthentication(auth);
//        } else {
//            SecurityContextHolder.clearContext();
//        }
//    }
//
//    String getHeader(HttpServletRequest request) {
//        String authenticationHeader = request.getHeader(HEADER);
//        if (Utils.isNullOrEmpty(authenticationHeader)) {
//            authenticationHeader = request.getHeader(HEADER2);
//        }
//        return authenticationHeader;
//    }
//
//
//    private boolean checkJWTToken(HttpServletRequest request, HttpServletResponse res) {
//        String authenticationHeader = getHeader(request);
//        if (authenticationHeader == null)
//            return false;
//        return true;
//    }
//
//}
