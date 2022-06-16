package com.softwarelara.uhcmeetupsystem.utils;

import com.softwarelara.uhcmeetupsystem.UHCMeetupSystem;

public class ConfigurationUtils {

    public static String getStringOfConfigPath(String path) {
        return UHCMeetupSystem.getInstance().getConfig().getString(path).replace("&", "§");
    }

    public static Integer getIntegerOfConfigPath(String path) {
        return UHCMeetupSystem.getInstance().getConfig().getInt(path);
    }

}
