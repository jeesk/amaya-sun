package io.github.amayaframework.core.sun;

import java.io.IOException;

import static io.github.amayaframework.core.util.IOUtil.readResourceFile;

public class IOUtil {
    private static final String ART = "art.txt";
    private static final String LOGO = "logo.txt";

    public static String readLogo() throws IOException {
        return readResourceFile(LOGO);
    }

    public static String readArt() throws IOException {
        return readResourceFile(ART);
    }
}
