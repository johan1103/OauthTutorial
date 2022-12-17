package com.springOauthTutorial.OauthTutorial;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService{

    private final UserRepository userRepository;
    public User loadUserByUsername(String username)throws UsernameNotFoundException{
        User user = userRepository.findByName(username);
        if(user==null){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        return user;
    }
}
