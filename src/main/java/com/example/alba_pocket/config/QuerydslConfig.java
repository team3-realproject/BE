package com.example.alba_pocket.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//config 설정으로 이 프로젝트에서는 어느 곳에서나 JPAQueryFactory를 주입 받아 Querydsl을 사용할 수 있게 됩니다.
@Configuration
public class QuerydslConfig {


    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
