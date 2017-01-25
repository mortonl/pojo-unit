package com.pojo_unit.pojo_test_classes;

public class ClassMissingSetterForField
{
    private Boolean testBoolean;
    private Character testCharacter;

    public Boolean isTestBoolean() {
        return testBoolean;
    }

    public void setTestBoolean(Boolean testBoolean) {
        this.testBoolean = testBoolean;
    }

    public final Character getTestCharacter() {
        return testCharacter;
    }
}
