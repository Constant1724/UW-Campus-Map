package campuspaths;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * This class defines a intern server exception
 *
 * The @ResponseStatus annotation marks this Error to be an Internal Server Error so that when it is thrown, the API
 * recognizes the error status code. Different exceptions can be created with different response codes including 200
 * and 404
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ServerSideException extends Exception {

    public ServerSideException(String message) {
        super(message);
    }

    public ServerSideException(Exception e) {
        super(e);
    }

}
