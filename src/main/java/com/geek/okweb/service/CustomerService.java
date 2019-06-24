package com.geek.okweb.service;

import com.geek.okweb.dao.CustomerDao;
import com.geek.okweb.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class CustomerService {

    @Autowired
    private CustomerDao customerDao;

    public void save(Customer customer){
        customerDao.save(customer);
    }

    public Customer findById(String id){
        return customerDao.findById(id);
    }

    public void update(Customer customer){
        customerDao.update(customer);
    }

    public void delete(String id){
        Customer customer = findById(id);
        customerDao.delete(customer);
    }

    public List<Customer> findAll(){
        return customerDao.findAll();
    }

}
