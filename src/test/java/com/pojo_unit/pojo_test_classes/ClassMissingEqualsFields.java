package com.pojo_unit.pojo_test_classes;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ClassMissingEqualsFields
{
    private Boolean testBoolean;
    private Character testCharacter;

    public Boolean getTestBoolean() {
        return testBoolean;
    }

    public void setTestBoolean(Boolean testBoolean) {
        this.testBoolean = testBoolean;
    }

    public Character getTestCharacter() {
        return testCharacter;
    }

    public void setTestCharacter(Character testCharacter) {
        this.testCharacter = testCharacter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ClassMissingEqualsFields that = (ClassMissingEqualsFields) o;

        return new EqualsBuilder()
                .append(testBoolean, that.testBoolean)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(testBoolean)
                .append(testCharacter)
                .toHashCode();
    }
}
