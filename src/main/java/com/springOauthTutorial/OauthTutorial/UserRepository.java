package com.springOauthTutorial.OauthTutorial;

import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class UserRepository {
    @PersistenceContext
    private EntityManager em;

    public User saveUser(String name, String email,Integer attributesId){
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setAttributeId(attributesId);
        em.persist(user);
        return user;
    }
    public User findByEmail(String email){
        return em.createQuery("select u from User u where " +
                "u.email=:email",User.class).setParameter("email",email)
                .getSingleResult();
    }
    public User findByName(String name){
        return em.createQuery("select u from User u where " +
                "u.name=:name",User.class).setParameter("name",name)
                .getSingleResult();
    }
    public User findById(Long id){
        return em.find(User.class,id);
    }
}
