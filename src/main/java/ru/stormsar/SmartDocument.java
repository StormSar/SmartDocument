package ru.stormsar;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.bson.Document;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class SmartDocument {
    private static final Logger LOG = LoggerFactory.getLogger(SmartDocument.class);

    public static Document remove(Document document, String path) {
        pathFinder(document, new Path(path), null, Action.REMOVE);
        return document;
    }

    public static Document put(Document document, String path, Object value) {
        pathFinder(document, new Path(path), value, Action.PUT);
        return document;
    }

    public static <T> Optional<T> getOptional(Document document, String path, Class<T> clazz) {
        return Optional.ofNullable(get(document, path, clazz));
    }

    public static <T> T get(Document document, String path, Class<T> clazz) {
        Object fined = pathFinder(document, new Path(path), null, Action.GET);
        try {
            return clazz.cast(fined);
        } catch (Throwable t) {
            LOG.warn("Cast Error: " + t.getLocalizedMessage());
        }
        return null;
    }

    public static Optional<Object> getOptional(Document document, String path) {
        return Optional.ofNullable(get(document, path));
    }

    public static Object get(Document document, String path) {
        return pathFinder(document, new Path(path), null, Action.GET);
    }

    private static Object createValueWithPath(Path path, Object value) {
        String key = path.getKey();
        path.incrementLevel();

        int index = path.tryCastKeyAsIndex();

        if (index >= 0 || key.equals("[]")) {
            List<Object> newList = new ArrayList<>();
            newList.add(value);
            return createValueWithPath(path, newList);
        } else {
            if (key.isEmpty())
                return value;
            Document newDoc = new Document(key, value);
            return createValueWithPath(path, newDoc);
        }

    }

    private static Object pathFinder(Object entity, Path path, Object value, Action action) {
        if (entity instanceof Document) {
            return forDocument((Document) entity, path, value, action);
        } else if (entity instanceof List) {
            return forList((List) entity, path, value, action);
        } else
            return forValue(entity, path, value, action);
    }

    private enum Action {
        GET,
        PUT,
        REMOVE
    }

    private static Object forDocument(Document entity, Path path, Object value, Action action) {
        String key = path.getKey();
        path.incrementLevel();
        Object entry = entity.get(key);

        switch (action) {
            case GET:
                if (key.isEmpty())
                    return entity;
                return pathFinder(entry, path, value, action);
            case PUT:

                if (entry == null) {
                    return entity.append(key, createValueWithPath(path.discardPath(true), value));
                } else {
                    if (path.getKey().isEmpty())
                        return entity.append(key, value);
                    else {
                        pathFinder(entry, path, value, action);
                        return null;
                    }
                }
            case REMOVE:
                if (path.getKey().isEmpty())
                    return entity.remove(key);
                else
                    return pathFinder(entry, path, value, action);
        }

        return null;
    }

    private static Object forValue(Object entity, Path path, Object value, Action action) {
        String key = path.getKey();
        path.incrementLevel();


        switch (action) {
            case GET:
                if (!key.isEmpty())
                    return null;
                return entity;
            case PUT:
                if (path.getKey().isEmpty()) {
                    return value;
                } else {
                    createValueWithPath(path.discardPath(true), value);
                }
                break;
            case REMOVE:
                if (path.getKey().isEmpty())
                    return null;
                else
                    return entity;
        }
        return null;
    }

    private static Object forList(List<Object> entity, Path path, Object value, Action action) {
        String key = path.getKey();
        int index = path.tryCastKeyAsIndex();
        path.incrementLevel();

        switch (action) {
            case GET:

                if (key.isEmpty())
                    return entity;
                if (index >= 0) {
                    if (index < entity.size()) {
                        return pathFinder(entity.get(index), path, value, action);
                    } else {
                        return null;
                    }
                } else {
                    path.decrement();

                    AtomicBoolean isAlwaysNull = new AtomicBoolean(true);
                    List<Object> entry = new ArrayList<>();
                    entity.forEach(e -> {
                        Object element = pathFinder(e, path.discardPath(false), value, action);
                        if (element != null)
                            isAlwaysNull.set(false);
                        entry.add(element);
                    });

                    if (isAlwaysNull.get()) {
                        return null;
                    } else {
                        return entry;
                    }
                }

            case PUT:

                if (key.isEmpty()) {
                    return value;
                }
                if (key.equals("[]")) {
                    entity.forEach(e -> pathFinder(e, path.discardPath(false), value, action));
                    return null;
                }
                if (index >= 0 && index < entity.size()) {
                    return pathFinder(entity.get(index), path, value, action);
                } else {
                    if (index < 0)
                        path.decrement();
                    entity.add(createValueWithPath(path.discardPath(true), value));
                    return null;
                }

            case REMOVE:
                if (index >= 0 && index < entity.size()) {
                    if (path.getKey().isEmpty())
                        entity.remove(index);
                    else {
                        pathFinder(entity.get(index), path.discardPath(false), value, action);
                    }
                    return entity;
                } else {
                    if (index < 0)
                        path.decrement();
                    entity.forEach(e -> pathFinder(e, path.discardPath(false), value, action));
                }
        }
        return null;
    }
}
