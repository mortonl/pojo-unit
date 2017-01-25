package com.pojo_unit;

import com.pojo_unit.pojo_test_classes.ClassMissingEquals;
import com.pojo_unit.pojo_test_classes.ClassMissingGetterForBooleanField;
import com.pojo_unit.pojo_test_classes.ClassMissingGetterForField;
import com.pojo_unit.pojo_test_classes.ClassMissingSetterForField;
import com.pojo_unit.pojo_test_classes.ClassMissingEqualsFields;
import com.pojo_unit.pojo_test_classes.ClassMissingHashCode;
import com.pojo_unit.pojo_test_classes.ClassMissingHashCodeFields;
import com.pojo_unit.pojo_test_classes.ClassWithoutNoArgumentConstructor;
import com.pojo_unit.pojo_test_classes.ValidClass;
import com.pojo_unit.pojo_test_classes.ValidClassWithDelegate;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PojoTestTest
{
    @Test
    public void testValidGettersAndSettersCanBeFoundAndUsed()
    {
        PojoTest tester = new PojoTest(ValidClass.class);

        tester.testGettersAndSetters();
    }

    @Test
    public void testToStringWorksAsExpected()
    {
        PojoTest tester = new PojoTest(ValidClass.class);

        tester.testToString();
    }

    @Test
    public void testEqualsAndHashCodeWorkAsExpected()
    {
        PojoTest tester = new PojoTest(ValidClass.class);

        tester.testEqualsAndHashCode();
    }

    @Test
    public void testValidGettersAndSettersCanBeFoundAndUsedForClassWithDelegate()
    {
        PojoTest tester = new PojoTest(ValidClassWithDelegate.class);

        tester.testGettersAndSetters();
    }

    @Test
    public void testToStringWorksAsExpectedForClassWithDelegate()
    {
        PojoTest tester = new PojoTest(ValidClassWithDelegate.class);

        tester.testToString();
    }

    @Test
    public void testEqualsAndHashCodeWorkAsExpectedForClassWithDelegate()
    {
        PojoTest tester = new PojoTest(ValidClassWithDelegate.class);

        tester.testEqualsAndHashCode();
    }

    @Test
    public void testBooleanGetterMissing()
    {
        PojoTest tester = new PojoTest(ClassMissingGetterForBooleanField.class);

        try
        {
            tester.testGettersAndSetters();
        }
        catch (AssertionError error)
        {
            assertThat(error.getMessage(), is(equalTo("Expected getter method: isTestBoolean was not found")));
        }
    }

    @Test
    public void testStandardSetterMissing()
    {
        PojoTest tester = new PojoTest(ClassMissingSetterForField.class);

        try
        {
            tester.testGettersAndSetters();
        }
        catch (AssertionError error)
        {
            assertThat(error.getMessage(), is(equalTo("Expected setter method: setTestCharacter was not found")));
        }
    }

    @Test
    public void testStandardGetterMissing()
    {
        PojoTest tester = new PojoTest(ClassMissingGetterForField.class);

        try
        {
            tester.testGettersAndSetters();
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

        try
        {
            tester.testEqualsAndHashCode();
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

        try
        {
            tester.testEqualsAndHashCode();
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

        try
        {
            tester.testEqualsAndHashCode();
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

        try
        {
            tester.testEqualsAndHashCode();
        }
        catch (AssertionError error)
        {
            assertThat(error.getMessage(), is(equalTo("Changing the value of field: testCharacter resulted in the test objects hashCode being equal but should not have been")));
        }
    }

    @Test
    public void testGettersAndSettersCanBeTestedForClassWithoutANoArgumentConstructor()
    {
        PojoTest tester = new PojoTest(ClassWithoutNoArgumentConstructor.class);

        tester.testGettersAndSetters();
    }

    @Test
    public void testMissingGettersCanBeTestedForClassWithoutANoArgumentConstructor()
    {
        PojoTest tester = new PojoTest(ClassWithoutNoArgumentConstructor.class);

        tester.testGettersAndSetters();
    }

    @Test
    public void testToStringForClassWithoutANoArgumentConstructor()
    {
        PojoTest tester = new PojoTest(ClassWithoutNoArgumentConstructor.class);

        tester.testToString();
    }

    @Test
    public void testEqualsAndHashCodeForClassWithoutANoArgumentConstructor()
    {
        PojoTest tester = new PojoTest(ClassWithoutNoArgumentConstructor.class);

        tester.testEqualsAndHashCode();
    }
}
