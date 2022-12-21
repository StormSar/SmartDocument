package ru.stormsar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Path {
    private int level = 0;
    private String key = "";

    private List<String> fields = new ArrayList<>();

    private boolean isReversed = false;

    public Path(List<String> fields, boolean isReversed) {
        this.fields.addAll(fields);
        setReverseAndLevel(isReversed);
    }

    public Path(String path, boolean isReversed) {
        fields = new ArrayList<>(Arrays.asList(path.split("\\.")));
        setReverseAndLevel(isReversed);
    }

    public Path(String path) {
        fields = new ArrayList<>(Arrays.asList(path.split("\\.")));
        setReverseAndLevel(false);
    }

    private void setReverseAndLevel(boolean isReversed) {
        this.isReversed = isReversed;
        if (isReversed)
            setLevelToEnd();
        else
            setLevelToStart();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;

        if (level < fields.size() && level >= 0) {
            this.key = fields.get(level);
        } else
            this.key = "";
    }

    public void incrementLevel() {
        level = isReversed ? --level : ++level;
        setLevel(level);
    }

    public void decrement() {
        level = isReversed ? ++level : --level;
        setLevel(level);
    }

    public boolean isReversed() {
        return isReversed;
    }

    public void setReversed(boolean reversed) {
        isReversed = reversed;
    }

    public String getKey() {
        return key;
    }


    public String markPathField() {
        StringBuilder markedPath = new StringBuilder();

        int index = level;
        index = Math.max(index, 0);
        index = index < fields.size() ? index : fields.size() - 1;

        for (int i = 0; i < fields.size(); i++) {
            String fild = fields.get(i);
            if (index == i)
                fild = "<" + fild + ">";
            markedPath.append(".").append(fild);
        }
        return markedPath.toString().replaceFirst("\\.", "");
    }

    public Path discardPath(boolean isReversed) {
        this.isReversed = isReversed;
        List<String> part = new ArrayList<>();

        for (int i = 0; i < fields.size(); i++) {
            if (i >= level)
                part.add(fields.get(i));
        }

        Path newPath;
        if (isReversed) {
            newPath = new Path(part, true);
        } else {
            newPath = new Path(part, false);
        }

        return newPath;
    }

    public int tryCastKeyAsIndex() {
        int index = -1;

        try {
            index = Integer.parseInt(key);
        } catch (Throwable ignored) {

        }

        return index;
    }

    public void setLevelToStart() {
        setLevel(0);
    }

    public void setLevelToEnd() {
        setLevel(fields.size() - 1);
    }
}
