package jp.darts;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 *
 * Copyright https://wtfu.site Inc. All Rights Reserved.
 *
 * @date 2025/4/16
 *                          @since 1.0
 *                          @author 12302
 *
 */
public class DoubleArrayNative {

    // 加载动态库
    static {
        try (InputStream inputStream = DoubleArrayNative.class.getResourceAsStream("native/Mac/x86_64/libdarts-java.dylib")){
            if (inputStream == null) { throw new RuntimeException("Native library not found in JAR");}
            Path tempFile = Files.createTempFile("lib", ".dylib");
            tempFile.toFile().deleteOnExit();
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            boolean b = tempFile.toFile().setExecutable(true);
            System.load(tempFile.toString());
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public static class Match {
        public int value;
        public int length;

        @Override
        public String toString() {
            return "Match{value=" + value + ", length=" + length + "}";
        }
    }

    private long handle;
    public DoubleArrayNative(){ handle = -1;}

    // 声明本地方法
    public native void createInstance();
    public native void destroyInstance();

    public native void setArray(byte[] array, int size);
    public native byte[] getArray();
    public native int size();

    public native int build(int numKeys, String[] keys) throws DoubleArrayException; // 构建 DoubleArray

    public int open(byte[] fileName){ return open(fileName, 0, 0); }
    public native int open(byte[] fileName, int offset, int size);

    public int save(byte[] fileName){ return save(fileName, 0); }
    public native int save(byte[] fileName, int offset);

    public int exactMatchSearch(String key){ return exactMatchSearch(key, 0, 0); }
    public native int exactMatchSearch(String key, int length, int nodePos); // 精确匹配搜索

    public Match[] commonPrefixSearch(String key, int maxResults){ return commonPrefixSearch(key, maxResults, 0, 0); }
    public native Match[] commonPrefixSearch(String key, int maxResults, int length, int nodePos); // 公共前缀搜索

}
