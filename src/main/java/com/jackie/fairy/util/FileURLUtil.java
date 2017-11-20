package com.jackie.fairy.util;

import java.net.URL;

/**
 * Created by jackie on 17/11/20.
 */
public class FileURLUtil {
    public static URL getFileURL(String fileName) {
        return FileURLUtil.class.getClassLoader().getResource(fileName);
    }
}
