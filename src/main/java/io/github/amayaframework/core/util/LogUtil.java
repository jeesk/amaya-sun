package io.github.amayaframework.core.util;

import io.github.amayaframework.core.contexts.HttpResponse;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class LogUtil {
    private static final String RESOURCES_PATH = "src/main/resources";
    private static final String LOGO_NAME = "logo.txt";
    private static final String ART_NAME = "art.txt";

    public static String readLogo() {
        return readResourceFile(LOGO_NAME);
    }

    public static String readArt() {
        return readResourceFile(ART_NAME);
    }

    public static String readResourceFile(String name) {
        FileInputStream stream;
        try {
            stream = new FileInputStream(RESOURCES_PATH + "/" + name);
        } catch (Exception e) {
            return "";
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines().reduce("", (left, right) -> left + right + "\n");
    }

    public static String getResponseData(HttpResponse response) {
        return "Code: " + response.getCode() + "\n" +
                "Body: " + response.getBody() + "\n" +
                "Attachments: " + response.getAttachments() + "\n" +
                "Headers: " + response.getHeaderMap().toString() + "\n" +
                "Cookies: " + response.getCookies() + "\n";
    }
}
