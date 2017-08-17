package com.pojo_unit.pojo_test_classes;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ClassWithDelegateThatUsesParametrisedConstructor
{
    private ClassWithoutNoArgumentConstructor parametrisedDelegate;

    public ClassWithoutNoArgumentConstructor getParametrisedDelegate() {
        return parametrisedDelegate;
    }

    public void setParametrisedDelegate(ClassWithoutNoArgumentConstructor parametrisedDelegate) {
        this.parametrisedDelegate = parametrisedDelegate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClassWithDelegateThatUsesParametrisedConstructor that = (ClassWithDelegateThatUsesParametrisedConstructor) o;

        return new EqualsBuilder()
                .append(parametrisedDelegate, that.parametrisedDelegate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(parametrisedDelegate)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("parametrisedDelegate", parametrisedDelegate)
                .toString();
    }
}
