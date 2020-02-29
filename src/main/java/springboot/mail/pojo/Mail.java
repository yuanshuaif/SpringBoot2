package springboot.mail.pojo;

import java.io.File;

/**
 * Created by Administrator on 2019/3/10.
 */
public class Mail {
    // 发送者
    private String sender;
    // 接收者
    private String receiver;
    // 标题
    private String subject;
    // 内容
    private String content;
    // 附件名称
    private String fileName;
    // 文件
    private File file;
    // 图片引用ID
    private String contentId;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    @Override
    public String toString() {
        return "mail{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", fileName='" + fileName + '\'' +
                ", file=" + file +
                ", contentId='" + contentId + '\'' +
                '}';
    }
}
