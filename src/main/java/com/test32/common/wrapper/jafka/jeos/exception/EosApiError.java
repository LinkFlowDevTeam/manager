package com.test32.common.wrapper.jafka.jeos.exception;

public class EosApiError {

    private String message;

    private int code;

    private EosError error;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public EosError getError() {
        return error;
    }

    public void setError(EosError error) {
        this.error = error;
    }

    

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EosApiError [message=");
        builder.append(message);
        builder.append(", code=");
        builder.append(code);
        builder.append(", error=");
        builder.append(error);
        builder.append("]");
        return builder.toString();
    }

    public String getDetailedMessage() {
        String result = error == null ? message : message + ": " + error.getWhat();
        if(error != null && error.getDetails() != null)
        {
            for(EosErrorDetails item : error.getDetails())
            {
                result = result + ", detail: " + item.getMessage();
            }
        }

        return result;
    }

    public Integer getEosErrorCode() {
        return error == null ? null : error.getCode();
    }
}
