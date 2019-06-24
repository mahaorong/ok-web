package com.geek.okweb.dao;

import com.geek.okweb.domain.User;
import com.geek.okweb.utils.MyPage;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class UserDao extends BaseDao<User> {


	//根据用户id查找该用户
	public User findByUser(String userId){
		return getSession().find(User.class,userId);
	}

	public void save(User user){
		getSession().save(user);
	}

	public MyPage<User> findAll(Integer page, Integer pageSize){
		DetachedCriteria dc = DetachedCriteria.forClass(User.class);
		return findPageByCriteria(dc, pageSize, page);
	}
    
    public User findByUserName(String username) {
    	DetachedCriteria detachedCriteria =DetachedCriteria.forClass(User.class);
    	Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
    	criteria.add(Restrictions.eq("username", username));
    	return (User) criteria.uniqueResult();
    }

	/**
	 * 更新用户
	 */
	public void  update(User user){
		getSession().update(user);
	}

	/**
	 * 删除用户
	 */
	public void delete(String id){
		getSession().delete(findByUser(id));
	}

}
