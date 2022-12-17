package com.springOauthTutorial.OauthTutorial;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class KthLoginAuthHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        //DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        //String token = jwtTokenProvider.createToken((String) oAuth2User.getAttributes().get("login"));
        String token = jwtTokenProvider.createAccessToken(authentication);
        //response.addHeader("kth-token", "Bearer " + token);

        System.out.println("token value "+token);
        Cookie myCookie = new Cookie("kth-token", token);
        myCookie.setMaxAge(300);
        myCookie.setPath("/"); // 모든 경로에서 접근 가능 하도록 설정
        response.addCookie(myCookie);
        System.out.println("login success, token value : "+token);
        /*
        String targetUrl = UriComponentsBuilder.fromUriString("/")
                .queryParam("token", token)
                .build().toUriString();

         */
        String targetUrl = UriComponentsBuilder.fromUriString("/")
                .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
