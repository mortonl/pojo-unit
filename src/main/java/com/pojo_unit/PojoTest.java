package com.pojo_unit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PojoTest {

    private final Class<?> clazzToTest;
    private final List<Field> fields;
    private final RandomObjectFactory randomObjectFactory = new RandomObjectFactory();

    public PojoTest(final Class<?> clazzToTest) {
        this.clazzToTest = clazzToTest;

        this.fields = randomObjectFactory.getTestableFieldsForClass(clazzToTest);
    }

    public void testGettersAndSetters(List<String> excludedFields) {
        Object testableInstance = getTestableInstance();

        for (Field field : fields) {
            if (excludedFields.contains(field.getName())) {
                break;
            }

            Method getterMethod = getGetterMethod(field);
            Method setterMethod = getSetterMethod(field);

            Object randomValue = randomObjectFactory.getRandomValueForField(field);

            try {
                setterMethod.invoke(testableInstance, randomValue);

                Object result = getterMethod.invoke(testableInstance);

                assertEquals(randomValue, result);
            } catch (IllegalAccessException | InvocationTargetException e) {
                fail("Could not invoke setter method for field: " + field.getName());
            }
        }
    }

    //Refactor into a setToRandomValues which returns a list of key value pairs

    public void testToString(List<String> excludedFields) {
        List<String> stringValues = new ArrayList<>();

        Object testableInstance = getTestableInstance();

        for (Field field : fields) {
            if (excludedFields.contains(field.getName())) {
                break;
            }

            Method setterMethod = getSetterMethod(field);

            Object randomValue = randomObjectFactory.getRandomValueForField(field);
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

        checkEmptyObjectsAreEqual(baseInstance, equalsInstance, equalsMethod);
        checkEmptyObjectsHaveSameHashCode(baseInstance, equalsInstance, hashCodeMethod);

        for (Field field : fields) {
            if (excludedFields.contains(field.getName())) {
                break;
            }

            Method setterMethod = getSetterMethod(field);

            Object randomValue = randomObjectFactory.getRandomValueForField(field);
            Object differentRandomValue = randomObjectFactory.getRandomValueForField(field);

            Integer attemptNumber = 0;
            while(differentRandomValue.equals(randomValue))
            {
                if(attemptNumber > 5)
                {
                    fail("Could not generate two different random values for field: " + field.getName());
                }
                differentRandomValue = randomObjectFactory.getRandomValueForField(field);
                attemptNumber++;
            }

            try {
                setterMethod.invoke(baseInstance, randomValue);
                setterMethod.invoke(equalsInstance, randomValue);
                setterMethod.invoke(differentInstance, differentRandomValue);

            } catch (IllegalAccessException | InvocationTargetException e) {
                fail("Could not invoke setter method for field: " + field.getName());
            }

            testEqualsMethodForField(baseInstance, equalsInstance, differentInstance, field, equalsMethod);
            testHashCodeForField(baseInstance, equalsInstance, differentInstance, field, hashCodeMethod);

            //Set the value back to match so each test is not polluted by previous fields results
            try {
                setterMethod.invoke(differentInstance, randomValue);

            } catch (IllegalAccessException | InvocationTargetException e) {
                fail("Could not invoke setter method for field: " + field.getName());
            }
        }
    }

    private void testEqualsMethodForField(Object baseInstance, Object equalsInstance, Object differentInstance, Field field, Method equalsMethod) {
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
    }

    private void checkEmptyObjectsAreEqual(Object baseInstance, Object equalsInstance, Method equalsMethod) {
        Boolean initialEqualsResult = null;

        try {
            initialEqualsResult = (Boolean) equalsMethod.invoke(baseInstance, equalsInstance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("Could not invoke equals method");
        }

        if (initialEqualsResult != true) {
            fail("Empty objects should have been equal but were not");
        }
    }

    private void checkEmptyObjectsHaveSameHashCode(Object baseInstance, Object equalsInstance, Method hashCodeMethod) {
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
    }

    private void testHashCodeForField(Object baseInstance, Object equalsInstance, Object differentInstance, Field field, Method hashCodeMethod) {
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
