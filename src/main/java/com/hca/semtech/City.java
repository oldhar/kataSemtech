package com.hca.semtech;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class City {
    Department department;
    String name;
    Long population;

    public City(Department department, String name, Long population) {
        this.department = department;
        this.name = name;
        this.population = population;
        checkParameters();
    }

    private void checkParameters() {
        List<String> errors = new ArrayList<>();
        if (department==null) {
            errors.add(Errors.DEPARTMENT_EMPTY.getErrorKey());
        }
        if (StringUtils.isEmpty(name)) {
            errors.add(Errors.CITY_NAME_EMPTY.getErrorKey());
        }
        //TODO: Business where a city can be empty?
//        if (population==null || population == 0) {
//            errors.add(Errors.CITY_POPULATION_EMPTY.getErrorKey());
//        }


        if(!errors.isEmpty()){
            throw new IllegalArgumentException("Cannot instanciate City data: "+ errors.stream().reduce("", (acc,  key)->acc+" "+key));
        }
    }

    public enum Errors {
        DEPARTMENT_EMPTY("city.department.empty"),
        CITY_NAME_EMPTY("city.name.empty"),
        CITY_POPULATION_EMPTY("city.population.empty");

        private final String errorKey;

        Errors(String errorKey) {
            this.errorKey = errorKey;
        }

        public String getErrorKey() {
            return errorKey;
        }
    }

    public String getName() {
        return name;
    }

    public Long getPopulation() {
        return population;
    }
}
