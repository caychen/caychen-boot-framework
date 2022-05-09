package com.caychen.boot.email.component;

import com.caychen.boot.email.config.MailProperties;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @Author: Caychen
 * @Date: 2022/5/9 18:17
 * @Description:
 */
@Component
@EnableConfigurationProperties(MailProperties.class)
public class MailComponent {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailProperties mailProperties;

    /**
     * 发送简单文本邮件
     *
     * @param subject： 主题
     * @param text： 文本信息
     */
    public void sendSimpleMail(String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailProperties.getFrom());
        mailMessage.setTo(mailProperties.getTo());

        mailMessage.setSubject(subject);
        mailMessage.setText(text);

        javaMailSender.send(mailMessage);
    }

    /**
     * 发送带有链接和附件的复杂邮件(如果需要测试附件，需要在附件参数中指定附件的文件路径)
     *
     * @param subject： 主题
     * @param text： 文本信息
     * @param attachmentMap： 附件
     * @throws MessagingException
     */
    public void sendHtmlMail(String subject, String text, Map<String, String> attachmentMap) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        //是否发送的邮件是富文本（附件，图片，html等）
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

        messageHelper.setFrom(mailProperties.getFrom());
        messageHelper.setTo(mailProperties.getTo());

        messageHelper.setSubject(subject);
        //重点，默认为false，显示原始html代码，无效果
        messageHelper.setText(text, true);

        if (attachmentMap != null) {
            attachmentMap.entrySet().stream().forEach(entrySet -> {
                try {
                    File file = new File(entrySet.getValue());
                    if (file.exists()) {
                        messageHelper.addAttachment(entrySet.getKey(), new FileSystemResource(file));
                    }
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            });
        }

        javaMailSender.send(mimeMessage);
    }

    /**
     * 按照模版发送邮件(需要在resources\templates目录下准备一个模版文件,例如mail.ftl)
     *
     * @param subject: 主题
     * @param templateName: 模板文件名
     * @param params： 模板对应的参数值
     * @throws MessagingException
     * @throws IOException
     * @throws TemplateException
     */
    public void sendTemplateMail(String subject, String templateName, Map<String, Object> params) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(mailProperties.getFrom());
        helper.setTo(mailProperties.getTo());

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
        configuration.setClassForTemplateLoading(this.getClass(), "/templates");

        String html = FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate(templateName), params);

        helper.setSubject(subject);
        //重点，默认为false，显示原始html代码，无效果
        helper.setText(html, true);

        javaMailSender.send(mimeMessage);
    }
}
