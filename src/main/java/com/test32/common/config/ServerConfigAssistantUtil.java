package com.test32.common.config;

import com.test32.common.util.AES256Util;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

public class ServerConfigAssistantUtil
{
    public final static String WINDOW_PATH_PREFIX = "C:/";
    public final static String LINUX_PATH_PREFIX = "/";

    public static boolean isWindows()
    {
        try {
            String osName = System.getProperty("os.name");
            return osName.toLowerCase().contains("windows");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean saveServerConfigInfo(String fileFullPath, String configDataByJson, String configEncodeMode, String encryptKey)
    {
        return saveFileToConfig(fileFullPath, configDataByJson, configEncodeMode, encryptKey);
    }

    public static String loadServerConfigInfo(String fileFullPath, String encryptKey)
    {
        return getServerConfigByPath(fileFullPath, encryptKey);
    }

    private static String encodeString(String input, String encryptKey)
    {
        try {
            AES256Util aes256Util = new AES256Util(encryptKey);
            return aes256Util.encrypt(input);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static String decodeString(String input, String encryptKey)
    {
        try {
            AES256Util aes256Util = new AES256Util(encryptKey);
            return aes256Util.decrypt(input);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void createPath(String path)
    {
        File file;
        String[] pathArray;
        pathArray = path.split("/");

        String rootDirectoryPath = "";
        for (int i = 0; i < pathArray.length; i++) {
            rootDirectoryPath += pathArray[i];
            file = new File(rootDirectoryPath);
            if (i < pathArray.length) {
                if (!file.isDirectory())
                    createDirectory(file.getPath());
                rootDirectoryPath += "/";
            }
        }
    }

    public static boolean createDirectory(String pathString)
    {
        Path path = FileSystems.getDefault().getPath(pathString);
        if (isWindows())
            return createDirectoryOnWindow(path);
        else
            return createDirectoryOnLinux(path);
    }

    private static boolean createDirectoryOnWindow(Path path)
    {
        try {
            Files.createDirectory(path);
            return true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static boolean createDirectoryOnLinux(Path path)
    {
        // 디렉토리 권한 설정
        Set<PosixFilePermission> permission = PosixFilePermissions.fromString("rwxr-x---");
        // 파일 속성
        FileAttribute<Set<PosixFilePermission>> attribute = PosixFilePermissions.asFileAttribute(permission);
        try {
            Files.createDirectory(path, attribute);
            return true;

        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String getServerConfigByPath(String configPath, String encryptKey)
    {
        String data = loadDataFromConfig(configPath);
        if (data == null) {
            System.out.println("### ServerConfigAssistantUtil.getServerConfigByPath() : failed");
            System.out.println("config data not exist on path : " + configPath);
            return null;
        }
        else {
            if (!StringUtils.isEmpty(data)) {
                if (data.trim().startsWith("{"))
                    return data;
                else
                    return decodeString(data, encryptKey);
            }
            else return data;
        }
    }

    public static String loadDataFromConfig(String path)
    {
        String fullPath;
        if (isWindows()) {
            fullPath = WINDOW_PATH_PREFIX;
        }
        else {
            fullPath = LINUX_PATH_PREFIX;
        }
        fullPath += path;

        return loadDataByFullSystemPath(fullPath);
    }

    public static String loadDataByFullSystemPath(String fullPath)
    {
        String content = null;
        File file = new File(fullPath);
        if (file.isFile()) {
            BufferedReader br = null;
            InputStreamReader isr = null;
            FileInputStream fis = null;

            String temp;
            try {
                content = "";

                fis = new FileInputStream(file);
                isr = new InputStreamReader(fis, "UTF-8");
                br = new BufferedReader(isr);
                while ((temp = br.readLine()) != null) {
                    content += temp;
                }

            }
            catch (FileNotFoundException e) {
                e.printStackTrace();

            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (fis != null)
                        fis.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    if (isr != null)
                        isr.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    if (br != null)
                        br.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content;
    }

    private static boolean saveFileToConfig(String path, String data, String configEncodeMode, String encryptKey)
    {
        File file;
        String[] pathArray;
        pathArray = path.split("/");

        String rootDirectoryPath = "";
        if (isWindows()) {
            rootDirectoryPath += WINDOW_PATH_PREFIX;
        }
        else {
            rootDirectoryPath += LINUX_PATH_PREFIX;
        }

        for (int i = 0; i < pathArray.length; i++) {
            rootDirectoryPath += pathArray[i];
            file = new File(rootDirectoryPath);
            if (i < pathArray.length - 1) {
                if (!file.isDirectory())
                    createDirectory(file.getPath());
                rootDirectoryPath += "/";
            }
        }
        try {
            boolean isForceEncode = false;
            if (!StringUtils.isEmpty(configEncodeMode) && configEncodeMode.equalsIgnoreCase("force")) {
                isForceEncode = true;
            }
            else if (StringUtils.isEmpty(configEncodeMode)) {
                String priorConfig = loadDataFromConfig(path);
                if (!StringUtils.isEmpty(priorConfig)) {
                    if (!priorConfig.trim().startsWith("{")) {
                        isForceEncode = true;
                    }
                }
            }

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(rootDirectoryPath));
            if (isForceEncode) {
                String cvt = encodeString(data, encryptKey);
                if (cvt != null)
                    bufferedWriter.write(cvt);
                bufferedWriter.close();
            }
            else {
                bufferedWriter.write(data);
                bufferedWriter.close();
            }

        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}