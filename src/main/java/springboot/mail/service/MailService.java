package springboot.mail.service;

import springboot.mail.pojo.Mail;

/**
 * Created by Administrator on 2019/3/9.
 */
public interface MailService {

    public void sendSimpleMail(Mail mail);

    public void sendHtmlMail(Mail mail);

    public void sendAttachmentsMail(Mail mail);

    public void sendInlineMail(Mail mail);
}
