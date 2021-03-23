package com.test32.common.util;

import com.test32.common.model.CustomHttpResponse;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;

public class CustomHttpClientUtil
{
    private static Integer SERVER_CONNECT_TIMEOUT = 30000;
    private static Integer SERVER_REQUEST_TIMEOUT = 30000;

    public static CustomHttpResponse httpGetRequest(String requestUrl, String requestParam)
    {
        return httpRequest(requestUrl, requestParam, "GET", SERVER_CONNECT_TIMEOUT, SERVER_REQUEST_TIMEOUT);
    }

    public static CustomHttpResponse httpPostRequest(String requestUrl, String requestParam, int connectTimeout, int readTimeout)
    {
        return httpRequest(requestUrl, requestParam, "POST", connectTimeout, readTimeout);
    }

    public static CustomHttpResponse httpPostRequest(String requestUrl, String requestParam)
    {
        return httpRequest(requestUrl, requestParam, "POST", SERVER_CONNECT_TIMEOUT, SERVER_REQUEST_TIMEOUT);
    }

    public static CustomHttpResponse httpPostRequest(String requestUrl, String requestParam, int readTimeout)
    {
        return httpRequest(requestUrl, requestParam, "POST", SERVER_CONNECT_TIMEOUT, readTimeout);
    }

     private static CustomHttpResponse httpRequest(String requestUrl, String requestParam, String requestMethod, int connTimeout, int readTimeout)
    {
        CustomHttpResponse result = new CustomHttpResponse();
        result.setRequestUrl(requestUrl);
        HttpURLConnection conn = null;

        String baseResponse = null;
        Integer responseCode = null;
        String errorMessage = null;
        try
        {
            URL url = new URL(requestUrl);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", "" + Integer.toString(requestParam.getBytes().length));
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setReadTimeout(readTimeout);
            conn.setConnectTimeout(connTimeout);

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
            writer.write(requestParam);
            writer.close();
            wr.close();

            InputStream is;

            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
            }
            else {
                is = conn.getErrorStream();
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            responseCode = conn.getResponseCode();
            baseResponse = response.toString();

            if (conn.getResponseCode() != 200)
            {
                System.out.println("## httpResponseCode error : " + conn.getResponseCode() + "  response : " + baseResponse);
            }
        }
        catch (Exception e)
        {
            System.out.println("## http Exception toString: " + e.toString());
            System.out.println("## http Exception getMessage: " + e.getMessage());
            //errorMessage = e.toString();
            errorMessage = e.getMessage();
        }
        finally
        {
            if (conn != null)
            {
                conn.disconnect();
            }
        }

        result.setResponseCode(responseCode);
        result.setBaseResponse(baseResponse);
        result.setErrorMessage(errorMessage);
        return result;
    }

    public static CustomHttpResponse httpsGetRequest(String requestUrl)
    {
        CustomHttpResponse result = new CustomHttpResponse();
        result.setRequestUrl(requestUrl);
        HttpsURLConnection conn = null;

        String baseResponse = null;
        Integer responseCode = null;
        String errorMessage = null;
        try
        {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager()
                    {
                        public X509Certificate[] getAcceptedIssuers()
                        {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType)
                        {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType)
                        {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            URL url = new URL(requestUrl);
            conn = (HttpsURLConnection) url.openConnection();

            conn.setUseCaches(false);
//            conn.setDoInput(true);
//            conn.setDoOutput(true);

            conn.setRequestMethod("GET");

            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setRequestProperty("User-Agent", "");
            conn.setRequestProperty("Content-Type", "application/xml");
            conn.setRequestProperty("Content-Length", "" + Integer.toString("".getBytes().length));

            conn.setReadTimeout(SERVER_REQUEST_TIMEOUT);
            conn.setConnectTimeout(SERVER_CONNECT_TIMEOUT);

            InputStream is;
            if (conn.getResponseCode() == 200)
                is = conn.getInputStream();
            else
                is = conn.getErrorStream();
            if (is == null)
                is = conn.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null)
            {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            responseCode = conn.getResponseCode();
            baseResponse = response.toString();
        }
        catch (Exception e)
        {
            System.out.println("## http Exception toString: " + e.toString());
            System.out.println("## http Exception getMessage: " + e.getMessage());
            //errorMessage = e.toString();
            errorMessage = e.getMessage();
        }
        finally
        {
            if (conn != null)
            {
                conn.disconnect();
            }
        }
        result.setResponseCode(responseCode);
        result.setBaseResponse(baseResponse);
        result.setErrorMessage(errorMessage);
        return result;
    }

    public static CustomHttpResponse httpsPostRequest(String requestUrl, String requestParam)
    {
        CustomHttpResponse result = new CustomHttpResponse();
        result.setRequestUrl(requestUrl);
        HttpsURLConnection conn = null;

        String baseResponse = null;
        Integer responseCode = null;
        String errorMessage = null;
        try
        {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager()
                    {
                        public X509Certificate[] getAcceptedIssuers()
                        {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType)
                        {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType)
                        {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            URL url = new URL(requestUrl);
            conn = (HttpsURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");

            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setRequestProperty("User-Agent", "");
            conn.setRequestProperty("Content-Type", "application/xml");
            conn.setRequestProperty("Content-Length", "" + Integer.toString(requestParam.getBytes().length));

            conn.setReadTimeout(SERVER_REQUEST_TIMEOUT);
            conn.setConnectTimeout(SERVER_CONNECT_TIMEOUT);

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
            writer.write(requestParam);
            writer.close();
            wr.close();

            InputStream is;
            if (conn.getResponseCode() == 200)
                is = conn.getInputStream();
            else
                is = conn.getErrorStream();
            if (is == null)
                is = conn.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null)
            {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            responseCode = conn.getResponseCode();
            baseResponse = response.toString();
        }
        catch (Exception e)
        {
            System.out.println("## http Exception toString: " + e.toString());
            System.out.println("## http Exception getMessage: " + e.getMessage());
            //errorMessage = e.toString();
            errorMessage = e.getMessage();
        }
        finally
        {
            if (conn != null)
            {
                conn.disconnect();
            }
        }

        result.setResponseCode(responseCode);
        result.setBaseResponse(baseResponse);
        result.setErrorMessage(errorMessage);
        return result;
    }
}