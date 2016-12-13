package com.pojo_unit.pojo_test_classes;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ValidClass
{
    private static final String TEST_STRING_CONSTANT = "someImportantText";

    private Boolean testBoolean;
    private boolean testPrimitiveBoolean;
    private Character testCharacter;
    private char testPrimitiveChar;
    private Byte testByte;
    private byte testPrimitiveByte;
    private Short testShort;
    private short testPrimitiveShort;
    private Integer testInteger;
    private int testPrimitiveInt;
    private Long testLong;
    private long testPrimitiveLong;
    private Float testFloat;
    private float testPrimitiveFloat;
    private Double testDouble;
    private double testPrimitiveDouble;
    private BigDecimal testBigDecimal;
    private Date testDate;
    private List<String> testList;
    private String[] testArray;
    private EnumExample testEnum;
    private List<EnumExample> testEnumList;

    public Boolean isTestBoolean() {
        return testBoolean;
    }

    public void setTestBoolean(Boolean testBoolean) {
        this.testBoolean = testBoolean;
    }

    public boolean isTestPrimitiveBoolean() {
        return testPrimitiveBoolean;
    }

    public void setTestPrimitiveBoolean(boolean testPrimitiveBoolean) {
        this.testPrimitiveBoolean = testPrimitiveBoolean;
    }

    public Character getTestCharacter() {
        return testCharacter;
    }

    public void setTestCharacter(Character testCharacter) {
        this.testCharacter = testCharacter;
    }

    public char getTestPrimitiveChar() {
        return testPrimitiveChar;
    }

    public void setTestPrimitiveChar(char testPrimitiveChar) {
        this.testPrimitiveChar = testPrimitiveChar;
    }

    public Byte getTestByte() {
        return testByte;
    }

    public void setTestByte(Byte testByte) {
        this.testByte = testByte;
    }

    public byte getTestPrimitiveByte() {
        return testPrimitiveByte;
    }

    public void setTestPrimitiveByte(byte testPrimitiveByte) {
        this.testPrimitiveByte = testPrimitiveByte;
    }

    public Short getTestShort() {
        return testShort;
    }

    public void setTestShort(Short testShort) {
        this.testShort = testShort;
    }

    public short getTestPrimitiveShort() {
        return testPrimitiveShort;
    }

    public void setTestPrimitiveShort(short testPrimitiveShort) {
        this.testPrimitiveShort = testPrimitiveShort;
    }

    public Integer getTestInteger() {
        return testInteger;
    }

    public void setTestInteger(Integer testInteger) {
        this.testInteger = testInteger;
    }

    public int getTestPrimitiveInt() {
        return testPrimitiveInt;
    }

    public void setTestPrimitiveInt(int testPrimitiveInt) {
        this.testPrimitiveInt = testPrimitiveInt;
    }

    public Long getTestLong() {
        return testLong;
    }

    public void setTestLong(Long testLong) {
        this.testLong = testLong;
    }

    public long getTestPrimitiveLong() {
        return testPrimitiveLong;
    }

    public void setTestPrimitiveLong(long testPrimitiveLong) {
        this.testPrimitiveLong = testPrimitiveLong;
    }

    public Float getTestFloat() {
        return testFloat;
    }

    public void setTestFloat(Float testFloat) {
        this.testFloat = testFloat;
    }

    public float getTestPrimitiveFloat() {
        return testPrimitiveFloat;
    }

    public void setTestPrimitiveFloat(float testPrimitiveFloat) {
        this.testPrimitiveFloat = testPrimitiveFloat;
    }

    public Double getTestDouble() {
        return testDouble;
    }

    public void setTestDouble(Double testDouble) {
        this.testDouble = testDouble;
    }

    public double getTestPrimitiveDouble() {
        return testPrimitiveDouble;
    }

    public void setTestPrimitiveDouble(double testPrimitiveDouble) {
        this.testPrimitiveDouble = testPrimitiveDouble;
    }

    public BigDecimal getTestBigDecimal() {
        return testBigDecimal;
    }

    public void setTestBigDecimal(BigDecimal testBigDecimal) {
        this.testBigDecimal = testBigDecimal;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    public List<String> getTestList() {
        return testList;
    }

    public void setTestList(List<String> testList) {
        this.testList = testList;
    }

    public String[] getTestArray() {
        return testArray;
    }

    public void setTestArray(String[] testArray) {
        this.testArray = testArray;
    }

    public EnumExample getTestEnum() {
        return testEnum;
    }

    public void setTestEnum(EnumExample testEnum) {
        this.testEnum = testEnum;
    }

    public List<EnumExample> getTestEnumList() {
        return testEnumList;
    }

    public void setTestEnumList(List<EnumExample> testEnumList) {
        this.testEnumList = testEnumList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ValidClass that = (ValidClass) o;

        return new EqualsBuilder()
                .append(testPrimitiveBoolean, that.testPrimitiveBoolean)
                .append(testPrimitiveChar, that.testPrimitiveChar)
                .append(testPrimitiveByte, that.testPrimitiveByte)
                .append(testPrimitiveShort, that.testPrimitiveShort)
                .append(testPrimitiveInt, that.testPrimitiveInt)
                .append(testPrimitiveLong, that.testPrimitiveLong)
                .append(testPrimitiveFloat, that.testPrimitiveFloat)
                .append(testPrimitiveDouble, that.testPrimitiveDouble)
                .append(testBoolean, that.testBoolean)
                .append(testCharacter, that.testCharacter)
                .append(testByte, that.testByte)
                .append(testShort, that.testShort)
                .append(testInteger, that.testInteger)
                .append(testLong, that.testLong)
                .append(testFloat, that.testFloat)
                .append(testDouble, that.testDouble)
                .append(testBigDecimal, that.testBigDecimal)
                .append(testDate, that.testDate)
                .append(testList, that.testList)
                .append(testArray, that.testArray)
                .append(testEnum, that.testEnum)
                .append(testEnumList, that.testEnumList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(testBoolean)
                .append(testPrimitiveBoolean)
                .append(testCharacter)
                .append(testPrimitiveChar)
                .append(testByte)
                .append(testPrimitiveByte)
                .append(testShort)
                .append(testPrimitiveShort)
                .append(testInteger)
                .append(testPrimitiveInt)
                .append(testLong)
                .append(testPrimitiveLong)
                .append(testFloat)
                .append(testPrimitiveFloat)
                .append(testDouble)
                .append(testPrimitiveDouble)
                .append(testBigDecimal)
                .append(testDate)
                .append(testList)
                .append(testArray)
                .append(testEnum)
                .append(testEnumList)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("testBoolean", testBoolean)
                .append("testPrimitiveBoolean", testPrimitiveBoolean)
                .append("testCharacter", testCharacter)
                .append("testPrimitiveChar", testPrimitiveChar)
                .append("testByte", testByte)
                .append("testPrimitiveByte", testPrimitiveByte)
                .append("testShort", testShort)
                .append("testPrimitiveShort", testPrimitiveShort)
                .append("testInteger", testInteger)
                .append("testPrimitiveInt", testPrimitiveInt)
                .append("testLong", testLong)
                .append("testPrimitiveLong", testPrimitiveLong)
                .append("testFloat", testFloat)
                .append("testPrimitiveFloat", testPrimitiveFloat)
                .append("testDouble", testDouble)
                .append("testPrimitiveDouble", testPrimitiveDouble)
                .append("testBigDecimal", testBigDecimal)
                .append("testDate", testDate)
                .append("testList", testList)
                .append("testArray", testArray)
                .append("testEnum", testEnum)
                .append("testEnumList", testEnumList)
                .toString();
    }
}
