package com.pojo_unit.pojo_test_classes;

public class ClassMissingGetterForField
{
    private Boolean testBoolean;
    private Character testCharacter;

    public Boolean isTestBoolean() {
        return testBoolean;
    }

    public void setTestBoolean(Boolean testBoolean) {
        this.testBoolean = testBoolean;
    }

    public final void setTestCharacter(final Character testCharacter) {
        this.testCharacter = testCharacter;
    }
}
