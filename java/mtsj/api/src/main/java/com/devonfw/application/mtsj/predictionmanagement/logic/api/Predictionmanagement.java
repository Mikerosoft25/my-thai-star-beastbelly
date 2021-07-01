package com.devonfw.application.mtsj.predictionmanagement.logic.api;

import com.devonfw.application.mtsj.predictionmanagement.logic.api.to.PredictionDataTo;
import com.devonfw.application.mtsj.predictionmanagement.logic.api.to.PredictionSearchCriteriaTo;

/**
 * Interface for Predictionmanagement component.
 */
public interface Predictionmanagement {

  PredictionDataTo getNextWeekPrediction(PredictionSearchCriteriaTo criteria);

}
