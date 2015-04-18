package de.gianasista.tldr_viewer.backend;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vera on 11.04.15.
 */
public class Command implements Comparable<Command>{

    private String name;
    private List<String> platforms = new ArrayList<>();

    public Command(String name) {
        this.name = name;
    }

    public void addPlatform(String platform) {
        platforms.add(platform);
    }

    public String getName() {
        return name;
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Command another) {
        return this.name.compareTo(another.name);
    }

    public String getPlatformString() {
        return TextUtils.join(", ", platforms);
    }
}
