package com.hca.semtech;

public class DepartmentStats implements Comparable{

    private Long totalPopulation = 0L;
    private City biggestCity;

    public DepartmentStats() {
    }

    public DepartmentStats(Long totalPopulation, City biggestCity) {
        this.totalPopulation = totalPopulation;
        this.biggestCity = biggestCity;
    }

    public void addCityData(City city) {
        totalPopulation+= city.population;
        if(biggestCity==null){
            biggestCity = city;
        } else {
            if (biggestCity.population < city.population) {
                biggestCity = city;
            }
        }
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof DepartmentStats){
            return this.totalPopulation.compareTo(((DepartmentStats) o).totalPopulation);
        }
        throw new IllegalArgumentException("Not comparable with DepartmentStats");
    }

    public Long getTotalPopulation() {
        return totalPopulation;
    }

    public City getBiggestCity() {
        return biggestCity;
    }
}
