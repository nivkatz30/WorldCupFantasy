package summer.rest.worldcupfantasy.models;


import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class ApiResponse<T> {

    private final Boolean success;
    private final Integer status;
    private final String message;
    private final T data;

    private ApiResponse(boolean success, Integer status, String message, T data) {
        this.success = success;
        this.status = status;
        this.message = message;
        this.data = data;
    }


    public static <T>ResponseEntity<ApiResponse<T>> respond(Boolean success, HttpStatus status, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>(success,status.value(), message, data);

        return new ResponseEntity<>(response,status);
    }

    public static ResponseEntity<ApiResponse<Object>> respond(Boolean success, HttpStatus status, String message) {
        return ApiResponse.respond(success,status,message,null);
    }

    public static <T>ResponseEntity<ApiResponse<T>> respond(Boolean success, HttpStatus status, T data) {
        return ApiResponse.respond(success,status,"",data);
    }

    public static <T>ResponseEntity<ApiResponse<T>> ok(T data) {
        return respond(true,HttpStatus.OK, data);
    }
}
