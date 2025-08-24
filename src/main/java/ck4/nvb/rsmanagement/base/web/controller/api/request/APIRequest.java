package ck4.nvb.rsmanagement.base.web.controller.api.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class APIRequest<T extends Serializable> implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  private APIRequestHeader meta;

  private T body;

  @Override
  public String toString() {
    ObjectMapper mapper = new ObjectMapper();
    try {
      String json = mapper.writeValueAsString(this);
      return "[" + super.getClass().getSimpleName() + "] " + json;
    } catch (JsonProcessingException e) {
      return super.toString();
    }
  }
}
