package com.linkFlow.manager.common.model.define;

public enum OperatorType
{
    ADMIN       (0, "ADMIN"),
    MASTER      (1, "MASTER"),
    OPERATOR    (2, "OPERATOR"),
    AGENT       (3, "AGENT");

    private int code;

    private String description;

    OperatorType(int code, String description)
    {
        this.code = code;
        this.description = description;
    }

    public int getCode()
    {
        return code;
    }

    public String getDescription()
    {
        return description;
    }

    public static OperatorType convertCodeToEnum(int code)
    {
        OperatorType result = null;
        OperatorType[] operatorTypes = OperatorType.values();

        for(OperatorType operatorType : operatorTypes)
        {
            if (operatorType.getCode() == code)
            {
                result = operatorType;
                break;
            }
        }

        return result;
    }
}
