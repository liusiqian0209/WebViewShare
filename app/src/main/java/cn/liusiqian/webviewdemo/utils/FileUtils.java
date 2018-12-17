package cn.liusiqian.webviewdemo.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liusiqian on 2018/12/17.
 */
public class FileUtils {
    /**
     * 获取图片类型
     *
     * @param filePath
     * @return
     */
    public static String getFileType(String filePath) throws IOException
    {
        Map<String, String> mFileTypes = new HashMap<>();
        mFileTypes.put("FFD8FF", "jpg");
        mFileTypes.put("89504E", "png");
        mFileTypes.put("474946", "gif");
        mFileTypes.put("49492A", "tif");
        mFileTypes.put("424D36", "bmp");
        return mFileTypes.get(getFileHeader(filePath));
    }

    /**
     * 获取文件头信息
     *
     * @param filePath
     * @return
     */
    private static String getFileHeader(String filePath) throws IOException {
        FileInputStream is = new FileInputStream(filePath);
        byte[] b = new byte[3];
        is.read(b, 0, b.length);
        String value = bytesToHexString(b);
        is.close();
        return value;
    }

    /**
     * 将byte字节转换为十六进制字符串
     *
     * @param src
     * @return
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }
}
