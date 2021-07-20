package cn.huimin100.erp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MailUtil {

    @Value("${spring.mail.personal}")
    private String personal;
    @Value("${spring.mail.username}")
    private String userName;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发送普通文本
     * @param email 对方邮箱地址
     * @param subject 主题
     * @param text 邮件内容
     */
    public void simpleMailSend(String email,String subject,String text) {
        //创建邮件内容
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(userName);
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);

//      抄送
//      message.setCc("gengxiang@huimin100.cn,915331408@qq.com");
//      message.setCc("gengxiang@huimin100.cn","915331408@qq.com");
//      密送
//      message.setBcc("915331408@qq.com");
//      message.setBcc("gengxiang@huimin100.cn","915331408@qq.com");

        //发送邮件
        mailSender.send(message);
        System.out.println("发送成功");
    }

    /**
     * 发送附件,支持多附件
     * //使用JavaMail的MimeMessage，支付更加复杂的邮件格式和内容
     //MimeMessages为复杂邮件模板，支持文本、附件、html、图片等。
     * @param email 对方邮箱
     * @param subject 主题
     * @param text 内容
     * @param paths 附件路径，和文件名
     * @throws MessagingException
     */
    public void attachedSend(String email, String subject, String text, Map<String,String> paths) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        //创建MimeMessageHelper对象，处理MimeMessage的辅助类
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //使用辅助类MimeMessage设定参数
        helper.setFrom(userName, personal);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(text);

//      抄送
//      helper.setCc("915331408@qq.com");
//      helper.setCc(new String [] {"gengxiang@huimin100.cn","915331408@qq.com"});
//      密送
//      helper.setBcc("915331408@qq.com");
//      helper.setBcc(new String [] {"gengxiang@huimin100.cn","915331408@qq.com"});

        if (paths!=null){
            paths.forEach((k,v)->{
                //加载文件资源，作为附件
                FileSystemResource file = new FileSystemResource(v);
                try {
                    //添加附件
                    helper.addAttachment(k, file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        //发送邮件
        mailSender.send(message);
        System.out.println("发送成功");
    }

    /**
     * 发送html文件，支持多图片
     * @param email 对方邮箱
     * @param subject 主题
     * @param text 内容
     * @param paths 富文本中添加用到的路径，一般是图片，或者css,js文件
     * @throws MessagingException
     */
    public void richContentSend(String email,String subject,String text,Map<String,String> paths) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(userName, personal);
        helper.setTo(email);
        helper.setSubject(subject);
        //第二个参数true，表示text的内容为html，然后注意<img/>标签，src='cid:file'，'cid'是contentId的缩写，'file'是一个标记，
        //需要在后面的代码中调用MimeMessageHelper的addInline方法替代成文件
        helper.setText(text,true);
        //文件地址相对应src目录
        // ClassPathResource file = new ClassPathResource("logo.png");

        if (paths!=null){
            paths.forEach((k,v)->{
                //文件地址对应系统目录
                FileSystemResource file = new FileSystemResource(v);
                try {
                    helper.addInline(k, file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        mailSender.send(message);
        System.out.println("发送成功");
    }

    /**
     * 群发多人，且多附件
     * @param emails 多人邮件地址
     * @param subject 主题
     * @param text 内容
     * @param filePath 文件路径
     * @throws Exception
     */
    public void sendBatchMailWithFile(String[] emails, String subject, String text,  String[] filePath) throws Exception {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(new InternetAddress(MimeUtility.encodeText(userName)));
        messageHelper.setSubject(subject);
        if (filePath != null) {
            BodyPart mdp = new MimeBodyPart();// 新建一个存放信件内容的BodyPart对象
            mdp.setContent(text, "text/html;charset=UTF-8");// 给BodyPart对象设置内容和格式/编码方式
            Multipart mm = new MimeMultipart();// 新建一个MimeMultipart对象用来存放BodyPart对象
            mm.addBodyPart(mdp);// 将BodyPart加入到MimeMultipart对象中(可以加入多个BodyPart)
            // 把mm作为消息对象的内容
            MimeBodyPart filePart;
            FileDataSource filedatasource;
            // 逐个加入附件
            for (int j = 0; j < filePath.length; j++) {
                filePart = new MimeBodyPart();
                filedatasource = new FileDataSource(filePath[j]);
                filePart.setDataHandler(new DataHandler(filedatasource));
                try {
                    filePart.setFileName(MimeUtility.encodeText(filedatasource.getName()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mm.addBodyPart(filePart);
            }
            mimeMessage.setContent(mm);
        } else {
            messageHelper.setText(text, true);
        }

        List<InternetAddress> list = new ArrayList<InternetAddress>();// 不能使用string类型的类型，这样只能发送一个收件人
        for (int i = 0; i < emails.length; i++) {
            list.add(new InternetAddress(emails[i]));
        }
        InternetAddress[] address = list.toArray(new InternetAddress[list.size()]);

        mimeMessage.setRecipients(Message.RecipientType.TO, address);
        mimeMessage = messageHelper.getMimeMessage();

        mailSender.send(mimeMessage);
        System.out.println("发送成功");
    }


    public static void main(String[] args) throws Exception
    {
        MailUtil emailUtil = new MailUtil();
        //测试发送普通文本
//        emailUtil.setInitData("smtp.qq.com","706548532@qq.com","123456");
//        emailUtil.setInitData("smtp.163.com","1234@163.com","1234");
//        emailUtil.setInitData("smtp.exmail.qq.com","oc-service@huimin100.cn","xxxxxx");
        emailUtil.simpleMailSend("limengjun1@huimin100.cn","测试","测试能不能发邮件！！！");

        //测试发送附件
       /* emailUtil.setInitData("smtp.163.com","1234@163.com","1234");
        Map<String,String> map = new HashMap<String, String>();
        map.put("test12.xls", "D:\\tomcat8\\apache-tomcat-8.0.29\\test12.xls");
        map.put("wsdl.rar", "D:\\wsdl.rar");
        emailUtil.attachedSend("706548532@qq.com","Hello Attachment","This is a mail with attachment",map);
       */

        //测试发送富文本（html文件）
      /*  emailUtil.setInitData("smtp.163.com","1234@163.com","1234");
        String text = "<body><p style='color:red;'>Hello Html Email</p><img src='cid:file'/></body>";
        Map<String,String> map = new HashMap<String, String>();
        map.put("file", "E:\\1f7827.jpg");
        emailUtil.richContentSend("706548532@qq.com","邮件标题",text,map);*/

        //测试群发多人多附件
//        emailUtil.setInitData("smtp.163.com","1234@163.com","1234");
//        String [] address = {"706548532@qq.com","1326624701@qq.com"};
//        String [] filePath = {"D:\\tomcat8\\apache-tomcat-8.0.29\\test12.xls", "D:\\wsdl.rar"};
//        emailUtil.sendBatchMailWithFile(address, "群发多文件", "实时",  filePath);
    }

}
