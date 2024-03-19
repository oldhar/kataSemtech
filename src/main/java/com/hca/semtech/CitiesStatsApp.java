package com.hca.semtech;

import java.io.IOException;

public class CitiesStatsApp {

    public static void main(String[] args) {
        if (args.length!=1){
            System.out.println("Usage: CitiesStatsApp /path/to/the/csv");
            return;
        }

        try {
            Stats stats = new StatsComputation(args[0]).computeStats();
            stats.statsPerDepartment.entrySet().forEach(entry->{
                DepartmentStats departmentStats = entry.getValue();
                System.out.println("Department: "+entry.getKey().getName()+", population: "+ departmentStats.getTotalPopulation().longValue()+", biggest city in the department: "+departmentStats.getBiggestCity().getName());
            });
            System.out.println("Least populated department:"+stats.getLeastPopulatedDepartment().getName());
        } catch (IOException e) {
            System.out.println("Cannot access file:" + args[0]+", error:"+e.getMessage());
        }
    }
}
