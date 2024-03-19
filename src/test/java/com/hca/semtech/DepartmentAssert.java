package com.hca.semtech;

import org.assertj.core.api.AbstractAssert;

public class DepartmentAssert extends AbstractAssert<DepartmentAssert, Department> {
    protected DepartmentAssert(Department department) {
        super(department, DepartmentAssert.class);
    }

    public static DepartmentAssert assertThat(Department department){
        return new DepartmentAssert(department);
    }

    public DepartmentAssert equals(Department department){
        isNotNull();
        if(actual.getName()!=department.getName()){
            failWithMessage("Expected name to be <%s> but was <%s>", department.getName(), actual.getName());
        }
        if(actual.getCode()!=department.getCode()){
            failWithMessage("Expected code to be <%s> but was <%s>", department.getCode(), actual.getCode());
        }
        return this;
    }
}
