package com.devonfw.application.mtsj.mailservice.logic.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

/**
 * Configuration Class for thymeleaf templates.
 */
@Configuration
public class TemplateConfiguration {

  /**
   * @return the thymeleaf template resolver.
   */
  @Bean
  public ITemplateResolver thymeleafClassLoaderTemplateResolver() {

    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setPrefix("templates/");
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode("HTML");
    templateResolver.setCharacterEncoding("UTF-8");
    return templateResolver;
  }

  /**
   * @return the thymeleaf template engine.
   */
  @Bean
  public SpringTemplateEngine thymeleafTemplateEngine() {

    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(thymeleafClassLoaderTemplateResolver());
    return templateEngine;
  }

}
