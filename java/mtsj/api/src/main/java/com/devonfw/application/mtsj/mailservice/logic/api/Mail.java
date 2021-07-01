package com.devonfw.application.mtsj.mailservice.logic.api;

import java.util.Map;

/**
 * Interface for Mail Service
 *
 */
public interface Mail {

  /**
   * Sends an email using the spring boot mail module.
   *
   * @param to who receives the email.
   * @param subject the subject of the mail.
   * @param text the content of the mail.
   *
   * @return the result of the sending process
   */
  boolean sendMail(String to, String subject, String text);

  /**
   * Sends an email with a html body.
   *
   * @param to who receives the email.
   * @param subject the subject of the mail.
   * @param htmlBody the content of the mail as html.
   *
   * @return the result of the sending process
   */
  boolean sendHtmlMail(String to, String subject, String htmlBody);

  /**
   * Sends a password reset email.
   *
   * @param to who receives the email
   * @param templateModel contains all key-value pairs for the html-template.
   */
  void sendPasswordResetMail(String to, Map<String, Object> templateModel);

  /**
   * Sends a booking confirmation email to the host of the booking using thymeleaf.
   *
   * @param to who receives the email.
   * @param templateModel contains all key-value pairs for the html-template.
   */
  void sendBookingConfirmationMailToHost(String to, Map<String, Object> templateModel);

  /**
   * Sends a confirmation email to the host of a booking that an invited guest accepted the invite.
   *
   * @param to who receives the email.
   * @param templateModel contains all key-value pairs for the html-template.
   */
  void sendBookingInviteAcceptMailToHost(String to, Map<String, Object> templateModel);

  /**
   * Sends a confirmation email to the host of a booking that an invited guest declined the invite.
   *
   * @param to who receives the email.
   * @param templateModel contains all key-value pairs for the html-template.
   */
  void sendBookingInviteDeclineMailToHost(String to, Map<String, Object> templateModel);

  /**
   * Sends an invitation email for a booking to an invited guest.
   *
   * @param to who receives the email.
   * @param templateModel contains all key-value pairs for the html-template.
   */
  void sendBookingInviteMailToGuest(String to, Map<String, Object> templateModel);

  /**
   * Sends a confirmation email to an invited guest that he accepted the invite.
   *
   * @param to who receives the email.
   * @param templateModel contains all key-value pairs for the html-template.
   */
  void sendBookingInviteAcceptMailToGuest(String to, Map<String, Object> templateModel);

  /**
   * Sends a confirmation email to an invited guest that he declined the invite.
   *
   * @param to who receives the email.
   * @param templateModel contains all key-value pairs for the html-template.
   */
  void sendBookingInviteDeclineMailToGuest(String to, Map<String, Object> templateModel);

  /**
   * Sends an order-confirmation email to the host of a booking containing the infos of the order and the booking.
   *
   * @param to who receives the email.
   * @param templateModel contains all key-value pairs for the html-template.
   */
  void sendOrderConfirmationMail(String to, Map<String, Object> templateModel);

  /**
   * Sends a confirmation email to the host of a booking that the booking was canceled.
   *
   * @param to who receives the email.
   * @param templateModel contains all key-value pairs for the html-template.
   */
  void sendBookingCancellationMailToHost(String to, Map<String, Object> templateModel);

  /**
   * Sends a confirmation email to the guest of a booking that the booking was canceled.
   *
   * @param to who receives the email.
   * @param templateModel contains all key-value pairs for the html-template.
   */
  void sendBookingCancellationMailToGuest(String to, Map<String, Object> templateModel);
}
