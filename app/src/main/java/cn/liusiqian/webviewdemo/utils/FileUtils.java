package cn.liusiqian.webviewdemo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;

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

    public static File parseImageFile(Response response, File directory, String fileName) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();

            long sum = 0;

            if (directory != null && !directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(directory, fileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
            }
            fos.flush();

            return file;

        } finally {
            response.body().close();
            if (is != null) {
                is.close();
            }
            if (fos != null) {
                fos.close();
            }

        }
    }
}
