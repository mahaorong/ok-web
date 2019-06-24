package com.geek.okweb.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发送简单信息邮件
     * @param toMail 接收人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    @Async
    public String sendSimpleMail(String toMail,String subject,String content){

        try {
            log.info("============================开始邮件发送============================");
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            Thread.sleep(1000);
            helper.setFrom(toMail); //设置发送人邮箱
            helper.setTo(toMail);  //设置接收人邮箱
            helper.setSubject(subject); //邮箱主题
            helper.setText(content,true); //邮箱内容
            mailSender.send(message);
            log.info("【简单邮件】成功发送给{}邮件。subject={},",toMail,subject);
            log.info("============================邮件发送成功============================");
            return "success";
        }catch (Exception e){
            log.error("【简单邮件】发送失败。subject={},",subject);
            log.info("【邮件发送异常】=={}",e);
            log.info("============================邮件发送失败============================");
            e.printStackTrace();
            return "error";
        }
    }
}
