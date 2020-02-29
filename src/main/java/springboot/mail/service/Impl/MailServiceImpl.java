package springboot.mail.service.Impl;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import springboot.mail.pojo.Mail;
import springboot.mail.service.MailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by Administrator on 2019/3/9.
 */
@Service
public class MailServiceImpl implements MailService {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发送简单邮件
     * @param mail
     */
    @Override
    public void sendSimpleMail(Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mail.getSender());
        message.setTo(mail.getReceiver());
        message.setSubject(mail.getSubject());
        message.setText(mail.getContent());
        try {
            mailSender.send(message);
            logger.info("simple邮件已经发送。");
        } catch (Exception e) {
            logger.error("发送simple邮件时发生异常！", e);
        }
    }

    /**
     * 发送html邮件
     * @param mail
     */
    public void sendHtmlMail(Mail mail){
        MimeMessage message = mailSender.createMimeMessage();
        try {
            // true 表示可以发送多个
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mail.getSender());
            helper.setTo(mail.getReceiver());
            helper.setSubject(mail.getSubject());
            // true 表示是html邮件
            helper.setText(mail.getContent(), true);
            mailSender.send(message);
            logger.info("html邮件发送成功。");
        } catch (MessagingException e) {
            e.printStackTrace();
            logger.error("发送html邮件时发生异常！", e);
        }
    }

    /**
     * 发送附件邮件
     * @param mail
     */
    public void sendAttachmentsMail(Mail mail){
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mail.getSender());
            helper.setTo(mail.getReceiver());
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getContent(), true);
            // 添加多个附件可以使用多条 helper.addAttachment(fileName, file)
            helper.addAttachment(mail.getFileName(), mail.getFile());
            mailSender.send(message);
            logger.info("attachment邮件发送成功。");
        } catch (MessagingException e) {
            e.printStackTrace();
            logger.error("发送attachment邮件时发生异常！", e);
        }
    }

    /**
     * 发送图片邮件
     * @param mail
     */
    public void sendInlineMail(Mail mail){
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mail.getSender());
            helper.setTo(mail.getReceiver());
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getContent(), true);
            // 添加多个图片可以使用多条 <img src='cid:" + contentId + "' > 和 helper.addInline(contentId, res) 来实现
            helper.addInline(mail.getContentId(), mail.getFile());
            mailSender.send(message);
            logger.info("pictrue邮件发送成功。");
        } catch (MessagingException e) {
            e.printStackTrace();
            logger.error("发送pictrue邮件时发生异常！", e);
        }
    }
}
