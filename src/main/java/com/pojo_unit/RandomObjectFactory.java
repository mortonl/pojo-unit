package com.pojo_unit;

import org.apache.commons.lang3.RandomStringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.junit.Assert.fail;

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

    private static boolean isTestableField(Field field) {
        return !isFieldFinal(field) && !field.isSynthetic();
    }

    private static boolean isFieldFinal(Field field) {
        return Modifier.isFinal(field.getModifiers());
    }

    public static List<Field> getTestableFieldsForClass(final Class<?> clazzToTest) {
        List<Field> fieldArrayList = new ArrayList<Field>();

        Field[] declaredFields = clazzToTest.getDeclaredFields();

        for(Field field : declaredFields)
        {
            if(isTestableField(field))
            {
                fieldArrayList.add(field);
            }
        }

        return Collections.unmodifiableList(fieldArrayList);
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
                if (!field.getName().contains(typeName))
                {
                    try {
                        type = Class.forName(typeName);
                    } catch (ClassNotFoundException e) {
                        fail("Could not find Class for type: " + typeName);
                    }
                }

                randomValue = createInnerObject(type);
        }

        if (isArray) {
            Object arrayInstance = Array.newInstance(type.getComponentType(), 1);
            Array.set(arrayInstance, 0, randomValue);

            return arrayInstance;
        }

        return randomValue;
    }

    private Object createInnerObject(Class<?> type) {
        Object randomValue = instantiateType(type);

        List<Field> testableFieldsForClass = getTestableFieldsForClass(type);

        if(!testableFieldsForClass.isEmpty()) {
            Field innerField = testableFieldsForClass.get(0);

            Object randomInnerValue = getRandomValueForField(innerField);

            String expectedSetterName = "set" + capitalize(innerField.getName());
            try {
                Method setterMethod = type.getDeclaredMethod(expectedSetterName, innerField.getType());

                setterMethod.invoke(randomValue, randomInnerValue);

            } catch (NoSuchMethodException e) {
                fail("Expected setter method: " + expectedSetterName + " was not found");
            } catch (IllegalAccessException | InvocationTargetException e) {
                fail("Could not invoke setter method for field: " + innerField.getName());
            }
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

        String convertedTypeName = convertTypeName(namedType);

        Object content1 = getRandomValueForNamedType(field, convertedTypeName);
        Object content2 = getRandomValueForNamedType(field, convertedTypeName);

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