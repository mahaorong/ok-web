package com.geek.okweb.dao;

import com.geek.okweb.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
@Slf4j
public class CustomerDao extends BaseDao<Customer>{

    public void save(Customer customer){
        getSession().save(customer);
    }

    public Customer findById(String id){
        return getSession().find(Customer.class, id);
    }

    public void update(Customer customer){
        getSession().update(customer);
    }

    public void delete(Customer customer){
        getSession().delete(customer);
    }

    public List<Customer> findAll(){
        DetachedCriteria dc = DetachedCriteria.forClass(Customer.class);
        return dc.getExecutableCriteria(getSession()).list();
    }

}
