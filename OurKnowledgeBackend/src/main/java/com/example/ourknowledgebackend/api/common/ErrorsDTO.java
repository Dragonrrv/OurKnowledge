package com.example.ourknowledgebackend.api.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

public class ErrorsDTO {

  private List<FieldErrorDTO> fieldErrors = new ArrayList<FieldErrorDTO>();

  private String globalError;


  @Builder
  public ErrorsDTO(List<FieldErrorDTO> fieldErrors) {
    this.fieldErrors = fieldErrors;

  }

  public ErrorsDTO(String globalError) {
    this.globalError = globalError;
  }

  public List<FieldErrorDTO> getFieldErrors() {
    return fieldErrors;
  }

  public void setFieldErrors(List<FieldErrorDTO> fieldErrors) {
    this.fieldErrors = fieldErrors;
  }

  public String getGlobalError() {
    return globalError;
  }

  public void setGlobalError(String globalError) {
    this.globalError = globalError;
  }
}
