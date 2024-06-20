package com.example.ourknowledgebackend.api.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

public class FieldErrorDTO {

  private String fieldName;

  private String message;

  public FieldErrorDTO(String fieldName, String message) {
    this.fieldName = fieldName;
    this.message = message;

  }

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
