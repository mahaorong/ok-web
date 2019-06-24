package com.geek.okweb.service;

import com.geek.okweb.dao.UserDao;
import com.geek.okweb.domain.User;
import com.geek.okweb.enums.BlogEnum;
import com.geek.okweb.exception.BlogException;
import com.geek.okweb.utils.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
public class UserService{

	@Autowired
	private UserDao userDao;


	//保存用户
	 public void save(User user){
		userDao.save(user);
     }

     //根据id查询用户
     public User findByUserId(String userId){
		 User user = userDao.findByUser(userId);
		 if (user == null){
		 	throw new BlogException(BlogEnum.USER_NOT_FOUND);
		 }
		 return user;
	 }

	 public User findByUserName(String username) {
		return userDao.findByUserName(username);
	 }

	 public MyPage<User> findAll(Integer page, Integer pageSize){
	   return userDao.findAll(page,pageSize);
	 }

	 //更新用户
	public void update(User user){
	 	userDao.update(user);
	}


	public void delete(String id) {
		userDao.delete(id);
	}
}
