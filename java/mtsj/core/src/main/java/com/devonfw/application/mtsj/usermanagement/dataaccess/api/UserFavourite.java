package com.devonfw.application.mtsj.usermanagement.dataaccess.api;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.devonfw.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity;

/**
 * The {@link com.devonfw.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity persistent entity} for
 * {@link UserFavourite}.
 *
 */
@Entity
@Table(name = "UserFavourite")
public class UserFavourite extends ApplicationPersistenceEntity {

  private static final long serialVersionUID = 1L;

  private Long idUser;

  private Long idDish;

  /**
   * @return idUser
   */
  public Long getIdUser() {

    return this.idUser;
  }

  /**
   * @param idUser new value of {@link #getIdUser}.
   */
  public void setIdUser(Long idUser) {

    this.idUser = idUser;
  }

  /**
   * @return idDish
   */
  public Long getIdDish() {

    return this.idDish;
  }

  /**
   * @param idDish new value of {@link #getIdDish}.
   */
  public void setIdDish(Long idDish) {

    this.idDish = idDish;
  }

}
