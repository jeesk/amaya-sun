package io.github.amayaframework.core.sun;

import java.io.IOException;

import static com.github.romanqed.util.IOUtil.readResourceFile;


class IOUtil {
    private static final String ART = "art.txt";
    private static final String LOGO = "logo.txt";

    static String readLogo() throws IOException {
        return readResourceFile(LOGO);
    }

    static String readArt() throws IOException {
        return readResourceFile(ART);
    }
}
