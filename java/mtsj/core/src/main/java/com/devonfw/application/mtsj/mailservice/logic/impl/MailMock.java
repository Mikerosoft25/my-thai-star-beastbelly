package com.devonfw.application.mtsj.mailservice.logic.impl;

import java.util.Map;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.devonfw.application.mtsj.mailservice.logic.api.Mail;

/**
 * Mock class for Mail Service. The mail content is printed as a Log
 *
 */
@Named
@ConditionalOnProperty(prefix = "spring.mail", name = "enabled", havingValue = "false")
public class MailMock implements Mail {

  /**
   * Logger instance.
   */
  private static final Logger LOG = LoggerFactory.getLogger(MailMock.class);

  /**
   * TemplateEngine by thymeleaf. Used for creating html-emails while using a template.
   */
  @Autowired
  public SpringTemplateEngine thymeleafTemplateEngine;

  @Override
  public boolean sendMail(String to, String subject, String text) {

    StringBuilder sb = new StringBuilder();
    sb.append("To: ").append(to).append("|").append("Subject: ").append(subject).append("|").append("Text: ")
        .append(text);
    LOG.info(sb.toString());
    return true;
  }

  @Override
  public boolean sendHtmlMail(String to, String subject, String htmlBody) {

    LOG.info(htmlBody);
    return true;
  }

  @Override
  public void sendPasswordResetMail(String to, Map<String, Object> templateModel) {

    Context ctx = new Context();
    ctx.setVariables(templateModel);

    String htmlBody = this.thymeleafTemplateEngine.process("password-reset.html", ctx);
    sendHtmlMail(to, "Reset Password - MyThaiStar", htmlBody);
  }

  @Override
  public void sendBookingConfirmationMailToHost(String to, Map<String, Object> templateModel) {

    Context ctx = new Context();
    templateModel.put("mailType", "confirmation");
    ctx.setVariables(templateModel);

    String htmlBody = this.thymeleafTemplateEngine.process("booking-information.html", ctx);
    sendHtmlMail(to, "Booking Confirmation - MyThaiStar", htmlBody);
  }

  @Override
  public void sendBookingInviteAcceptMailToHost(String to, Map<String, Object> templateModel) {

    Context ctx = new Context();
    templateModel.put("mailType", "accept");
    ctx.setVariables(templateModel);

    String htmlBody = this.thymeleafTemplateEngine.process("booking-information.html", ctx);
    sendHtmlMail(to, "Booking Invite Accepted - MyThaiStar", htmlBody);
  }

  @Override
  public void sendBookingInviteDeclineMailToHost(String to, Map<String, Object> templateModel) {

    Context ctx = new Context();
    templateModel.put("mailType", "decline");
    ctx.setVariables(templateModel);

    String htmlBody = this.thymeleafTemplateEngine.process("booking-information.html", ctx);
    sendHtmlMail(to, "Booking Invite Declined - MyThaiStar", htmlBody);
  }

  @Override
  public void sendBookingInviteMailToGuest(String to, Map<String, Object> templateModel) {

    Context ctx = new Context();
    templateModel.put("mailType", "invite");
    ctx.setVariables(templateModel);

    String htmlBody = this.thymeleafTemplateEngine.process("booking-invitation.html", ctx);
    sendHtmlMail(to, "Booking Invitation - MyThaiStar", htmlBody);
  }

  @Override
  public void sendBookingInviteAcceptMailToGuest(String to, Map<String, Object> templateModel) {

    Context ctx = new Context();
    templateModel.put("mailType", "accept");
    ctx.setVariables(templateModel);

    String htmlBody = this.thymeleafTemplateEngine.process("booking-invitation.html", ctx);
    sendHtmlMail(to, "Booking Invite Accepted - MyThaiStar", htmlBody);

  }

  @Override
  public void sendBookingInviteDeclineMailToGuest(String to, Map<String, Object> templateModel) {

    Context ctx = new Context();
    templateModel.put("mailType", "decline");
    ctx.setVariables(templateModel);

    String htmlBody = this.thymeleafTemplateEngine.process("booking-invitation.html", ctx);
    sendHtmlMail(to, "Booking Invite Declined - MyThaiStar", htmlBody);

  }

  @Override
  public void sendOrderConfirmationMail(String to, Map<String, Object> templateModel) {

    Context ctx = new Context();
    ctx.setVariables(templateModel);

    String htmlBody = this.thymeleafTemplateEngine.process("order-confirmation.html", ctx);
    sendHtmlMail(to, "Order Confirmation - MyThaiStar", htmlBody);
  }

  @Override
  public void sendBookingCancellationMailToHost(String to, Map<String, Object> templateModel) {

    Context ctx = new Context();
    templateModel.put("mailType", "cancel");
    ctx.setVariables(templateModel);

    String htmlBody = this.thymeleafTemplateEngine.process("booking-information.html", ctx);
    sendHtmlMail(to, "Booking Canceled - MyThaiStar", htmlBody);

  }

  @Override
  public void sendBookingCancellationMailToGuest(String to, Map<String, Object> templateModel) {

    Context ctx = new Context();
    templateModel.put("mailType", "cancel");
    ctx.setVariables(templateModel);

    String htmlBody = this.thymeleafTemplateEngine.process("booking-invitation.html", ctx);
    sendHtmlMail(to, "Booking Invitation Canceled - MyThaiStar", htmlBody);

  }
}
