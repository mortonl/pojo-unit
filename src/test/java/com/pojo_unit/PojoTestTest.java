package com.pojo_unit;

import com.pojo_unit.pojo_test_classes.ClassMissingEquals;
import com.pojo_unit.pojo_test_classes.ClassMissingGetterAndSetterForBooleanField;
import com.pojo_unit.pojo_test_classes.ClassMissingGetterAndSetterForField;
import com.pojo_unit.pojo_test_classes.ClassMissingEqualsFields;
import com.pojo_unit.pojo_test_classes.ClassMissingHashCode;
import com.pojo_unit.pojo_test_classes.ClassMissingHashCodeFields;
import com.pojo_unit.pojo_test_classes.ValidClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PojoTestTest
{
    @Test
    public void testValidGettersAndSettersCanBeFoundAndUsed()
    {
        PojoTest tester = new PojoTest(ValidClass.class);

        ArrayList<String> excludedFields = new ArrayList<>();

        tester.testGettersAndSetters(excludedFields);
    }

    @Test
    public void testToStringWorksAsExpected()
    {
        PojoTest tester = new PojoTest(ValidClass.class);

        ArrayList<String> excludedFields = new ArrayList<>();

        tester.testToString(excludedFields);
    }

    @Test
    public void testEqualsAndHashCodeWorkAsExpected()
    {
        PojoTest tester = new PojoTest(ValidClass.class);

        ArrayList<String> excludedFields = new ArrayList<>();

        tester.testEqualsAndHashCode(excludedFields);
    }

    @Test
    public void testBooleanGetterMissing()
    {
        PojoTest tester = new PojoTest(ClassMissingGetterAndSetterForBooleanField.class);

        ArrayList<String> excludedFields = new ArrayList<>();

        try
        {
            tester.testGettersAndSetters(excludedFields);
        }
        catch (AssertionError error)
        {
            assertThat(error.getMessage(), is(equalTo("Expected getter method: isTestBoolean was not found")));
        }
    }

    @Test
    public void testStandardGetterMissing()
    {
        PojoTest tester = new PojoTest(ClassMissingGetterAndSetterForField.class);

        ArrayList<String> excludedFields = new ArrayList<>();

        try
        {
            tester.testGettersAndSetters(excludedFields);
        }
        catch (AssertionError error)
        {
            assertThat(error.getMessage(), is(equalTo("Expected getter method: getTestCharacter was not found")));
        }
    }

    @Test
    public void testEqualsMissing()
    {
        PojoTest tester = new PojoTest(ClassMissingEquals.class);

        ArrayList<String> excludedFields = new ArrayList<>();

        try
        {
            tester.testEqualsAndHashCode(excludedFields);
        }
        catch (AssertionError error)
        {
            assertThat(error.getMessage(), is(equalTo("Expected method: equals was not found")));
        }
    }

    @Test
    public void testEqualsMissingSomeFields()
    {
        PojoTest tester = new PojoTest(ClassMissingEqualsFields.class);

        ArrayList<String> excludedFields = new ArrayList<>();

        try
        {
            tester.testEqualsAndHashCode(excludedFields);
        }
        catch (AssertionError error)
        {
            assertThat(error.getMessage(), is(equalTo("Changing the value of field: testCharacter resulted in the test objects being equal but should have been different")));
        }
    }

    @Test
    public void testHashCodeMissing()
    {
        PojoTest tester = new PojoTest(ClassMissingHashCode.class);

        ArrayList<String> excludedFields = new ArrayList<>();

        try
        {
            tester.testEqualsAndHashCode(excludedFields);
        }
        catch (AssertionError error)
        {
            assertThat(error.getMessage(), is(equalTo("Expected method: hashCode was not found")));
        }
    }

    @Test
    public void testHashCodeMissingSomeFields()
    {
        PojoTest tester = new PojoTest(ClassMissingHashCodeFields.class);

        ArrayList<String> excludedFields = new ArrayList<>();

        try
        {
            tester.testEqualsAndHashCode(excludedFields);
        }
        catch (AssertionError error)
        {
            assertThat(error.getMessage(), is(equalTo("Changing the value of field: testCharacter resulted in the test objects hashCode being equal but should not have been")));
        }
    }
}
