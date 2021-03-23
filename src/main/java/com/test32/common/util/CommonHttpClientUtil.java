package com.test32.common.util;

import com.linkFlow.manager.admin.ServiceException;
import com.linkFlow.manager.common.model.ReturnCode;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;

public class CommonHttpClientUtil
{
    private static Integer SERVER_CONNECT_TIMEOUT = 30000;
    private static Integer SERVER_REQUEST_TIMEOUT = 30000;

    public static String httpPostRequest(String requestUrl, String requestParam, int connectTimeout, int readTimeout)
    {
        return httpRequest(requestUrl, requestParam, "POST", connectTimeout, readTimeout);
    }

    public static String httpPostRequest(String requestUrl, String requestParam)
    {
        return httpRequest(requestUrl, requestParam, "POST", SERVER_CONNECT_TIMEOUT, SERVER_REQUEST_TIMEOUT);
    }

    public static String httpPostRequest(String requestUrl, String requestParam, int readTimeout)
    {
        return httpRequest(requestUrl, requestParam, "POST", SERVER_CONNECT_TIMEOUT, readTimeout);
    }

     private static String httpRequest(String requestUrl, String requestParam, String requestMethod, int connectionTimeout, int readTimeout)
    {
        String result = null;
        HttpURLConnection connection = null;

        try
        {
            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(requestMethod);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(requestParam.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setReadTimeout(readTimeout);
            connection.setConnectTimeout(connectionTimeout);

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
            writer.write(requestParam);
            writer.close();
            wr.close();

            InputStream is;

            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
            }
            else {
                is = connection.getErrorStream();
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            result = response.toString();

            if (connection.getResponseCode() != 200)
            {
                System.out.println("## httpResponseCode error : " + connection.getResponseCode() + "  response : " + result);
            }
        }
        catch (Exception e)
        {
            System.out.println("## http Exception toString: " + e.toString());
            System.out.println("## http Exception getMessage: " + e.getMessage());
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
        }

        return result;
    }

    public static String postJson(String requestUrl, String requestParam)
    {
        String result = null;
        HttpURLConnection connection = null;

        try
        {
            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(requestParam.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setReadTimeout(SERVER_REQUEST_TIMEOUT);
            connection.setConnectTimeout(SERVER_CONNECT_TIMEOUT);

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
            writer.write(requestParam);
            writer.close();
            wr.close();

            InputStream is;

            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
            }
            else {
                is = connection.getErrorStream();
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            result = response.toString();

            if (connection.getResponseCode() != 200)
            {
                System.out.println("## httpResponseCode error : " + connection.getResponseCode() + "  response : " + result);
            }
        }
        catch (Exception e)
        {
            System.out.println("## http Exception toString: " + e.toString());
            System.out.println("## http Exception getMessage: " + e.getMessage());
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
        }
        return result;
    }

    public static String eosRequest(String requestUrl, String requestParam)
    {
        return postJson(requestUrl, requestParam);
    }

    public static String httpsGetRequest(String requestUrl)
    {
        String result;
        HttpsURLConnection connection = null;

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
            connection = (HttpsURLConnection) url.openConnection();

            connection.setUseCaches(false);
//            connection.setDoInput(true);
//            connection.setDoOutput(true);

            connection.setRequestMethod("GET");

            connection.setSSLSocketFactory(sc.getSocketFactory());
            connection.setRequestProperty("User-Agent", "");
            connection.setRequestProperty("Content-Type", "application/xml");
            connection.setRequestProperty("Content-Length", "" + Integer.toString("".getBytes().length));

            connection.setReadTimeout(SERVER_REQUEST_TIMEOUT);
            connection.setConnectTimeout(SERVER_CONNECT_TIMEOUT);

            InputStream is;
            if (connection.getResponseCode() == 200)
                is = connection.getInputStream();
            else
                is = connection.getErrorStream();
            if (is == null)
                is = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null)
            {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            result = response.toString();
        }
        catch (ServiceException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ServiceException(ReturnCode.HTTP_TROUBLE, e.getMessage());
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
        }

        return result;
    }

    public static String httpsPostRequest(String requestUrl, String requestParam)
    {
        String result;
        HttpsURLConnection connection = null;

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
            connection = (HttpsURLConnection) url.openConnection();

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.setRequestMethod("POST");

            connection.setSSLSocketFactory(sc.getSocketFactory());
            connection.setRequestProperty("User-Agent", "");
            connection.setRequestProperty("Content-Type", "application/xml");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(requestParam.getBytes().length));

            connection.setReadTimeout(SERVER_REQUEST_TIMEOUT);
            connection.setConnectTimeout(SERVER_CONNECT_TIMEOUT);

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
            writer.write(requestParam);
            writer.close();
            wr.close();

            InputStream is;
            if (connection.getResponseCode() == 200)
                is = connection.getInputStream();
            else
                is = connection.getErrorStream();
            if (is == null)
                is = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null)
            {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            result = response.toString();
        }
        catch (ServiceException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ServiceException(ReturnCode.HTTP_TROUBLE, e.getMessage());
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
        }

        return result;
    }
}