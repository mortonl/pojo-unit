package com.pojo_unit;

import org.apache.commons.lang3.RandomStringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.fail;

public class RandomObjectFactory {

    private Random random;

    public RandomObjectFactory() {
        random = new Random();
    }

    public static List<Field> getTestableFieldsForClass(final Class<?> clazzToTest) {
        return getTestableFieldsForClass(clazzToTest, new ArrayList<String>());
    }

    public static List<Field> getTestableFieldsForClass(final Class<?> clazzToTest, List<String> excludedFieldNames) {
        List<Field> fieldArrayList = new ArrayList<>();

        Field[] declaredFields = clazzToTest.getDeclaredFields();

        for (Field field : declaredFields) {
            if (!isFieldExcluded(excludedFieldNames, field) && isTestableField(field)) {
                fieldArrayList.add(field);
            }
        }

        return Collections.unmodifiableList(fieldArrayList);
    }

    private static boolean isTestableField(Field field) {
        return !field.isSynthetic() && !Modifier.isStatic(field.getModifiers());
    }

    private static boolean isFieldExcluded(final List<String> excludedFieldNames, final Field field) {
        return excludedFieldNames.contains(field.getName());
    }

    private Object createInnerObject(Class<?> type) {
        Object randomValue = instantiateType(type);

        List<Field> testableFieldsForClass = getTestableFieldsForClass(type);

        if (!testableFieldsForClass.isEmpty()) {
            Field innerField = testableFieldsForClass.get(0);
            innerField.setAccessible(true);

            Object randomInnerValue = getRandomValueForField(innerField);

            try {
                innerField.set(randomValue, randomInnerValue);
            } catch (IllegalAccessException e) {
                fail("Could not set field value via reflection");
            }
        }

        return randomValue;
    }

    public Object getRandomValueForField(final Field field) {
        Class<?> type = field.getType();

        boolean isCollection = Collection.class.isAssignableFrom(type);
        boolean isMap = Map.class.isAssignableFrom(type);

        if (isCollection || isMap) {
            return getRandomCollection(field);
        }

        return getRandomValueForType(type);
    }

    private Object getRandomCollection(Field field) {
        String genericTypeName = field.getGenericType().toString();

        int beginIndex = genericTypeName.indexOf("<") + 1;
        int endIndex = genericTypeName.indexOf(">");

        String namedType = genericTypeName.substring(beginIndex, endIndex);
        String collectionType = trimJavaPackageName(field.getType().getName());

        boolean isMap = namedType.contains(",");
        if (isMap) {
            return createRandomMap(namedType);
        }

        Class<?> innerType = null;

        try {
            innerType = Class.forName(namedType);
        } catch (ClassNotFoundException e) {
            fail("Could not find Class for innerType: " + namedType);
        }

        return getRandomValueForNamedCollectionType(collectionType, innerType);
    }

    private Object createRandomMap(final String namedType) {
        String[] namedTypes = namedType.split(", ");

        if (namedTypes.length > 2) {
            fail("More type arguments than expected for creating a hashMap");
        }

        Class<?> innerType1 = getClassByName(namedTypes[0]);
        Object key = getRandomValueForType(innerType1);

        Class<?> innerType2 = getClassByName(namedTypes[1]);
        Object value = getRandomValueForType(innerType2);

        Map<Object, Object> randomMap = new HashMap<>();

        randomMap.put(key, value);

        return randomMap;
    }

    private Class<?> getClassByName(final String namedType) {
        Class<?> innerType = null;

        try {
            innerType = Class.forName(namedType);
        } catch (ClassNotFoundException e) {
            fail("Could not find Class for innerType: " + namedType);
        }
        return innerType;
    }

    public Object getRandomValueForNamedCollectionType(String typeName, Class<?> innerType) {
        Object item1 = getRandomValueForType(innerType);
        Object item2 = getRandomValueForType(innerType);

        Object randomValue = null;

        switch (typeName) {
            case "List":
                randomValue = Arrays.asList(item1, item2);
                break;
            case "Set":
                randomValue = new HashSet<>(Arrays.asList(item1, item2));
                break;
            default:
                fail("Couldn't generate a randomValue for collection type: " + typeName);
        }

        return randomValue;
    }

    public Object getRandomValueForType(final Class<?> type) {
        String typeName = type.getName();

        boolean isArray = type.isArray();

        if (isArray) {
            return createRandomArrayOfType(type);
        }

        boolean isJavaType = typeName.startsWith("java.");
        boolean isPrimitive = type.isPrimitive();

        if (isJavaType) {
            typeName = trimJavaPackageName(typeName);
        }

        if (isJavaType || isPrimitive) {
            return getRandomValueForBasicNamedType(typeName);
        } else {
            return createInnerObject(type);
        }
    }

    public Object createRandomArrayOfType(final Class<?> type) {
        Class<?> componentType = type.getComponentType();

        Object firstItem = getRandomValueForType(componentType);

        Object arrayInstance = Array.newInstance(componentType, 1);
        Array.set(arrayInstance, 0, firstItem);

        return arrayInstance;
    }

    private String trimJavaPackageName(final String typeName) {
        String trimmedName = typeName;

        Pattern pattern = Pattern.compile("java\\..*\\.");
        Matcher matcher = pattern.matcher(typeName);

        if (matcher.find()) {
            trimmedName = typeName.substring(matcher.end(), typeName.length());
        }

        return trimmedName;
    }

    public Object getRandomValueForBasicNamedType(String typeName) {
        Object randomValue = null;

        switch (typeName) {
            case "Byte":
            case "byte":
                randomValue = getRandomByte();
                break;
            case "Short":
            case "short":
                randomValue = (short) random.nextInt();
                break;
            case "Integer":
            case "int":
                randomValue = random.nextInt();
                break;
            case "BigDecimal":
                randomValue = BigDecimal.valueOf(random.nextDouble());
                break;
            case "Date":
                randomValue = getRandomDate();
                break;
            case "Long":
            case "long":
                randomValue = random.nextLong();
                break;
            case "Float":
            case "float":
                randomValue = random.nextFloat();
                break;
            case "Double":
            case "double":
                randomValue = random.nextDouble();
                break;
            case "Character":
            case "char":
                randomValue = RandomStringUtils.randomAlphabetic(1).charAt(0);
                break;
            case "String":
                randomValue = RandomStringUtils.randomAlphabetic(5);
                break;
            case "Boolean":
            case "boolean":
                randomValue = random.nextBoolean();
                break;
            case "List":
                randomValue = new ArrayList<>();
                break;
            default:
                fail("Couldn't generate a randomValue for type: " + typeName);
        }

        return randomValue;
    }

    private Date getRandomDate() {
        Integer randomYear = random.nextInt(50) + 2000;
        Integer randomMonth = random.nextInt(12);
        Integer randomDay = random.nextInt(28) + 1;
        Integer randomHour = random.nextInt(24);
        Integer randomMinute = random.nextInt(60);

        Calendar calendar = Calendar.getInstance();

        calendar.set(randomYear, randomMonth, randomDay, randomHour, randomMinute);

        return calendar.getTime();
    }

    private Object getRandomByte() {
        byte[] bytes = new byte[1];
        random.nextBytes(bytes);

        return Byte.valueOf(bytes[0]);
    }

    private Object instantiateType(Class<?> type) {
        Object randomValue = null;

        if (classHasZeroArgumentPublicConstructor(type)) {
            try {
                randomValue = type.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                fail("Could not instantiate type: " + type);
            }
        } else {
            if (type.isEnum()) {
                int x = random.nextInt(type.getEnumConstants().length);
                randomValue = type.getEnumConstants()[x];
            } else {
                randomValue = createObjectInstanceUsingParametrisedConstructor(type);
            }
        }

        if (randomValue == null) {
            fail("Could not instantiate type: " + type);
        }

        return randomValue;
    }

    public boolean classHasZeroArgumentPublicConstructor(Class<?> type) {
        for (Constructor<?> constructor : type.getConstructors()) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();

            if (parameterTypes.length == 0) {
                return true;
            }
        }
        return false;
    }

    public Object createObjectInstanceUsingParametrisedConstructor(Class<?> type) {
        Constructor<?> constructor = getSmallestConstructor(type);
        List<Object> parameters = new ArrayList<>();

        for (Class<?> parameterType : constructor.getParameterTypes()) {
            Object randomValue = getRandomValueForType(parameterType);
            parameters.add(randomValue);
        }

        Object testableInstance = null;

        try {
            testableInstance = constructor.newInstance(parameters.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            fail("Could not instantiate class");
        }
        return testableInstance;
    }

    public Constructor<?> getSmallestConstructor(Class<?> type) {
        Constructor<?>[] constructors = type.getConstructors();

        if (constructors.length == 0) {
            fail("Class doesn't have any public constructors");
        }

        Constructor<?> smallestConstructor = null;
        Integer smallestParameterCount = null;

        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();

            int constructorParameters = parameterTypes.length;

            if (smallestParameterCount == null || constructorParameters < smallestParameterCount) {

                smallestParameterCount = constructorParameters;
                smallestConstructor = constructor;

                if (constructorParameters == 1) {
                    break;
                }
            }
        }

        return smallestConstructor;
    }
}