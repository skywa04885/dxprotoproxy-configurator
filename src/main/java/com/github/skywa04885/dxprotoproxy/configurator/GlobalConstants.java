package com.github.skywa04885.dxprotoproxy.configurator;

import java.util.regex.Pattern;

public class GlobalConstants {
    public static final int PORT_MIN = 0;
    public static final int PORT_MAX = 65535;

    public static Pattern MQTT_USERNAME = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9_.\\-]{2,29}$");
    public static Pattern MQTT_CLIENT_IDENTIFIER = Pattern.compile("^[a-zA-Z0-9]{1,23}$");
    public static Pattern namePattern = Pattern.compile("^[a-zA-Z0-9_]{1,64}$");
    public static Pattern httpVersionPattern = Pattern.compile("^http/([0-9]+)(\\.[0-9]+)?$", Pattern.CASE_INSENSITIVE);
    public static Pattern protocolPattern = Pattern.compile("^(http|https)$", Pattern.CASE_INSENSITIVE);
    public static Pattern HOSTNAME_PATTERN = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
            "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
    public static Pattern templatePathPattern = Pattern.compile("^((/[a-zA-Z0-9_\\-.]+)|(/\\{[a-zA-Z0-9\\-.]+\\}))+/?$");
    public static Pattern MQTT_TOPIC_PATTERN = Pattern.compile("^[a-zA-Z0-9/]+$");

    public static boolean isNameValid(String name) {
        return namePattern.matcher(name).matches();
    }

    public static boolean isHttpVersionValid(String httpVersion) {
        return httpVersionPattern.matcher(httpVersion).matches();
    }

    public static boolean isProtocolValid(String protocol) {
        return protocolPattern.matcher(protocol).matches();
    }

    public static boolean isValidHost(String host) {
        return HOSTNAME_PATTERN.matcher(host).matches();
    }

    public static boolean isValidTemplatePath(String templatePath) {
        return templatePathPattern.matcher(templatePath).matches();
    }
}
