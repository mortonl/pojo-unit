package com.pojo_unit.pojo_test_classes;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ValidClassWithDelegate
{
    private ValidClass validClazz;

    public ValidClass getValidClazz() {
        return validClazz;
    }

    public void setValidClazz(ValidClass validClazz) {
        this.validClazz = validClazz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ValidClassWithDelegate that = (ValidClassWithDelegate) o;

        return new EqualsBuilder()
                .append(validClazz, that.validClazz)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(validClazz)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("validClazz", validClazz)
                .toString();
    }
}
