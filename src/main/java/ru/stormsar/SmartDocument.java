package ru.stormsar;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SmartDocument {
    private static final Logger LOG = LoggerFactory.getLogger(SmartDocument.class);

    public static <T> Object getValue(Document document, String path, Class<T> clazz) {
        List<String> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(path.split("\\.")));
        return find(document, fields, clazz);
    }

    public static Object getValue(Document document, String path) {
        return getValue(document, path, Object.class);
    }

    private static <T> Object find(Object entity, List<String> fields, Class<T> clazz) {
        return find(entity, fields, -1, clazz);
    }

    private static <T> Object find(Object entity, List<String> fields, int level, Class<T> clazz) {
        level++;
        String key = "";

        if (fields.size() > level) {
            key = fields.get(level);
        }

        try {

            if (key.isEmpty()) {
                try {
                    clazz.cast(entity);
                    return entity;
                } catch (Throwable t) {
                    throw new Exception(String.format("Can't cast '%s': %s", markPathField(fields, level), t.getLocalizedMessage()));
                }
            }

            if (entity instanceof Document) {
                Object entry = ((Document) entity).get(key);
                return find(entry, fields, level, clazz);

            } else if (entity instanceof List) {

                List<Object> listEntry = new ArrayList<>();

                int index = tryCastKeyAsIndex(key);

                if (index < 0) {
                    level--;
                    int finalLevel = level;

                    AtomicBoolean isAlwaysNull = new AtomicBoolean(true);
                    ((List<?>) entity).forEach(e -> {
                        Object result = find(e, fields, finalLevel, clazz);
                        if (result != null)
                            isAlwaysNull.set(false);
                        listEntry.add(result);
                    });

                    if (isAlwaysNull.get())
                        return null;
                    else
                        return listEntry;
                } else {
                    if (((List<?>) entity).size() > index) {
                        return find(((List<?>) entity).get(index), fields, level, clazz);
                    } else
                        return null;
                }

            } else {
                if (entity == null)
                    return null;
                else
                    throw new Exception(String.format("The item '%s' not Collection or Document", markPathField(fields, level)));
            }
        } catch (Exception t) {
            LOG.warn(t.getLocalizedMessage());
            t.printStackTrace();
            return null;
        }
    }

    private static int tryCastKeyAsIndex(String key) {
        int index = -1;

        try {
            index = Integer.parseInt(key);
        } catch (Throwable ignored) {

        }

        return index;
    }

    private static String markPathField(List<String> fields, int level) {
        StringBuilder markedPath = new StringBuilder();
        if (level >= fields.size())
            level = fields.size() - 1;
        fields.add(level, "<" + fields.get(level) + ">");
        fields.remove(level + 1);
        fields.forEach(f -> markedPath.append(".").append(f));
        return markedPath.toString().replaceFirst("\\.", "");
    }
}
