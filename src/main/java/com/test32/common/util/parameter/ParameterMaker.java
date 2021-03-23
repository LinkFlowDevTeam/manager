package com.test32.common.util.parameter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ParameterMaker
{
	public static Map<String, Object> getParamMap(Object requestObject) throws IllegalAccessException
	{
		Map<String, Object> result = new HashMap<String, Object>();

		if (requestObject != null)
		{
			/**
			 * 선언된 변수 필드 리스트를 가져온다.
			 */
			if (requestObject.getClass().getSuperclass() != null)
			{
				Field[] parentFields = requestObject.getClass().getSuperclass().getDeclaredFields();
				for (Field field : parentFields)
				{
					field.setAccessible(true);
					Object value = field.get(requestObject);
					if (value != null)
					{
						/**
						 * ParamName 어노테이션 유무 확인.
						 */
						if (field.isAnnotationPresent(ParamName.class))
						{
							ParamName paramName = field.getDeclaredAnnotation(ParamName.class);
							String parameterValue = paramName.value();

							String key;

							/**
							 * 파라미터 값이 공백(디폴트값)이면, 변수명을 사용하도록 함.
							 */
							if (parameterValue.isEmpty())
							{
								key = field.getName();
							}
							else
							{
								key = paramName.value();
							}

							result.put(key, value);
						}
					}
				}
			}

			Field[] fields = requestObject.getClass().getDeclaredFields();
			for (Field field : fields)
			{
				field.setAccessible(true);
				Object value = field.get(requestObject);
				if (value != null)
				{
					/**
					 * ParamName 어노테이션 유무 확인.
					 */
					if (field.isAnnotationPresent(ParamName.class))
					{
						ParamName paramName = field.getDeclaredAnnotation(ParamName.class);
						String parameterValue = paramName.value();
						String key;

						/**
						 * 파라미터 값이 공백(디폴트값)이면, 변수명을 사용하도록 함.
						 */
						if (parameterValue.isEmpty())
						{
							key = field.getName();
						}
						else
						{
							key = paramName.value();
						}

						field.setAccessible(true);
						result.put(key, value);
					}
				}
			}
		}
		else
		{
			throw new NullPointerException();
		}

		return result;
	}

	public static String getParamString(Object requestObject) throws IllegalAccessException
	{
		String result = "";

		StringBuilder sb = new StringBuilder(500);

		if (requestObject != null)
		{
			/**
			 * 선언된 변수 필드 리스트를 가져온다.
			 */
			if (requestObject.getClass().getSuperclass() != null)
			{
				Field[] parentFields = requestObject.getClass().getSuperclass().getDeclaredFields();
				for (Field field : parentFields)
				{
					field.setAccessible(true);
					Object value = field.get(requestObject);
					if (value != null)
					{
						/**
						 * ParamName 어노테이션 유무 확인.
						 */
						if (field.isAnnotationPresent(ParamName.class))
						{
							if (sb.length() > 1)
							{
								sb.append("&");
							}

							ParamName paramName = field.getDeclaredAnnotation(ParamName.class);
							String parameterValue = paramName.value();

							/**
							 * 파라미터 값이 공백(디폴트값)이면, 변수명을 사용하도록 함.
							 */
							if (parameterValue.isEmpty())
							{
								sb.append(field.getName());
							}
							else
							{
								sb.append(paramName.value());
							}
							sb.append("=").append(value);
						}
					}
				}
			}

			Field[] fields = requestObject.getClass().getDeclaredFields();
			for (Field field : fields)
			{
				field.setAccessible(true);
				Object value = field.get(requestObject);
				if (value != null)
				{
					/**
					 * ParamName 어노테이션 유무 확인.
					 */
					if (field.isAnnotationPresent(ParamName.class))
					{
						if (sb.length() > 1)
						{
							sb.append("&");
						}

						ParamName paramName = field.getDeclaredAnnotation(ParamName.class);
						String parameterValue = paramName.value();

						/**
						 * 파라미터 값이 공백(디폴트값)이면, 변수명을 사용하도록 함.
						 */
						if (parameterValue.isEmpty())
						{
							sb.append(field.getName());
						}
						else
						{
							sb.append(paramName.value());
						}

						field.setAccessible(true);
						sb.append("=").append(value);
					}
				}
			}
			result = sb.toString();
		}
		else
		{
			throw new NullPointerException();
		}

		return result;
	}
}
