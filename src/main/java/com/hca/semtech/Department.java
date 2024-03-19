package com.hca.semtech;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Department {

    private String code;
    private String name;

    public Department(String code, String name){
        this.code = code;
        this.name = name;

        checkParameters();
    }

    private void checkParameters() {
        List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(code)) {
            errors.add(Errors.CODE_EMPTY.getErrorKey());
        }
        if (StringUtils.isEmpty(name)) {
            errors.add(Errors.NAME_EMPTY.getErrorKey());
        }


        if(!errors.isEmpty()){
            throw new IllegalArgumentException("Cannot instanciate department: "+ errors.stream().reduce("", (acc,  key)->acc+" "+key));
        }
    }

    public enum Errors {
        CODE_EMPTY("department.code.empty"),
        NAME_EMPTY("department.name.empty");

        private final String errorKey;

        Errors(String errorKey) {
            this.errorKey = errorKey;
        }

        public String getErrorKey() {
            return errorKey;
        }
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(code, that.code) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name);
    }

}
