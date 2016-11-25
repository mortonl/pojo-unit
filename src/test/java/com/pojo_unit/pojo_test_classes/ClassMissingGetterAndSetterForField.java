package com.pojo_unit.pojo_test_classes;

public class ClassMissingGetterAndSetterForField
{
    private Boolean testBoolean;
    private Character testCharacter;

    public Boolean isTestBoolean() {
        return testBoolean;
    }

    public void setTestBoolean(Boolean testBoolean) {
        this.testBoolean = testBoolean;
    }
}
