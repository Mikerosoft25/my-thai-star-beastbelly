package com.devonfw.application.mtsj.mailservice.logic.impl;

import java.util.Map;

import javax.inject.Named;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.devonfw.application.mtsj.mailservice.logic.api.Mail;

/**
 * Class for Mail service
 *
 */
@Named
// outcomment this one for deployment
@ConditionalOnProperty(prefix = "spring.mail", name = "enabled", havingValue = "true")
public class MailImpl implements Mail {

  /** Logger instance. */
  private static final Logger LOG = LoggerFactory.getLogger(MailImpl.class);

  /**
   * Mailsender for sending emails
   */
  @Autowired
  public JavaMailSender mailSender;

  /**
   * TemplateEngine by thymeleaf. Used for creating html-emails while using a template.
   */
  @Autowired
  public SpringTemplateEngine thymeleafTemplateEngine;

  @Override
  public boolean sendMail(String to, String subject, String text) {

    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("MyThaiStar");
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);

    try {
      this.mailSender.send(message);
      return true;
    } catch (Exception e) {
      LOG.error("Error sending mail: " + e);
      return false;
    }
  }

  @Override
  @Async
  public boolean sendHtmlMail(String to, String subject, String htmlBody) {

    try {
      MimeMessage message = this.mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
      helper.setFrom("test");
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(htmlBody, true);

      helper.addInline("myThaiStarBannerImage", new ClassPathResource("static/images/mail_banner.png"));
      helper.addInline("myThaiStarFooterImage", new ClassPathResource("static/images/mail_footer.png"));

      this.mailSender.send(message);
      return true;

    } catch (Exception e) {
      LOG.error("Error sending mail: " + e);
      return false;
    }
  }

  @Override
  @Async
  public void sendPasswordResetMail(String to, Map<String, Object> templateModel) {

    Context ctx = new Context();
    ctx.setVariables(templateModel);

    String htmlBody = this.thymeleafTemplateEngine.process("password-reset.html", ctx);
    sendHtmlMail(to, "Reset Password - MyThaiStar", htmlBody);
  }

  @Override
  @Async
  public void sendBookingConfirmationMailToHost(String to, Map<String, Object> templateModel) {

    Context ctx = new Context();
    templateModel.put("mailType", "confirmation");
    ctx.setVariables(templateModel);

    String htmlBody = this.thymeleafTemplateEngine.process("booking-information.html", ctx);
    sendHtmlMail(to, "Booking Confirmation - MyThaiStar", htmlBody);
  }

  @Override
  @Async
  public void sendBookingInviteAcceptMailToHost(String to, Map<String, Object> templateModel) {

    Context ctx = new Context();
    templateModel.put("mailType", "accept");
    ctx.setVariables(templateModel);

    String htmlBody = this.thymeleafTemplateEngine.process("booking-information.html", ctx);
    sendHtmlMail(to, "Booking Invite Accepted - MyThaiStar", htmlBody);
  }

  @Override
  @Async
  public void sendBookingInviteDeclineMailToHost(String to, Map<String, Object> templateModel) {

    Context ctx = new Context();
    templateModel.put("mailType", "decline");
    ctx.setVariables(templateModel);

    String htmlBody = this.thymeleafTemplateEngine.process("booking-information.html", ctx);
    sendHtmlMail(to, "Booking Invite Declined - MyThaiStar", htmlBody);
  }

  @Override
  @Async
  public void sendBookingInviteMailToGuest(String to, Map<String, Object> templateModel) {

    Context ctx = new Context();
    templateModel.put("mailType", "invite");
    ctx.setVariables(templateModel);

    String htmlBody = this.thymeleafTemplateEngine.process("booking-invitation.html", ctx);
    sendHtmlMail(to, "Booking Invitation - MyThaiStar", htmlBody);
  }

  @Override
  @Async
  public void sendBookingInviteAcceptMailToGuest(String to, Map<String, Object> templateModel) {

    Context ctx = new Context();
    templateModel.put("mailType", "accept");
    ctx.setVariables(templateModel);

    String htmlBody = this.thymeleafTemplateEngine.process("booking-invitation.html", ctx);
    sendHtmlMail(to, "Booking Invite Accepted - MyThaiStar", htmlBody);

  }

  @Override
  @Async
  public void sendBookingInviteDeclineMailToGuest(String to, Map<String, Object> templateModel) {

    Context ctx = new Context();
    templateModel.put("mailType", "decline");
    ctx.setVariables(templateModel);

    String htmlBody = this.thymeleafTemplateEngine.process("booking-invitation.html", ctx);
    sendHtmlMail(to, "Booking Invite Declined - MyThaiStar", htmlBody);

  }

  @Override
  @Async
  public void sendOrderConfirmationMail(String to, Map<String, Object> templateModel) {

    Context ctx = new Context();
    ctx.setVariables(templateModel);

    String htmlBody = this.thymeleafTemplateEngine.process("order-confirmation.html", ctx);
    sendHtmlMail(to, "Order Confirmation - MyThaiStar", htmlBody);
  }

  @Override
  @Async
  public void sendBookingCancellationMailToHost(String to, Map<String, Object> templateModel) {

    Context ctx = new Context();
    templateModel.put("mailType", "cancel");
    ctx.setVariables(templateModel);

    String htmlBody = this.thymeleafTemplateEngine.process("booking-information.html", ctx);
    sendHtmlMail(to, "Booking Canceled - MyThaiStar", htmlBody);

  }

  @Override
  @Async
  public void sendBookingCancellationMailToGuest(String to, Map<String, Object> templateModel) {

    Context ctx = new Context();
    templateModel.put("mailType", "cancel");
    ctx.setVariables(templateModel);

    String htmlBody = this.thymeleafTemplateEngine.process("booking-invitation.html", ctx);
    sendHtmlMail(to, "Booking Invitation Canceled - MyThaiStar", htmlBody);

  }

}
