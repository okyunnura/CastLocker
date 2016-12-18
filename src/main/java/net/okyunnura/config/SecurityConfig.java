package net.okyunnura.config;

import net.okyunnura.service.AuthorizedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
				.antMatchers("/css/**", "/js/**", "/img/**");
	}

//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication()
//				.withUser("user").password("password").roles("USER");
//	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/startup").permitAll()
				.anyRequest()
				.authenticated();
		http.formLogin()
				.loginPage("/login")
				.usernameParameter("username").passwordParameter("password")
				.loginProcessingUrl("/login/auth").failureUrl("/login?error")
				.permitAll();
		http.logout()
				.logoutUrl("/logout").permitAll()
				.logoutSuccessUrl("/login")
				.deleteCookies("JSESSIONID").invalidateHttpSession(true);
	}

	@Configuration
	public class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {

		private final AuthorizedService authorizedService;

		@Autowired
		public AuthenticationConfiguration(AuthorizedService authorizedService) {
			this.authorizedService = authorizedService;
		}

		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(authorizedService)
					.passwordEncoder(new StandardPasswordEncoder());
		}
	}
}
