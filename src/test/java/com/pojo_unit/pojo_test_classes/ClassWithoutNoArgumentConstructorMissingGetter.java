package com.pojo_unit.pojo_test_classes;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ClassWithoutNoArgumentConstructorMissingGetter {
    private String someProperty;

    public ClassWithoutNoArgumentConstructorMissingGetter(final String someProperty) {
        this.someProperty = someProperty;
    }

    public final void setSomeProperty(final String someProperty) {
        this.someProperty = someProperty;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClassWithoutNoArgumentConstructorMissingGetter that = (ClassWithoutNoArgumentConstructorMissingGetter) o;

        return new EqualsBuilder()
                .append(someProperty, that.someProperty)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(someProperty)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("someProperty", someProperty)
                .toString();
    }
}
