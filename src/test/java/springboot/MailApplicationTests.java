package springboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import springboot.mail.pojo.Mail;
import springboot.mail.service.MailService;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailApplicationTests {
	/*@Autowired
	private MailService MailService;
    @Autowired
    private TemplateEngine templateEngine;

    @Value("${mail.attachment.filePath}")
    private String filePath;
    @Value("${mail.fromMail.addr}")
    private String sender;
    @Value("${mail.picture.contentId}")
    private String contentId;
    @Value("${mail.picture.path}")
    private String picturePath;

	@Test
	public void testSimpleMail() {
        Mail mail = new Mail();
        mail.setSender(sender);
        mail.setReceiver("m18364632015@163.com");
        mail.setSubject("simple mail");
        mail.setContent("我是老婆");
		MailService.sendSimpleMail(mail);
	}

    @Test
    public void testHtmlMail() {
        String content="<html>\n" +
                            "<body>\n" +
                            "    <h3>hello 老公 ! 这是一封Html邮件!</h3>\n" +
                            "</body>\n" +
                        "</html>";
        Mail mail = new Mail();
        mail.setSender(sender);
        mail.setReceiver("m18364632015@163.com");
        mail.setSubject("html mail");
        mail.setContent(content);
        MailService.sendHtmlMail(mail);
    }

    @Test
    public void sendAttachmentsMail() {
        String content="<html>\n" +
                "<body>\n" +
                "    <h3>hello 老公 ! 这是一封attachment邮件!</h3>\n" +
                "</body>\n" +
                "</html>";
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
        Mail mail = new Mail();
        mail.setSender(sender);
        mail.setReceiver("m18364632015@163.com");
        mail.setSubject("attachment mail");
        mail.setContent(content);
        mail.setFile(new File(filePath));
        mail.setFileName(fileName);
        MailService.sendAttachmentsMail(mail);
    }

    @Test
    public void sendInlineMail() {
        String content="<html><body>这是有图片的邮件：<img src=\'cid:" + contentId + "\' ></body></html>";
        Mail mail = new Mail();
        mail.setSender(sender);
        mail.setReceiver("m18364632015@163.com");
        mail.setSubject("picture mail");
        mail.setContent(content);
        mail.setFile(new File(picturePath));
        mail.setContentId(contentId);
        MailService.sendInlineMail(mail);
    }

    *//**
     * 使用trymeleaf模板邮件
     *//*
    @Test
    public void sendTemplateMail() {
        //创建邮件正文
        Context context = new Context();
        context.setVariable("id", "006");
        String emailContent = templateEngine.process("emailTemplate", context);
        Mail mail = new Mail();
        mail.setSender(sender);
        mail.setReceiver("m18364632015@163.com");
        mail.setSubject("template mail");
        mail.setContent(emailContent);
        MailService.sendHtmlMail(mail);
    }*/
}
