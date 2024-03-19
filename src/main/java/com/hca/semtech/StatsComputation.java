package com.hca.semtech;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class StatsComputation {

    private final String dataFilePath;

    public StatsComputation(String dataFilePath) {
        this.dataFilePath = dataFilePath;
        checkFile(dataFilePath);
    }

    private void checkFile(String dataFilePath) {
        boolean exists = new File(dataFilePath).exists();
        List<String> errors = new ArrayList<>();
        if (!exists) {
            errors.add(Errors.FILE_NOT_EXISTS.errorKey);
        }
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Errors: " + errors.stream().reduce("", (acc, key) -> acc + " " + key));
        }

    }

    public Stats computeStats() throws IOException {
        List<String[]> wrongFormattedLines = new ArrayList<>();
        Map</*departmentCode*/String, /*departmentName*/String> departmentsReferential = new HashMap<>();

        Stats computedStats = Files.readAllLines(Path.of(dataFilePath))
                .stream()
                .skip(1)
                .map(line -> line.split(";", -1))
                .map((String[] columns) -> removeLinesWithWrongColumnsNumber(columns, wrongFormattedLines))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(columns -> ((String[]) columns)[0]).reversed().thenComparing(Comparator.comparing(columns -> ((String[]) columns)[3]).reversed()))
                .map(columns -> mapRawData(columns, wrongFormattedLines))
                .filter(Objects::nonNull)
                .map(rawCityData -> cleanDepartmentCodeProperty(rawCityData, wrongFormattedLines, departmentsReferential))
                .filter(Objects::nonNull)
                .map(rawCityData -> cleanDepartmentNameProperty(rawCityData, departmentsReferential))
                .map(rawCityData -> {
                    try{
                        return rawCityData.asCity();
                    } catch (IllegalArgumentException e) {
                        wrongFormattedLines.add(rawCityData.asStringArray());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .peek(city -> addDepartmentToReferal(city, departmentsReferential))
                .reduce(
                        new Stats(),
                        (stats, city) -> stats.addDataFromCity(city),
                        (stats, otherStats) -> stats.aggregate(otherStats)
                );
        computedStats.setWrongFormattedLines(wrongFormattedLines);

        return computedStats;

    }

    private static void addDepartmentToReferal(City city, Map<String, String> departmentsReferential) {
        if(!departmentsReferential.containsKey(city.department.getCode())){
            departmentsReferential.put(city.department.getCode(), city.department.getName());
        }
    }

    private RawCityData cleanDepartmentNameProperty(RawCityData rawCityData, Map<String, String> departmentsReferential) {
        if (StringUtils.isBlank(rawCityData.departmentName)) {
            String departmentName;
            if (departmentsReferential.containsKey(rawCityData.departmentCode)) {
                 departmentName = departmentsReferential.get(rawCityData.departmentCode);
            } else {
                //TODO: Discuss about business choice
                departmentName= rawCityData.departmentCode;
            }
            return new RawCityData(rawCityData.departmentCode, departmentName, rawCityData.cityName, rawCityData.population);
        }
        return rawCityData;
    }

    private RawCityData cleanDepartmentCodeProperty(RawCityData rawCityData, List<String[]> wrongFormattedLines, Map<String, String> departmentsReferential) {
        if (StringUtils.isBlank(rawCityData.departmentCode)) {
            if (departmentsReferential.containsValue(rawCityData.departmentName)) {
                String departmentName = rawCityData.departmentName;
                String departmentCode = departmentsReferential.entrySet().stream().filter(entry -> entry.getValue().equals(departmentName)).findFirst().get().getKey();
                return new RawCityData(departmentCode, departmentName, rawCityData.cityName, rawCityData.population);
            } else {
                wrongFormattedLines.add(rawCityData.asStringArray());
                return null;
            }
        }
        return rawCityData;
    }

    private RawCityData mapRawData(String[] columns, List<String[]> wrongFormattedLines) {
        Long population;
        try {
            population = Long.valueOf(columns[2]);
        } catch (NumberFormatException e) {
            wrongFormattedLines.add(columns);
            return null;
        }
        return new RawCityData(columns[0], columns[3], columns[1], population);
    }

    private static String[] removeLinesWithWrongColumnsNumber(String[] columns, List<String[]> wrongFormattedLine) {
        if (columns.length != 4 || (StringUtils.isBlank(columns[0]) && StringUtils.isBlank(columns[3]))) {
            wrongFormattedLine.add(columns);
            return null;
        }
        return columns;
    }

    public enum Errors {
        FILE_NOT_EXISTS("file.not.exists");

        private final String errorKey;

        Errors(String errorKey) {
            this.errorKey = errorKey;
        }

        public String getErrorKey() {
            return errorKey;
        }
    }

    private class RawCityData{
        private final String departmentCode;
        private final String departmentName;
        private final String cityName;
        private final Long population;

        public RawCityData(String departmentCode, String departmentName, String cityName, Long population) {
            this.departmentCode = departmentCode;
            this.departmentName = departmentName;
            this.cityName = cityName;
            this.population = population;
        }

        public String[] asStringArray() {
            return new String[]{departmentCode, cityName, population.toString(), departmentName};
        }

        public City asCity() {
            return new City(new Department(this.departmentCode, this.departmentName), this.cityName, this.population);
        }
    }

}
