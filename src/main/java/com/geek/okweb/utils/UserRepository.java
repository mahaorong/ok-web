package com.geek.okweb.utils;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.geek.okweb.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User,String>{
}
