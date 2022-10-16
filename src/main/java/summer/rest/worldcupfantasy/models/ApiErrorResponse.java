package summer.rest.worldcupfantasy.models;


import org.springframework.http.HttpStatus;

import java.util.LinkedHashMap;


public class ApiErrorResponse extends Exception {
    private final Integer status;
    private final String message;

    public ApiErrorResponse(HttpStatus status, String message) {
        super(message);
        this.status = status.value();
        this.message = message;
    }

    public LinkedHashMap<String,Object> getBody() {
        return new LinkedHashMap<>(){{
            put("success",false);
            put("status", status);
            put("message", message);
            put("data", null);
        }};
    }
}
