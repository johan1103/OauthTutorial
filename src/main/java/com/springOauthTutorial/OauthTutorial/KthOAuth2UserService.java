package com.springOauthTutorial.OauthTutorial;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class KthOAuth2UserService extends DefaultOAuth2UserService {
    private final HttpSession httpSession;
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2UserService<OAuth2UserRequest,OAuth2User> originOauth = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = originOauth.loadUser(userRequest);
        Map<String, Object> userAttributes = oAuth2User.getAttributes();

        String provider = userRequest.getClientRegistration().getRegistrationId();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        String name=null;
        String email=null;
        Integer attributeId=null;
        if(provider.equals("github")) {
            name = (String) userAttributes.get("login");
            email = (String) userAttributes.get("html_url");
            attributeId = (Integer) userAttributes.get("id");
        }else if(provider.equals("kakao")){
            Map<String,Object> kakaoAttributes=(Map<String,Object>)userAttributes.get("kakao_account");
            name = (String) kakaoAttributes.get("email");
            email = (String) kakaoAttributes.get("email");
            attributeId = Long.valueOf((Long) userAttributes.get("id")).intValue();
        }
        User user=userRepository.saveUser(name,email,attributeId);
        return KthUserDetails.create(user, oAuth2User.getAttributes());
    }
}
