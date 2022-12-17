package com.springOauthTutorial.OauthTutorial;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationProvider;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

@SpringBootApplication
@Controller
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class OauthTutorialApplication extends WebSecurityConfigurerAdapter {
	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;

	@ResponseBody
	@GetMapping("/user")
	public Map<String, Object> user(@RequestParam(name = "token")@Nullable String token
			,@CookieValue(value = "kth-token", required = false) Cookie coookie) {
		//이 컨트롤러는 Authorization 코드를 받아서 스프링 시큐리티가 전처리로 클라이언트 아이디 비밀번호 인증까지 해서
		// 엑세스 토큰까지 받아온 상황이다. 그런데 어째서 Collection만 리턴하는가. Third파티가 제공한 로그인 페이지에서
		// 로그인 수락후 리다이렉션 URL인데, Html 문서를 리턴해주어야 하는것 아닌가?
		// 현재 개인적인 의견으로는 이것도 스프링이 추상화해서 적용시키는 것 같다.
		System.out.println("function called");
		System.out.println(coookie.getValue());
		return Collections.singletonMap("name", coookie.getValue());
	}
	@GetMapping("session-test")
	public String sessionUser(@CookieValue(value = "kth-token", required = false) Cookie coookie,
							  Model model){
		System.out.println("cookie value "+coookie.getValue());
		Long userId = Long.valueOf(jwtTokenProvider.getUserPk(coookie.getValue()));
		User user = userRepository.findById(userId);
		System.out.println(userId);
		model.addAttribute("userName",
				user.getName());
		return "session";
	}

	@GetMapping("")
	public String mainPage(@RequestParam(name = "token")@Nullable String token, Model model
	,HttpServletResponse response){
		System.out.println(token);
		TokenDto tokenDto=new TokenDto();
		tokenDto.setValue(token);
		if(token!=null)
			tokenDto.setValue(token);
		else
			tokenDto.setValue("null");
		model.addAttribute("token",tokenDto);
		if(token!=null) {
			System.out.println("token value "+token);
			Cookie myCookie = new Cookie("kth-token", token);
			myCookie.setMaxAge(300);
			myCookie.setPath("/"); // 모든 경로에서 접근 가능 하도록 설정
			response.addCookie(myCookie);
		}
		return "index";
	}

	public static void main(String[] args) {
		SpringApplication.run(OauthTutorialApplication.class, args);
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
				.authorizeRequests(a -> a
						.antMatchers("/", "/error", "/webjars/**","/submit","/basic/**","/create",
								"/templates/**").permitAll()
						.anyRequest().authenticated()
				)
				.exceptionHandling(e -> e
						.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
				)
				.logout(l -> l
						.logoutSuccessUrl("/basic/login").permitAll()
				)
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.oauth2Login()
				.successHandler(new KthLoginAuthHandler(jwtTokenProvider))
				.and()
				.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
						OAuth2LoginAuthenticationFilter.class);
/*
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
 */
		// @formatter:on
	}
}
