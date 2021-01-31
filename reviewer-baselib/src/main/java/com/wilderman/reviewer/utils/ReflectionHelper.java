package com.wilderman.reviewer.utils;

import com.wilderman.reviewer.db.primary.entities.IEntity;
import org.apache.commons.lang.NullArgumentException;
import org.hibernate.collection.internal.PersistentBag;

import javax.persistence.Index;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ReflectionHelper {

    private static Map<Class, List<Method>> setters = new HashMap<>();
    private static Map<Class, List<Method>> getters = new HashMap<>();
    private static Map<Class, Method[]> declaredMethods = new HashMap<>();
    private static Map<Class, Field[]> declaredFields = new HashMap<>();


    public static Method[] getDeclaredMethods(Class clazz) {
        if (!declaredMethods.containsKey(clazz)) {
            Method[] methods = clazz.getDeclaredMethods();
            declaredMethods.put(clazz, methods);
        }

        return declaredMethods.get(clazz);
    }

    public static Field[] getDeclaredFields(Class clazz) {
        if (!declaredFields.containsKey(clazz)) {
            Field[] fields = clazz.getDeclaredFields();
            declaredFields.put(clazz, fields);
        }

        return declaredFields.get(clazz);
    }

    public static List<Method> getGetterMethods(Class clazz) {
        if (!getters.containsKey(clazz)) {
            List<Method> mLst = new ArrayList<>();
            Method[] methods = getDeclaredMethods(clazz);
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                if (isGetter(method)) {
                    mLst.add(method);
                }
            }

            getters.put(clazz, mLst);
        }

        return getters.get(clazz);
    }

    public static List<Method> getSetterMethods(Class clazz) {
        if (!setters.containsKey(clazz)) {
            List<Method> mLst = new ArrayList<>();
            Method[] methods = getDeclaredMethods(clazz);
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                if (isSetter(method)) {
                    mLst.add(method);
                }
            }
            setters.put(clazz, mLst);
        }

        return setters.get(clazz);
    }

    public static Method getGetterByField(Field field) {
        return getGetterByField(field.getName(), field.getDeclaringClass());
    }

    public static Method getGetterByField(String fieldName, Class<?> clazz) {
        fieldName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        String getterName = "get" + fieldName;
        Method getter = null;
        try {
            getter = clazz.getDeclaredMethod(getterName);
            if (!isGetter(getter)) {
                getter = null;
            }
        } catch (NoSuchMethodException e) {

        }

        return getter;
    }

    public static Method getSetterByField(String fieldName, Class type, Class clazz) {
        Method method = null;
        try {
            method = getSetterByField(clazz.getDeclaredField(fieldName), type);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
            return method;
        }
    }

    public static Method getSetterByField(Field field, Class type) {
        String fieldName = field.getName();
        fieldName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        String setterName = "set" + fieldName;
        Method setter = null;
        try {
            setter = field.getDeclaringClass().getDeclaredMethod(setterName, type);
            if (!isSetter(setter)) {
                setter = null;
            }
        } catch (NoSuchMethodException e) {

        }

        return setter;
    }

    public static Method getSetterByField(Field field) {
        return getSetterByField(field, field.getType());
    }

    public static <T extends Class> List<Field> getAnnotatedFields(Class clazz, T annotationClass) {
        List<Field> fields = new ArrayList<>();
        for (Field f : getDeclaredFields(clazz)) {
            if (f.getAnnotation(annotationClass)!= null) {
                fields.add(f);
            }
        }
        return fields;
    }

    public static Field getFieldForGetterSetter(Method method) {
        if (!(isGetter(method) || isSetter(method))) {
            return null;
        }

        String fieldName = method.getName().replace("^(set|get)", "");
        fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
        Field field = null;
        try {
            field = method.getDeclaringClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {

        }

        return field;
    }

    public static boolean isGetter(Method method) {
        if (!method.getName().startsWith("get")) {
            return false;
        }
        if (method.getParameterTypes().length != 0) {
            return false;
        }

        if (void.class.equals(method.getReturnType())) {
            return false;
        }

        return true;
    }

    public static boolean isSetter(Method method) {
        if (!method.getName().startsWith("set")) {
            return false;
        }

        if (method.getParameterTypes().length != 1) {
            return false;
        }

        return true;
    }

    public static Set<String> getEntityIndexNames(Class<? extends IEntity> clazz) {
        Set<String> indexNames = new HashSet<>();
        try {
            Index[] indexes = clazz.getAnnotation(Table.class).indexes();
            indexNames = Arrays.stream(indexes).map(index -> index.name()).collect(Collectors.toSet());
        } catch (NullPointerException e) {

        } catch (NullArgumentException e) {

        }
        return indexNames;
    }

    public static IEntity getEntityCopy(IEntity source, Class<? extends IEntity> clazz) throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        Set<String> ignoreFields = new HashSet<>();


        IEntity target = clazz.getDeclaredConstructor().newInstance();

        Method ignoreFieldNamesGetter = null;
        try {
            ignoreFieldNamesGetter = clazz.getMethod("getIgnoreFieldsOnCopy");
            List<String> temp = (List<String>) ignoreFieldNamesGetter.invoke(null);
            ignoreFields = new HashSet<>(temp);
        } catch (NoSuchMethodException e) {

        } catch (IllegalAccessException e) {

        } catch (InvocationTargetException e) {

        }

        ignoreFields.add("id");

        for (Field f : clazz.getDeclaredFields()) {
            if (ignoreFields.contains(f.getName())) {
                continue;
            }

            Method getter = ReflectionHelper.getGetterByField(f.getName(), clazz);
            Method setter = ReflectionHelper.getSetterByField(f);
            if (getter == null || setter == null) {
                continue;
            }

            Object value = getter.invoke(source);
            if (value instanceof PersistentBag) {
                continue;
            }

            if (value instanceof Optional) {
                setter.invoke(target, ((Optional) value).orElse(null));
            } else {
                setter.invoke(target, value);
            }

        }

        return target;
    }


}
