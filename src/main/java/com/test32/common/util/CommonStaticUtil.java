package com.test32.common.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test32.common.model.WsCommData;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.Key;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommonStaticUtil
{
    public static WsCommData createDefaultWsCommData(String api, Integer stateCode, Object data)
    {
        WsCommData WsCommData = new WsCommData();
        WsCommData.setT(api);
        WsCommData.setS(stateCode);
        WsCommData.setD(data);
        return WsCommData;
    }

    public static String createDefaultWsCommDataByString(String gameId, String api, Integer stateCode, Object data)
    {
        try
        {
            WsCommData WsCommData = new WsCommData();
            WsCommData.setG(gameId);
            WsCommData.setT(api);
            WsCommData.setS(stateCode);
            WsCommData.setD(data);

            return convertObjectToJson(WsCommData);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T convertJsonStringToObject(String jsonString, Class<T> convertObjectClass) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper.readValue(jsonString, convertObjectClass);
    }

    public static String convertObjectToJson(Object obj) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.writeValueAsString(obj);
    }

    public static String convertObjectToJsonWithoutException(Object obj)
    {
        try
        {
            //2019-02-14T14:53:48.780+09:00
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.writeValueAsString(obj);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 서버의 맥 어드레스 주소를 가져오는 메소드. Open JDK 환경에서 지원하지 않음.
     */
    public String getServerMacAddress() throws Exception
    {
        String result;
        try
        {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++)
            {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            result = sb.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    public static String getSessionID()
    {
        return RequestContextHolder.getRequestAttributes().getSessionId();
    }

    public static String getIpAddress()
    {
        String result;

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        result = req.getHeader("X-FORWARDED-FOR");

        if (result == null || "".equals(result))
        {
            result = req.getRemoteAddr();
        }

        return result;
    }
    public static String createSHA1(String str)
    {
        String SHA;
        try
        {
            MessageDigest sh = MessageDigest.getInstance("SHA-1");
            sh.update(str.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();
            for(int i = 0 ; i < byteData.length ; i++){
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            SHA = sb.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            SHA = null;
        }
        return SHA;
    }
    public static String createSHA256(String str)
    {
        String SHA;
        try
        {
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(str.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();
            for(int i = 0 ; i < byteData.length ; i++){
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            SHA = sb.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            SHA = null;
        }
        return SHA;
    }

    public static String createSHA512(String str)
    {
        String SHA;
        try
        {
            MessageDigest sh = MessageDigest.getInstance("SHA-512");
            sh.update(str.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();
            for(int i = 0 ; i < byteData.length ; i++){
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            SHA = sb.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            SHA = null;
        }
        return SHA;
    }

    public static String createMD5(String input)
    {
        String resultHash;
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte byteData[] = md.digest();
            StringBuilder sb = new StringBuilder();

            for (byte aByteData : byteData)
            {
                sb.append(Integer.toString((aByteData & 0xff) + 0x100, 16).substring(1));
            }
            resultHash = sb.toString();
        }
        catch (Exception e)
        {
            System.out.println("## [ERROR] failed to createMD5.  input:" + input);
            e.printStackTrace();
            return null;
        }
        return resultHash.toUpperCase();
    }

    public static String getJdbcUrlPrefix(String jdbcDriverClassName)
    {
        String result;

        switch (jdbcDriverClassName)
        {
            case "net.sf.log4jdbc.DriverSpy":
                result = "jdbc:log4jdbc:mysql://";
                break;

            case "com.mysql.jdbc.Driver":
                result = "jdbc:mysql://";
                break;
            default:
                result = "";
        }

        return result;
    }

    private static Key getAESKey(String key, boolean isFull256) throws Exception {
        Key keySpec;

        byte[] keyBytes;
        if(isFull256)
            keyBytes = new byte[32];
        else
            keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");

        int len = b.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }

        System.arraycopy(b, 0, keyBytes, 0, len);
        keySpec = new SecretKeySpec(keyBytes, "AES");

        return keySpec;
    }

    public static String encodeBase64(String input)
    {
        try
        {
            byte[] targetBytes = input.getBytes("UTF-8");
            Base64.Encoder encoder = Base64.getEncoder();
            byte[] encodedBytes = encoder.encode(targetBytes);
            return new String(encodedBytes);
        }
        catch (Exception e)
        {
            System.out.println("## [ERROR] failed to encode to Base64.  input:" + input);
            e.printStackTrace();
            return null;
        }
    }

    public static String decodeBase64(String input)
    {
        try
        {
            byte[] targetBytes = input.getBytes("UTF-8");
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] resultBytes = decoder.decode(targetBytes);
            return new String(resultBytes);
        }
        catch (Exception e)
        {
            System.out.println("## [ERROR] failed to decode to Base64.  input:" + input);
            e.printStackTrace();
            return null;
        }
    }
    public static String addStringData(String target, String addStr)
    {
        if ("".equals(target) || target == null)
        {
            target = "";
            target += addStr;
        }
        else
        {
            target += ", " + addStr;
        }

        return target;
    }

    public static void replaceCountryCodeForApi(Map<String, Object> paramsMap)
    {
        String targetKey = "country";
        String replaceKey = "cnCode";
        if(paramsMap.containsKey(targetKey))
        {
            String dataValue = paramsMap.get(targetKey).toString();
            paramsMap.remove(targetKey);
            paramsMap.put(replaceKey, dataValue);
        }
    }

    public static void replaceCommaBasedValueForList_int(Map<String, Object> paramsMap, String targetKey)
    {
        if(paramsMap.containsKey(targetKey))
        {
            String dataValue = paramsMap.get(targetKey).toString();
            paramsMap.remove(targetKey);

            List<Integer> list = new ArrayList<>();
            if( ! StringUtils.isEmpty(dataValue))
            {
                String[] split = dataValue.split(",");
                for(String item : split)
                {
                    try
                    {
                        list.add(Integer.parseInt(item.trim()));
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.toString());
                        e.printStackTrace();
                    }
                }
            }
            if(list.size() > 0)
                paramsMap.put(targetKey, list);
        }
    }

    public static void replaceCommaBasedValueForList_string(Map<String, Object> paramsMap, String targetKey)
    {
        if(paramsMap.containsKey(targetKey))
        {
            String dataValue = paramsMap.get(targetKey).toString();
            paramsMap.remove(targetKey);

            List<String> list = new ArrayList<>();
            if( ! StringUtils.isEmpty(dataValue))
            {
                String[] split = dataValue.split(",");
                for(String item : split)
                {
                    try
                    {
                        list.add(item.trim());
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.toString());
                        e.printStackTrace();
                    }
                }
            }
            if(list.size() > 0)
                paramsMap.put(targetKey, list);
        }
    }

    public static void replaceByIntegerForQuery(Map<String, Object> paramsMap, String targetKey)
    {
        if(paramsMap.containsKey(targetKey))
        {
            String dataValue = paramsMap.get(targetKey).toString();
            paramsMap.remove(targetKey);
            try
            {
                paramsMap.put(targetKey, Integer.parseInt(dataValue));
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
                e.printStackTrace();
            }
        }
    }
    public static String getMonthlyTableName(String runtimeTableName, Date now, String timeZone)
    {
        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM");
            dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
            String targetMonth = dateFormat.format(now);
            runtimeTableName = runtimeTableName.replace("_runtime", "");
            return runtimeTableName + "_" + targetMonth;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public static String getAnnualTableName(String runtimeTableName, Date now, String timeZone)
    {
        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
            String targetMonth = dateFormat.format(now);
            runtimeTableName = runtimeTableName.replace("_runtime", "");
            return runtimeTableName + "_" + targetMonth;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public static String getSchmeNameFromJDBC(String jdbcUrl)
    {
        try
        {
            //String url = "jdbc:mysql://10.110.30.63:3306/#####?characterEncoding=UTF-8&allowPublicKeyRetrieval=true&autoReconnection=true&useSSL=false&serverTimezone=Asia/Seoul";
            String[] split1 = jdbcUrl.split("//");
            String[] split2 = split1[1].split("/");
            //String[] split3 = split2[1].split("?");
            return split2[1].substring(0, split2[1].indexOf("?"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean isValidDatabaseRequest(String req)
    {
        if( ! StringUtils.isEmpty(req))
        {
            String lowerCased = req.toLowerCase();

            List<String> keyList = new ArrayList<>();
            keyList.add(" privileges on ");
            keyList.add("create table");
            keyList.add("alter table");
            keyList.add("drop table");
            keyList.add("drop database");

            keyList.add("delete from");
            keyList.add("insert into");

            for(String key : keyList)
            {
                if(lowerCased.contains(key))
                    return false;
            }
            return !req.contains("update") || !req.contains("set");
        }
        return true;
    }

    public static boolean isValidDatabaseRequest(Map<String, Object> paramsMap)
    {
        if(paramsMap != null)
        {
            for( Map.Entry<String, Object> elem : paramsMap.entrySet() )
            {
                if(elem.getValue() != null)
                {
                    String value = elem.getValue().toString();
                    if( ! isValidDatabaseRequest(value))
                        return false;
                }
            }
        }
        return true;
    }

}