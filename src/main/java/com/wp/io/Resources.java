package com.wp.io;

import java.io.InputStream;

/**
 * @author Elliot0215
 */
public class Resources {

    public static InputStream getResourceAsStream(String path){
        return Resources.class.getClassLoader().getResourceAsStream(path);
    }
}
