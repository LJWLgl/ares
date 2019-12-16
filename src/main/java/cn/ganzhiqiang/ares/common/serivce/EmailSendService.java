package cn.ganzhiqiang.ares.common.serivce;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author nanxuan
 * @since 2018/05/14
 **/

@Service
public class EmailSendService {

    @Value("${email.username}")
    private String user;

    @Resource
    private Session emailSession;

    private MimeMessage msg;
    private String text;
    private String html;
    private List<MimeBodyPart> attachments = new ArrayList<MimeBodyPart>();

    public void subject(String subject) throws MessagingException {
        msg = new MimeMessage(emailSession);
        msg.setSubject(subject, "UTF-8");
    }

    public void from(String nickName) throws MessagingException {
        from(nickName, user);
    }

    private void from(String nickName, String from) throws MessagingException {
        try {
            nickName = MimeUtility.encodeText(nickName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        msg.setFrom(new InternetAddress(nickName + " <" + from + ">"));
    }

    public EmailSendService replyTo(String... replyTo) throws MessagingException {
        String result = Arrays.asList(replyTo).toString().replaceAll("(^\\[|\\]$)", "").replace(", ", ",");
        msg.setReplyTo(InternetAddress.parse(result));
        return this;
    }

    public EmailSendService replyTo(String replyTo) throws MessagingException {
        msg.setReplyTo(InternetAddress.parse(replyTo.replace(";", ",")));
        return this;
    }

    public void to(String... to) throws Exception {
        addRecipients(to, Message.RecipientType.TO);
    }

    public void to(String to) throws MessagingException {
        addRecipient(to, Message.RecipientType.TO);
    }

    public void cc(String... cc) throws MessagingException {
        addRecipients(cc, Message.RecipientType.CC);
    }

    public void cc(String cc) throws MessagingException {
        addRecipient(cc, Message.RecipientType.CC);
    }

    public void bcc(String... bcc) throws MessagingException {
        addRecipients(bcc, Message.RecipientType.BCC);
    }

    public void bcc(String bcc) throws MessagingException {
        addRecipient(bcc, Message.RecipientType.BCC);
    }

    private void addRecipients(String[] recipients, Message.RecipientType type) throws MessagingException {
        String result = Arrays.asList(recipients).toString().replace("(^\\[|\\]$)", "").replace(", ", ",");
        msg.setRecipients(type, InternetAddress.parse(result));
    }

    private void addRecipient(String recipient, Message.RecipientType type) throws MessagingException {
        msg.setRecipients(type, InternetAddress.parse(recipient.replace(";", ",")));
    }

    public void text(String text) {
        this.text = text;
    }

    public void html(String html) {
        this.html = html;
    }

    public void attach(File file) throws MessagingException {
        attachments.add(createAttachment(file, null));
    }

    public void attach(File file, String fileName) throws MessagingException {
        attachments.add(createAttachment(file, fileName));
    }

    private MimeBodyPart createAttachment(File file, String fileName) throws MessagingException {
        MimeBodyPart attachmentPart = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(file);
        attachmentPart.setDataHandler(new DataHandler(fds));
        try {
            attachmentPart.setFileName(null == fileName ? MimeUtility.encodeText(fds.getName()) : MimeUtility.encodeText(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attachmentPart;
    }

    public void send() throws MessagingException {
        if (text == null && html == null)
            throw new NullPointerException("At least one context has to be provided: Text or Html");

        MimeMultipart cover;
        boolean usingAlternative = false;
        boolean hasAttachments = attachments.size() > 0;

        if (text != null && html == null) {
            // TEXT ONLY
            cover = new MimeMultipart("mixed");
            cover.addBodyPart(textPart());
        } else if (text == null && html != null) {
            // HTML ONLY
            cover = new MimeMultipart("mixed");
            cover.addBodyPart(htmlPart());
        } else {
            // HTML + TEXT
            cover = new MimeMultipart("alternative");
            cover.addBodyPart(textPart());
            cover.addBodyPart(htmlPart());
            usingAlternative = true;
        }

        MimeMultipart content = cover;
        if (usingAlternative && hasAttachments) {
            content = new MimeMultipart("mixed");
            content.addBodyPart(toBodyPart(cover));
        }

        for (MimeBodyPart attachment : attachments) {
            content.addBodyPart(attachment);
        }

        msg.setContent(content);
        msg.setSentDate(new Date());
        Transport.send(msg);
    }

    private MimeBodyPart toBodyPart(MimeMultipart cover) throws MessagingException {
        MimeBodyPart wrap = new MimeBodyPart();
        wrap.setContent(cover);
        return wrap;
    }

    private MimeBodyPart textPart() throws MessagingException {
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setText(text);
        return bodyPart;
    }

    private MimeBodyPart htmlPart() throws MessagingException {
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(html, "text/html; charset=utf-8");
        return bodyPart;
    }

}