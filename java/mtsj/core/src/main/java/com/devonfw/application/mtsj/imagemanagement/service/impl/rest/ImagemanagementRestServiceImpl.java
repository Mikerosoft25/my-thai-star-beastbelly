package com.devonfw.application.mtsj.imagemanagement.service.impl.rest;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.domain.Page;

import com.devonfw.application.mtsj.general.common.impl.security.ApplicationAccessControlConfig;
import com.devonfw.application.mtsj.imagemanagement.logic.api.Imagemanagement;
import com.devonfw.application.mtsj.imagemanagement.logic.api.to.ImageEto;
import com.devonfw.application.mtsj.imagemanagement.logic.api.to.ImageSearchCriteriaTo;
import com.devonfw.application.mtsj.imagemanagement.service.api.rest.ImagemanagementRestService;

/**
 * The service implementation for REST calls in order to execute the logic of component {@link Imagemanagement}.
 */
@Named("ImagemanagementRestService")
public class ImagemanagementRestServiceImpl implements ImagemanagementRestService {

  @Inject
  private Imagemanagement imagemanagement;

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_FIND_IMAGE)
  public ImageEto getImage(long id) {

    return this.imagemanagement.findImage(id);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SAVE_IMAGE)
  public ImageEto saveImage(ImageEto image) {

    return this.imagemanagement.saveImage(image);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_DELETE_IMAGE)
  public void deleteImage(long id) {

    this.imagemanagement.deleteImage(id);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SEARCH_IMAGES)
  public Page<ImageEto> findImagesByPost(ImageSearchCriteriaTo searchCriteriaTo) {

    return this.imagemanagement.findImageEtos(searchCriteriaTo);
  }

}