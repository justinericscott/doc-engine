package com.github.justinericscott.docengine.config;

//import java.io.IOException;

//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.web.csrf.CsrfFilter;
//import org.springframework.security.web.csrf.CsrfToken;
//import org.springframework.security.web.csrf.CsrfTokenRepository;
//import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.util.WebUtils;

/**
 * @author Justin Scott TODO: Description
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//	private static final Logger LOG = LogManager.getLogger(SecurityConfig.class);
//	private static final String COOKIE = "XSRF-TOKEN";
//	private static final String CSRF_HEADER = "X-XSRF-TOKEN";
//	private static final String[] PUBLIC_CONTENT = new String[] { "/", "/angular/**", "/html/**", "/html/**", "/css/**",
//			"/js/**" };

	public SecurityConfig() {
	}

//	@Autowired
//	public void registerGlobalAuthentication(AuthenticationManagerBuilder auth) throws Exception {
//		// TODO: Trace log entry
//		auth.inMemoryAuthentication().withUser("user").password("pass").roles("user");
//	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO: Trace log entry
		http.formLogin().disable().logout().disable();
	}

//	private Filter csrfHeaderFilter() {
//		// TODO: Trace log entry
//		return new OncePerRequestFilter() {
//			@Override
//			protected void doFilterInternal(HttpServletRequest request,
//					HttpServletResponse response, FilterChain filterChain)
//					throws ServletException, IOException {
//				CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
//				if (csrf != null) {
//					Cookie cookie = WebUtils.getCookie(request, COOKIE);
//					String token = csrf.getToken();
//					if (cookie == null || token != null && !token.equals(cookie.getValue())) {
//						cookie = new Cookie(COOKIE, token);
//						cookie.setPath("/");
//						response.addCookie(cookie);
//					}
//				}
//				filterChain.doFilter(request, response);
//			}
//		};
//	}

//	private CsrfTokenRepository csrfTokenRepository() {
//		// TODO: Trace log entry
//		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
//		repository.setHeaderName(CSRF_HEADER);
//		return repository;
//	}
}