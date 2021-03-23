package com.test32.common.model.type;

public enum ServerModeType
{
    DEBUG(0, "DEBUG MODE"),
    RELEASE(1, "RELEASE MODE");

    private Integer code;
    private String description;

    ServerModeType(Integer code, String description)
    {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ServerModeType convertToType(Integer code)
    {
        ServerModeType result = ServerModeType.DEBUG;

        switch (code)
        {
            case 0:
                result = ServerModeType.DEBUG;
                break;
            case 1:
                result = ServerModeType.RELEASE;
                break;
            default:
                result = ServerModeType.DEBUG;
                break;
        }

        return result;
    }
}
