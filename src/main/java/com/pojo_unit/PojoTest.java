package com.pojo_unit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PojoTest {

    private final Class<?> clazzToTest;
    private final List<Field> testableFields;
    private final RandomObjectFactory randomObjectFactory;

    public PojoTest(final Class<?> clazzToTest) {
        this(clazzToTest, new ArrayList<String>());
    }

    public PojoTest(final Class<?> clazzToTest, List<String> excludedFieldNames) {
        this.clazzToTest = clazzToTest;
        this.randomObjectFactory = new RandomObjectFactory();

        this.testableFields = randomObjectFactory.getTestableFieldsForClass(clazzToTest, excludedFieldNames);
    }

    /**
     * Test Getters And Setters for a class,
     */
    public void testGettersAndSetters() {
        testGettersAndSetters(new ArrayList<String>(), new ArrayList<String>());
    }

    /**
     * Test Getters And Setters for a class,
     *
     * @param getterExclusions
     * @param setterExclusions
     */
    public void testGettersAndSetters(List<String> getterExclusions, List<String> setterExclusions) {
        Object testableInstance = getTestableInstance();

        for (Field field : testableFields) {
            String fieldName = field.getName();

            //setters which are excluded or implicitly excluded by the final keyword shouldn't be tested
            boolean isSetterTestable = !isFieldExcluded(setterExclusions, fieldName) && !isFieldFinal(field);

            //getters which are excluded shouldn't be tested
            boolean isGetterTestable = !isFieldExcluded(getterExclusions, fieldName);

            if (isSetterTestable && isGetterTestable) {
                Method setterMethod = getSetterMethod(field);
                Method getterMethod = getGetterMethod(field);

                Object randomValue = randomObjectFactory.getRandomValueForField(field);
                invokeSetterMethod(testableInstance, setterMethod, fieldName, randomValue);

                Object result = invokeGetterMethod(testableInstance, getterMethod, fieldName);
                assertEquals(randomValue, result);
            } else if (isGetterTestable) {
                Object randomValue = randomObjectFactory.getRandomValueForField(field);

                setFieldUsingReflection(field, testableInstance, randomValue);

                Method getterMethod = getGetterMethod(field);
                Object result = invokeGetterMethod(testableInstance, getterMethod, fieldName);
                assertEquals(randomValue, result);
            }
        }
    }

    private void setFieldUsingReflection(final Field field, final Object testableInstance, final Object randomValue) {
        field.setAccessible(true);
        try {
            field.set(testableInstance, randomValue);
        } catch (IllegalAccessException e) {
            fail("Could not set value of field via reflection");
        }
    }

    private Object getTestableInstance() {
        Object testableInstance = null;

        if (classHasZeroArgumentPublicConstructor()) {
            testableInstance = getObjectInstance();
        } else {
            Constructor<?> constructor = getSmallestConstructor();
            List<Object> parameters = new ArrayList<>();

            for (Class<?> parameterType : constructor.getParameterTypes()) {
                Object randomValue = randomObjectFactory.getRandomValueForType(parameterType);
                parameters.add(randomValue);
            }

            try {
                testableInstance = constructor.newInstance(parameters.toArray());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                fail("Could not instantiate class");
            }
        }

        return testableInstance;
    }

    private Object getObjectInstance() {
        Object testableInstance = null;

        try {
            testableInstance = clazzToTest.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            fail("Could not instantiate class");
        }
        return testableInstance;
    }

    private boolean classHasZeroArgumentPublicConstructor() {
        for (Constructor<?> constructor : clazzToTest.getConstructors()) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();

            if (parameterTypes.length == 0) {
                return true;
            }
        }
        return false;
    }

    private Constructor<?> getSmallestConstructor() {
        Constructor<?>[] constructors = clazzToTest.getConstructors();

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

    private boolean isFieldExcluded(final List<String> fieldsExcluded, final String fieldName) {
        return fieldsExcluded.contains(fieldName);
    }

    private static boolean isFieldFinal(Field field) {
        return Modifier.isFinal(field.getModifiers());
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

    private String getExpectedSetterName(Field field) {
        return "set" + capitalize(field.getName());
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

    private String getExpectedGetterName(Field field) {
        String expectedGetterName;

        if (field.getType() == boolean.class || field.getType() == Boolean.class) {
            expectedGetterName = "is" + capitalize(field.getName());
        } else {
            expectedGetterName = "get" + capitalize(field.getName());
        }

        return expectedGetterName;
    }

    private void invokeSetterMethod(final Object testableInstance, final Method setterMethod, final String fieldName, final Object randomValue) {
        try {
            setterMethod.invoke(testableInstance, randomValue);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("Could not invoke setter method for field: " + fieldName);
        }
    }

    private Object invokeGetterMethod(final Object testableInstance, final Method getterMethod, final String fieldName) {
        Object result = null;

        try {
            result = getterMethod.invoke(testableInstance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("Could not invoke getter method for field: " + fieldName);
        }

        return result;
    }

    public void testToString() {
        testToString(new ArrayList<String>());
    }

    public void testToString(List<String> excludedFields) {
        List<String> stringValues = new ArrayList<>();

        Object testableInstance = getTestableInstance();

        for (Field field : testableFields) {
            String fieldName = field.getName();

            //setters which are excluded or implicitly excluded by the final keyword shouldn't be tested
            boolean isFieldExpected = !isFieldExcluded(excludedFields, fieldName);

            if (isFieldExpected) {
                stringValues.add(fieldName);

                Object randomValue = randomObjectFactory.getRandomValueForField(field);

                if (randomValue == null) {
                    fail("This random value shouldn't have been null");
                }

                setFieldUsingReflection(field, testableInstance, randomValue);

                String toString = randomValue.toString();

                if (toString.contains("[L")) {
                    toString = Arrays.toString((Object[]) randomValue).replaceAll("\\[", "").replaceAll("\\]", "");
                }
                stringValues.add(toString);
            }
        }

        String toStringResult = invokeToString(testableInstance);

        for (String expectedToStringItem : stringValues) {
            if (!toStringResult.contains(expectedToStringItem)) {
                fail("The objects toString did not contain the expected item: " + expectedToStringItem);
            }
        }
    }

    private String invokeToString(final Object testableInstance) {
        String toStringResult = null;

        try {
            Method toStringMethod = clazzToTest.getDeclaredMethod("toString");
            toStringResult = (String) toStringMethod.invoke(testableInstance);
        } catch (NoSuchMethodException e) {
            fail("Expected method: toString was not found");
        } catch (InvocationTargetException | IllegalAccessException e) {
            fail("Could not invoke method: toString");
        }

        return toStringResult;
    }

    public void testEqualsAndHashCode() {
        testEqualsAndHashCode(new ArrayList<String>());
    }

    public void testEqualsAndHashCode(List<String> excludedFields) {
        List<Object> instances = getTestableInstancesForEqualsAndHashCode();

        Object baseInstance = instances.get(0);
        Object equalsInstance = instances.get(1);
        Object differentInstance = instances.get(2);

        Method equalsMethod = getEqualsMethod();
        Method hashCodeMethod = getHashCodeMethod();

        checkEmptyObjectsAreEqual(baseInstance, equalsInstance, equalsMethod);
        checkEmptyObjectsHaveSameHashCode(baseInstance, equalsInstance, hashCodeMethod);

        for (Field field : testableFields) {
            String fieldName = field.getName();

            if (excludedFields.contains(fieldName)) {
                break;
            }

            Object randomValue = randomObjectFactory.getRandomValueForField(field);
            Object differentRandomValue = randomObjectFactory.getRandomValueForField(field);

            if (randomValue == null || differentRandomValue == null) {
                fail("This random value shouldn't have been null");
            }

            Integer attemptNumber = 0;
            while (differentRandomValue.equals(randomValue)) {
                if (attemptNumber > 5) {
                    fail("Could not generate two different random values for field: " + fieldName);
                }
                differentRandomValue = randomObjectFactory.getRandomValueForField(field);
                attemptNumber++;
            }

            setFieldUsingReflection(field, baseInstance, randomValue);
            setFieldUsingReflection(field, equalsInstance, randomValue);
            setFieldUsingReflection(field, differentInstance, differentRandomValue);

            testEqualsMethodForField(baseInstance, equalsInstance, differentInstance, field, equalsMethod);
            testHashCodeForField(baseInstance, equalsInstance, differentInstance, field, hashCodeMethod);

            //Set the value back to match so each test is not polluted by previous fields results
            setFieldUsingReflection(field, differentInstance, randomValue);
        }
    }

    private List<Object> getTestableInstancesForEqualsAndHashCode() {
        Object testableInstance1 = null;
        Object testableInstance2 = null;
        Object testableInstance3 = null;

        if (classHasZeroArgumentPublicConstructor()) {
            testableInstance1 = getObjectInstance();
            testableInstance2 = getObjectInstance();
            testableInstance3 = getObjectInstance();
        } else {
            Constructor<?> constructor = getSmallestConstructor();
            List<Object> parameters = new ArrayList<>();

            for (Class<?> parameterType : constructor.getParameterTypes()) {
                Object randomValue = randomObjectFactory.getRandomValueForType(parameterType);
                parameters.add(randomValue);
            }

            try {
                testableInstance1 = constructor.newInstance(parameters.toArray());
                testableInstance2 = constructor.newInstance(parameters.toArray());
                testableInstance3 = constructor.newInstance(parameters.toArray());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                fail("Could not instantiate class");
            }
        }

        return Arrays.asList(testableInstance1, testableInstance2, testableInstance3);
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

    private Method getHashCodeMethod() {
        Method hashCodeMethod = null;
        try {
            hashCodeMethod = clazzToTest.getDeclaredMethod("hashCode");
        } catch (NoSuchMethodException e) {
            fail("Expected method: hashCode was not found");
        }
        return hashCodeMethod;
    }

    private void checkEmptyObjectsAreEqual(Object baseInstance, Object equalsInstance, Method equalsMethod) {
        Boolean equalsResult = null;

        try {
            equalsResult = (Boolean) equalsMethod.invoke(baseInstance, equalsInstance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("Could not invoke equals method");
        }

        if (!equalsResult) {
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
}
