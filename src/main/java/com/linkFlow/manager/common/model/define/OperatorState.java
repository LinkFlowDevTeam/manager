package com.linkFlow.manager.common.model.define;

public enum OperatorState
{
    USE     (0, "USE"),
    BLOCK   (1, "BLOCK"),
    ASK     (2, "ASK");

    private int code;
    private String description;

    OperatorState(int code, String description)
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

    public static OperatorState convertCodeToEnum(int code)
    {
        OperatorState result = null;
        OperatorState[] operatorStates = OperatorState.values();

        for(OperatorState operatorState : operatorStates)
        {
            if (operatorState.getCode() == code)
            {
                result = operatorState;
                break;
            }
        }
        return result;
    }
}
