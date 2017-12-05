package org.ileler.settings.manager.sbs.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.Channel;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年12月05日 上午8:19
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class IOUtil {

    public static void close(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException var2) {
                ;
            }

        }
    }

    public static void close(Channel channel) {
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException var2) {
                ;
            }

        }
    }

    public static void close(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException var2) {
                ;
            }

        }
    }

    public static void close(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException var2) {
                ;
            }

        }
    }

    public static void close(Writer writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException var2) {
                ;
            }

        }
    }

}
