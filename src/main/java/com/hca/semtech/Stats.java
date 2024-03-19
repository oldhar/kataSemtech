package com.hca.semtech;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Stats {
    Map<Department, DepartmentStats> statsPerDepartment = new HashMap();

    private List<String[]> wrongFormattedLines;

    private Department lowestPopulatedDepartment;


    public Stats addDataFromCity(City city) {
        if (statsPerDepartment.containsKey(city.department)) {
            statsPerDepartment.get(city.department).addCityData(city);
        } else {
            DepartmentStats departmentStats = new DepartmentStats();
            departmentStats.addCityData(city);
            statsPerDepartment.put(city.department, departmentStats);
        }
        return this;
    }

    public Stats aggregate(Stats otherStats) {
        for (Map.Entry<Department, DepartmentStats> entry : otherStats.statsPerDepartment.entrySet()) {
            this.statsPerDepartment.merge(entry.getKey(), entry.getValue(), ((departmentStats, otherDepartmentStats) -> new DepartmentStats(
                    departmentStats.getTotalPopulation() + otherDepartmentStats.getTotalPopulation(),
                    departmentStats.getBiggestCity().population > otherDepartmentStats.getBiggestCity().population
                            ? departmentStats.getBiggestCity()
                            : otherDepartmentStats.getBiggestCity()
            )));
        }
        return this;
    }

    public Department getLeastPopulatedDepartment() {
        if (lowestPopulatedDepartment == null) {
            lowestPopulatedDepartment = statsPerDepartment.values()
                    .stream()
                    .sorted(Comparator.comparing(DepartmentStats::getTotalPopulation))
                    .limit(1)
                    .map(departmentStats -> departmentStats.getBiggestCity().department)
                    .collect(Collectors.toList())
                    .get(0);
        }
        return lowestPopulatedDepartment;
    }


    public void setWrongFormattedLines(List<String[]> wrongFormattedLines) {
        this.wrongFormattedLines = wrongFormattedLines;
    }
}
