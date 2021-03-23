package com.linkFlow.manager.common.model;

import lombok.Getter;

@Getter
public enum ReturnCode
{
	SUCCESS						(0, "Success"),
	PARTIALLY_FAILED			(1, "Partially failed"),

	OUT_OF_AUTHORITY			(2, "Out of Authority"),
    BAD_REQUEST					(3, "Bad request"),
	AUTHENTICATION_FAILED		(4, "Password does not match"),
	FIELD_VALIDATE_FAIL			(5, "Field validation failed."),
	REQUIRED_FIELDS_MISSING		(6, "Required fields missing."),
    IP_NOT_AUTHORIZED           (7, "IP is not authorized"),
	DATA_NOT_FOUND 				(8, "Data not found"),
	DATA_UPDATE_FAILED			(9, "Data update failed"),
	DATA_ALREADY_EXISTS			(10, "Data already exists"),
	REQUEST_ALREADY_PROCESSED	(11, "Request already processed"),
	REQUEST_ALREADY_REJECTED	(12, "Request already rejected"),
	INVALID_CONFIG				(13, "Invalid config"),
	INVALID_IP_FORMAT			(14, "Invalid IP format"),
	INVALID_TIME_FORMAT			(15, "Invalid time format. required : UTC, millisecond, long"),
	INVALID_DATE_RANGE			(16, "Invalid date range"),
	INVALID_REQUEST_DATA		(17, "Invalid request data"),
	IMPROPER_DATA_ON_REQUEST	(18, "Improper data exists on request"),

	REQUEST_TIMEOUT				(19, "Request timeout"),

    INVALID_PRIOR_STATE			(21, "Prior state is wrong"),

	USER_KYC_NOT_REQUESTED		(31, "KYC is not requested"),
	USER_KYC_REJECTED			(32, "KYC rejected"),
	USER_KYC_ON_REVIEW			(33, "KYC is on review"),

	ACCOUNT_NOT_FOUND			(4100, "Account not found"),
	ACCOUNT_ALREADY_EXISTS		(4200, "Account already exist"),
	ACCOUNT_BLOCKED				(4300, "Account is blocked"),
    OUT_OF_BALANCE				(4400, "Account balance is not sufficient"),
	NOT_PREPAID_TYPE			(4500, "Account is not prepaid type"),
	DELETE_UNAVAILABLE			(4700, "Cannot delete due to existing data"),
	PARENT_IS_BLOCKED			(4800, "Parent is blocked. Change parent first"),
	PASSWORD_NOT_MATCH			(4900, "password is not match"),
	PASSWORD_LENGTH_SHORT		(5000, "password length is too short"),
	TOO_MANY_CHILD				(5100, "Too many child operator exist."),
    CHILD_CONFIG_MUST_EQUAL_TO_PARENT(5200, "This config must equal to parent data"),
	AGENT_CODE_ALREADY_EXISTS	(5300, "AgentCode already exists"),
	PROFIT_RATE_EXCEEDED		(5400, "Profit rate exceeded"),
	QR_GENERATE_ERROR			(5500, "Failed to generate QrCode"),
	ACCOUNT_GENERATE_ERROR		(5600, "Failed to generate new Account"),
	TOO_MANY_WRONG_REQUEST		(5700, "Too many wrong request.  Account is blocked in 3 minute"),
	ACCOUNT_DELETED				(5800, "Account is deleted"),
    REFERRAL_DELETED			(5900, "Referral not found"),

	PRIOR_REQUEST_EXIST			(6100, "Prior request exists"),
    PROCESSED_WITHDRAWAL        (6200, "Withdrawal is already processed before"),
    TOKEN_WITHDRAWAL_LOCKED     (6300, "Withdrawal is locked on this token"),
	TOKEN_SWAP_LOCKED     		(6400, "Swap is locked on this token"),
    UNDER_MIN_WITHDRAW_AMOUNT   (6500, "Under min withdraw amount"),
	TOKEN_PRICE_UPDATE_FAILED	(6600, "Token price update failed"),


	TICKET_NOT_EXIST			(7100, "Ticket not exists"),
	TICKET_EXPIRED				(7200, "Ticket is expired"),
	TICKET_VALIDATION_FAILED	(7300, "Ticket validation failed"),
	TICKET_GENERATE_FAILED		(7400, "Ticket generation failed"),
	TICKET_NOT_LATEST			(7500, "Ticket is not latest"),

	PRODUCT_NOT_FOUND			(8100, "Product not found"),
	PRODUCT_BLOCKED				(8200, "Product is blocked"),
	PRODUCT_INVALID_AMOUNT		(8300, "Invalid amount for product"),
	PRODUCT_SOLD_OUT			(8400, "Product sold out"),
	PRODUCT_PURCHASE__LIMITED	(8500, "Product purchase is limited"),
    PRODUCT_LOCKUP              (8600, "Product with lockup."),
	PRODUCT_LEVEL_NOT_MATCH		(8700, "Product level not match."),
	PRODUCT_PRICE_NOT_MATCH     (8800, "Product price is not match."),
	PRODUCT_PRICE_EXCEEDED		(8801, "Product price exceeded."),

	GIFTICON_REFERENCE_NOT_FOUNT(8900, "Gifticon reference info not found."),


    API_RESTRICTED              (9100, "Api is restricted for a moment"),

	INTERNAL_ERROR				(10100, "Internal error"),
    UNDER_MAINTENANCE			(10200, "Service Maintenance"),
	SYSTEM_IS_BUSY_TRY_AGAIN	(10300, "System is busy, try again after seconds"),
	NOT_IMPLEMENTED				(10400, "Not implemented"),
	CONFIG_DISABLED				(10500, "Config disabled"),
    DISABLED_FOR_SCHEDULED_TASK (10600, "Disabled for scheduling task."),
    DISABLED_FOR_SCHEDULED_UPDATE(10700, "시스템 업데이트중.  3분정도 뒤에 다시 시도하세요"),

	CERTIFICATION_LIMIT_OVER	(11100, "Too many certification request"),
	SMS_SENT					(11200, "Sent by SMS"),
	SMS_ERROR					(11300, "SMS service error"),
	CERTIFICATION_WRONG			(11400, "Certification wrong"),
	CERTIFICATION_MATCH			(11500, "Certification match"),
	CERTIFICATION_SEND_ERROR	(11600, "Certification send failed"),

	TEMP_PASSWORD_SENT_BY_SMS	(12100, "Temporary password is sent by sms"),

	ERC20_ADDRESS_MISSING		(13100, "ERC20 address not set"),
	ERC20_API_ERROR				(13200, "ERC20 API error"),
	TOKEN_TYPE_NOT_MATCH		(13300, "Token type not match"),


	EOS_REQUEST_TIMEOUT			(20100, "EOS request timeout"),
	EOS_REQUEST_ERROR			(20200, "EOS request error"),

	HTTP_TROUBLE				(90100, "Http trouble."),
	UNKNOWN_ERROR				(99900, "Error code not defined.");

	private Integer code;
	private String message;

	ReturnCode(Integer code, String message)
	{
		this.code = code;
		this.message = message;
	}

	public static ReturnCode convertCodeToEnum(int code)
	{
		ReturnCode result = null;

		ReturnCode[] returnCodes = ReturnCode.values();

		for (ReturnCode returnCode : returnCodes)
		{
			if (returnCode.getCode() == code)
			{
				result = returnCode;
				break;
			}
		}

		return result;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}