package com.pojo_unit;

import org.apache.commons.lang3.RandomStringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class RandomObjectFactory
{
    private static final String JAVA_LANG = "java.lang.";
    private static final String JAVA_MATH = "java.math.";
    private static final String JAVA_UTIL = "java.util.";

    private static final String ARRAY = "Array";

    private Random random;

    public RandomObjectFactory()
    {
        random = new Random();
    }

    public Object getRandomValueForField(Field field)
    {
        String typeName = convertTypeName(field.getType().getName());

        return getRandomValueForNamedType(field, typeName);
    }

    private Object getRandomValueForNamedType(Field field, String typeName)
    {
        Object randomValue = null;

        Boolean isArray = false;

        if (typeName.contains(ARRAY))
        {
            isArray = true;
            int endIndex = typeName.length() - ARRAY.length();

            typeName = typeName.substring(0, endIndex);
        }

        Class<?> type = field.getType();

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
                randomValue = getRandomList(field);
                break;
            default:
                randomValue = instantiateType(type);
        }

        if (isArray) {
            Object arrayInstance = Array.newInstance(type.getComponentType(), 1);
            Array.set(arrayInstance, 0, randomValue);

            return arrayInstance;
        }

        return randomValue;
    }

    private Object instantiateType(Class<?> type) {
        Object randomValue;

        try {
            randomValue = type.newInstance();

        } catch (InstantiationException | IllegalAccessException e) {
            if (type.isEnum()) {
                int x = random.nextInt(type.getEnumConstants().length);
                randomValue = type.getEnumConstants()[x];
            } else {
                randomValue = null;
            }
        }

        return randomValue;
    }

    private Object getRandomList(Field field) {
        String namedType = field.getGenericType().toString().replace("java.util.List<", "").replace(">", "");

        Object content1 = getRandomValueForNamedType(field, convertTypeName(namedType));
        Object content2 = getRandomValueForNamedType(field, convertTypeName(namedType));

        return Arrays.asList(content1, content2);
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

    private String convertTypeName(String typeName)
    {
        String convertedName = typeName;

        if (convertedName.contains("[L"))
        {
            convertedName = convertedName.substring("[L".length(), convertedName.length() - 1);
            convertedName = convertedName.concat(ARRAY);
        }

        if (convertedName.contains(JAVA_LANG) || convertedName.contains(JAVA_MATH) || convertedName.contains(JAVA_UTIL))
        {
            convertedName = convertedName.substring(JAVA_LANG.length(), convertedName.length());
        }

        return convertedName;
    }
}