package ck4.nvb.rsmanagement.base.web.controller.api.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class APIRequest<T extends Serializable> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private APIRequestHeader header;

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
