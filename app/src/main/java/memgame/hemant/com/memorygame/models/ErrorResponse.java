package memgame.hemant.com.memorygame.models;

import memgame.hemant.com.memorygame.businesslayer.INotifyObject;

/**
 * Error Response model returns error code along with error message.
 *
 * Created by Hemant on 10/6/15.
 */
public class ErrorResponse implements INotifyObject {

    private int errorCode;
    private String message;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
