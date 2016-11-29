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
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PojoTest {
    private static final String JAVA_LANG = "java.lang.";
    private static final String JAVA_MATH = "java.math.";
    private static final String JAVA_UTIL = "java.util.";

    private final Class<?> clazzToTest;
    private final List<Field> fields;

    public PojoTest(final Class<?> clazzToTest) {
        this.clazzToTest = clazzToTest;

        List<Field> fieldArrayList = new ArrayList<Field>();

        Field[] declaredFields = clazzToTest.getDeclaredFields();

        for(Field field : declaredFields)
        {
            if(!Modifier.isFinal(field.getModifiers()))
            {
                fieldArrayList.add(field);
            }
        }

        this.fields = Collections.unmodifiableList(fieldArrayList);
    }

    public void testGettersAndSetters(List<String> excludedFields) {
        Object testableInstance = getTestableInstance();

        for (Field field : fields) {
            if (excludedFields.contains(field.getName())) {
                break;
            }

            Method getterMethod = getGetterMethod(field);

            Method setterMethod = getSetterMethod(field);

            Object randomValue = getRandomValueForField(field);

            try {
                setterMethod.invoke(testableInstance, randomValue);

                Object result = getterMethod.invoke(testableInstance);

                assertEquals(randomValue, result);
            } catch (IllegalAccessException | InvocationTargetException e) {
                fail("Could not invoke setter method for field: " + field.getName());
            }
        }
    }

    //Refactor into a setToRandomValues which returns a key value thing

    public void testToString(List<String> excludedFields) {
        List<String> stringValues = new ArrayList<>();

        Object testableInstance = getTestableInstance();

        for (Field field : fields) {
            if (excludedFields.contains(field.getName())) {
                break;
            }

            Method setterMethod = getSetterMethod(field);

            Object randomValue = getRandomValueForField(field);
            stringValues.add(field.getName());

            String toString = randomValue.toString();

            if(toString.contains("[L"))
            {
                toString = Arrays.toString((Object[]) randomValue).replaceAll("\\[", "").replaceAll("\\]","");
            }
            stringValues.add(toString);

            try {
                setterMethod.invoke(testableInstance, randomValue);
            } catch (IllegalAccessException | InvocationTargetException e) {
                fail("Could not invoke setter method for field: " + field.getName());
            }
        }

        String toStringResult = null;

        try {
            Method toStringMethod = clazzToTest.getDeclaredMethod("toString");
            toStringResult = (String) toStringMethod.invoke(testableInstance);
        } catch (NoSuchMethodException e) {
            fail("Expected method: toString was not found");
        } catch (InvocationTargetException | IllegalAccessException e) {
            fail("Could not invoke method: toString");
        }

        for (String expectedToStringItem : stringValues) {
            if (!toStringResult.contains(expectedToStringItem)) {
                fail("The objects toString did not contain the expected item: " + expectedToStringItem);
            }
        }
    }

    public void testEqualsAndHashCode(List<String> excludedFields) {
        Object baseInstance = getTestableInstance();
        Object equalsInstance = getTestableInstance();
        Object differentInstance = getTestableInstance();

        Method equalsMethod = getEqualsMethod();
        Method hashCodeMethod = getHashCodeMethod();

        Boolean initialEqualsResult = null;
        try {
            initialEqualsResult = (Boolean) equalsMethod.invoke(baseInstance, equalsInstance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("Could not invoke equals method");
        }

        if (initialEqualsResult != true) {
            fail("Empty objects should have been equal but were not");
        }

        Integer initialHashCodeResult1 = null;
        Integer initialHashCodeResult2 = null;

        try {
            initialHashCodeResult1 = (Integer) hashCodeMethod.invoke(baseInstance);
            initialHashCodeResult2 = (Integer) hashCodeMethod.invoke(equalsInstance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("Could not invoke hashCode method");
        }

        if (!initialHashCodeResult2.equals(initialHashCodeResult1)) {
            fail("Empty objects should have had the same hashCode but did not");
        }

        for (Field field : fields) {
            if (excludedFields.contains(field.getName())) {
                break;
            }

            Method setterMethod = getSetterMethod(field);

            Object randomValue = getRandomValueForField(field);
            Object differentRandomValue = getRandomValueForField(field);

            Integer attemptNumber = 0;
            while(differentRandomValue.equals(randomValue))
            {
                if(attemptNumber > 5)
                {
                    fail("Could not generate two different random values for field: " + field.getName());
                }
                differentRandomValue = getRandomValueForField(field);
                attemptNumber++;
            }

            try {
                setterMethod.invoke(baseInstance, randomValue);
                setterMethod.invoke(equalsInstance, randomValue);
                setterMethod.invoke(differentInstance, differentRandomValue);

            } catch (IllegalAccessException | InvocationTargetException e) {
                fail("Could not invoke setter method for field: " + field.getName());
            }

            Boolean expectedEqualResult = null;
            Boolean expectedDifferentResult = null;
            try {
                expectedEqualResult = (Boolean) equalsMethod.invoke(baseInstance, equalsInstance);
                expectedDifferentResult = (Boolean) equalsMethod.invoke(baseInstance, differentInstance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                fail("Could not invoke equals method");
            }

            if (expectedEqualResult != true) {
                fail("Changing the value of field: " + field.getName() + " resulted in the test objects not being equal but should have been");
            }

            if (expectedDifferentResult != false) {
                fail("Changing the value of field: " + field.getName() + " resulted in the test objects being equal but should have been different");
            }

            Integer hashCodeResult1 = null;
            Integer hashCodeResult2 = null;
            Integer hashCodeResult3 = null;
            try {
                hashCodeResult1 = (Integer) hashCodeMethod.invoke(baseInstance);
                hashCodeResult2 = (Integer) hashCodeMethod.invoke(equalsInstance);
                hashCodeResult3 = (Integer) hashCodeMethod.invoke(differentInstance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                fail("Could not invoke hashCode method");
            }

            if (!hashCodeResult2.equals(hashCodeResult1)) {
                fail("Changing the value of field: " + field.getName() + " resulted in the test objects hashCode not being equal but should have been");
            }

            if (hashCodeResult3.equals(hashCodeResult1)) {
                fail("Changing the value of field: " + field.getName() + " resulted in the test objects hashCode being equal but should not have been");
            }

            //Set the value back to match so each test is not polluted by previous fields results
            try {
                setterMethod.invoke(differentInstance, randomValue);

            } catch (IllegalAccessException | InvocationTargetException e) {
                fail("Could not invoke setter method for field: " + field.getName());
            }
        }
    }

    private Method getHashCodeMethod() {
        Method hashCodeMethod = null;
        try {
            hashCodeMethod = clazzToTest.getDeclaredMethod("hashCode");
        } catch (NoSuchMethodException e) {
            fail("Expected method: hashCode was not found");
        }
        return hashCodeMethod;
    }

    private Method getEqualsMethod() {
        Method equalsMethod = null;
        try {
            equalsMethod = clazzToTest.getDeclaredMethod("equals", Object.class);
        } catch (NoSuchMethodException e) {
            fail("Expected method: equals was not found");
        }
        return equalsMethod;
    }

    private Object getTestableInstance() {
        Object testableInstance = null;

        try {
            testableInstance = clazzToTest.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            fail("Could not instantiate class");
        }
        return testableInstance;
    }

    private Method getGetterMethod(Field field) {
        String expectedGetterName = getExpectedGetterName(field);

        try {
            return clazzToTest.getDeclaredMethod(expectedGetterName);
        } catch (NoSuchMethodException e) {
            fail("Expected getter method: " + expectedGetterName + " was not found");
            return null;
        }
    }

    private Method getSetterMethod(Field field) {
        String expectedSetterName = getExpectedSetterName(field);

        try {
            return clazzToTest.getDeclaredMethod(expectedSetterName, field.getType());
        } catch (NoSuchMethodException e) {
            fail("Expected setter method: " + expectedSetterName + " was not found");
            return null;
        }
    }

    private Object getRandomValueForField(Field field) {
        String typeName = convertTypeName(field.getType().getName());

        return getRandomValueForNamedType(field, typeName);
    }

    private Object getRandomValueForNamedType(Field field, String typeName) {
        Object randomValue = null;

        Random random = new Random();
        Boolean isArray = false;

        if(typeName.contains("Array"))
        {
            isArray = true;
            int endIndex = typeName.length() - "Array".length();

            typeName = typeName.substring(0, endIndex);
        }

        switch (typeName) {
            case "Byte":
            case "byte":
                byte[] bytes = new byte[1];
                random.nextBytes(bytes);
                randomValue = Byte.valueOf(bytes[0]);
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
                String namedType = field.getGenericType().toString().replace("java.util.List<", "").replace(">", "");

                Object randomListContent = getRandomValueForNamedType(field, convertTypeName(namedType));
                randomValue = Arrays.asList(randomListContent);
                break;
            default:
                try {
                    randomValue = field.getType().newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
        }

        if(isArray)
        {
            Object arrayInstance = Array.newInstance(field.getType().getComponentType(), 1);
            Array.set(arrayInstance, 0, randomValue);

            return arrayInstance;
        }

        return randomValue;
    }

    private String convertTypeName(String typeName) {
        if (typeName.contains("[L"))
        {
            typeName = typeName.substring("[L".length(),typeName.length() - 1);
            typeName = typeName.concat("Array");
        }

        if (typeName.contains(JAVA_LANG)) {
            typeName = typeName.substring(JAVA_LANG.length(), typeName.length());
        }
        else if (typeName.contains(JAVA_MATH)) {
            typeName = typeName.substring(JAVA_MATH.length(), typeName.length());
        }
        else if (typeName.contains(JAVA_UTIL)) {
            typeName = typeName.substring(JAVA_UTIL.length(), typeName.length());
        }
        return typeName;
    }

    private String getExpectedGetterName(Field field) {
        String expectedGetterName;

        if (field.getType() == boolean.class || field.getType() == Boolean.class) {
            expectedGetterName = "is" + capitalize(field.getName());
        } else {
            expectedGetterName = "get" + capitalize(field.getName());
        }

        return expectedGetterName;
    }

    private String getExpectedSetterName(Field field) {
        return "set" + capitalize(field.getName());
    }
}
