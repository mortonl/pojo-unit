package com.pojo_unit.pojo_test_classes;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ClassWithoutNoArgumentConstructor {

    private String someProperty;
    private final String someFinalProperty;

    public ClassWithoutNoArgumentConstructor(final String someProperty, final String someFinalProperty) {
        this.someProperty = someProperty;
        this.someFinalProperty = someFinalProperty;
    }

    public final String getSomeProperty() {
        return someProperty;
    }

    public final void setSomeProperty(final String someProperty) {
        this.someProperty = someProperty;
    }

    public final String getSomeFinalProperty() {
        return someFinalProperty;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClassWithoutNoArgumentConstructor that = (ClassWithoutNoArgumentConstructor) o;

        return new EqualsBuilder()
                .append(someProperty, that.someProperty)
                .append(someFinalProperty, that.someFinalProperty)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(someProperty)
                .append(someFinalProperty)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("someProperty", someProperty)
                .append("someFinalProperty", someFinalProperty)
                .toString();
    }
}
