package com.devonfw.application.mtsj.dishmanagement.service.impl.rest;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.domain.Page;

import com.devonfw.application.mtsj.dishmanagement.logic.api.Dishmanagement;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.CategoryEto;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.CategorySearchCriteriaTo;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.DishCto;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.DishEto;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.DishSearchCriteriaTo;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.IngredientEto;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.IngredientSearchCriteriaTo;
import com.devonfw.application.mtsj.dishmanagement.service.api.rest.DishmanagementRestService;
import com.devonfw.application.mtsj.general.common.impl.security.ApplicationAccessControlConfig;

/**
 * The service implementation for REST calls in order to execute the logic of component {@link Dishmanagement}.
 */
@Named("DishmanagementRestService")
public class DishmanagementRestServiceImpl implements DishmanagementRestService {

  @Inject
  private Dishmanagement dishmanagement;

  @Override
  @PermitAll
  public CategoryEto getCategory(long id) {

    return this.dishmanagement.findCategory(id);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SAVE_CATEGORY)
  public CategoryEto saveCategory(CategoryEto category) {

    return this.dishmanagement.saveCategory(category);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_DELETE_CATEGORY)
  public void deleteCategory(long id) {

    this.dishmanagement.deleteCategory(id);
  }

  @Override
  @PermitAll
  public Page<CategoryEto> findCategorysByPost(CategorySearchCriteriaTo searchCriteriaTo) {

    return this.dishmanagement.findCategoryEtos(searchCriteriaTo);
  }

  @Override
  @PermitAll
  public DishCto getDish(long id) {

    return this.dishmanagement.findDish(id);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SAVE_DISH)
  public DishEto saveDish(DishEto dish) {

    return this.dishmanagement.saveDish(dish);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_DELETE_DISH)
  public void deleteDish(long id) {

    this.dishmanagement.deleteDish(id);
  }

  @Override
  @PermitAll
  public Page<DishCto> findDishsByPost(DishSearchCriteriaTo searchCriteriaTo) {

    Page<DishCto> pageDishCto = this.dishmanagement.findDishCtos(searchCriteriaTo);
    return pageDishCto;
  }

  @Override
  @PermitAll
  public IngredientEto getIngredient(long id) {

    return this.dishmanagement.findIngredient(id);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SAVE_INGREDIENT)
  public IngredientEto saveIngredient(IngredientEto ingredient) {

    return this.dishmanagement.saveIngredient(ingredient);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_DELETE_INGREDIENT)
  public void deleteIngredient(long id) {

    this.dishmanagement.deleteIngredient(id);
  }

  @Override
  @PermitAll
  public Page<IngredientEto> findIngredientsByPost(IngredientSearchCriteriaTo searchCriteriaTo) {

    return this.dishmanagement.findIngredientEtos(searchCriteriaTo);
  }

}
