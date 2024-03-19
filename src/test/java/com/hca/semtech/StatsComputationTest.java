package com.hca.semtech;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class StatsComputationTest {

    @Nested
    class Creation{
        @Test
        public void throw_exception_on_non_existing_file(){

            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
                new StatsComputation("/not_existing_file");
            });
            illegalArgumentException.getMessage().contains(StatsComputation.Errors.FILE_NOT_EXISTS.getErrorKey());

        }
        @Test
        public void everything_is_ok(){

            String dataFilePath = StatsComputationTest.class.getResource("/population_2019.csv").getPath();

            new StatsComputation(dataFilePath);

        }
    }


    @Nested
    class Computation {

        @Test
        void should_drop_headLine() throws IOException {
            String filePath = new TestFileUtils().createFile()
                    .withLine("Code_Département;Commune;Population;Nom_Département")
                    .withLine("75;Paris;1000000;Paris")
                    .withLine("59;Lille;500000;Nord")
                    .build();
            Stats stats = new StatsComputation(filePath).computeStats();


            assertEquals(2, stats.statsPerDepartment.entrySet().size());
        }

        @Test
        void should_compute_stats() throws IOException {
            String filePath = new TestFileUtils().createFile()
                    .withLine("Code_Département;Commune;Population;Nom_Département")
                    .withLine("59;Dunkerque;200000;Nord")
                    .withLine("75;Paris;1000000;Paris")
                    .withLine("59;Lille;500000;Nord")
                    .build();

            Stats stats = new StatsComputation(filePath).computeStats();

            assertEquals("Nord", stats.getLeastPopulatedDepartment().getName());
            assertEquals("59", stats.getLeastPopulatedDepartment().getCode());


            assertThat(stats.statsPerDepartment.keySet())
                    .extracting(Department::getCode, Department::getName)
                    .containsOnly(new Tuple("59", "Nord"), new Tuple("75", "Paris"));

            assertThat(stats.statsPerDepartment.values())
                    .extracting(DepartmentStats::getTotalPopulation)
                    .containsOnly(1000000L, 700000L);

            assertThat(stats.statsPerDepartment.values())
                    .extracting(DepartmentStats::getBiggestCity)
                    .extracting(City::getName, City::getPopulation)
                    .containsOnly(new Tuple("Lille", 500000L), new Tuple("Paris", 1000000L));

        }

        @Test
        void should_compute_stats_with_missing_department_code() throws IOException {
            String filePath = new TestFileUtils().createFile()
                    .withLine("Code_Département;Commune;Population;Nom_Département")
                    .withLine("59;Dunkerque;200000;Nord")
                    .withLine("75;Paris;1000000;Paris")
                    .withLine(";Lille;500000;Nord")
                    .build();

            Stats stats = new StatsComputation(filePath).computeStats();

            assertEquals("Nord", stats.getLeastPopulatedDepartment().getName());
            assertEquals("59", stats.getLeastPopulatedDepartment().getCode());


            assertThat(stats.statsPerDepartment.keySet())
                    .extracting(Department::getCode, Department::getName)
                    .containsOnly(new Tuple("59", "Nord"), new Tuple("75", "Paris"));

            assertThat(stats.statsPerDepartment.values())
                    .extracting(DepartmentStats::getTotalPopulation)
                    .containsOnly(1000000L, 700000L);

            assertThat(stats.statsPerDepartment.values())
                    .extracting(DepartmentStats::getBiggestCity)
                    .extracting(City::getName, City::getPopulation)
                    .containsOnly(new Tuple("Lille", 500000L), new Tuple("Paris", 1000000L));

        }

        @Test
        void should_compute_stats_with_missing_department_name() throws IOException {
            String filePath = new TestFileUtils().createFile()
                    .withLine("Code_Département;Commune;Population;Nom_Département")
                    .withLine("59;Dunkerque;200000;Nord")
                    .withLine("75;Paris;1000000;Paris")
                    .withLine("59;Lille;500000;")
                    .build();

            Stats stats = new StatsComputation(filePath).computeStats();

            assertEquals("Nord", stats.getLeastPopulatedDepartment().getName());
            assertEquals("59", stats.getLeastPopulatedDepartment().getCode());


            assertThat(stats.statsPerDepartment.keySet())
                    .extracting(Department::getCode, Department::getName)
                    .containsOnly(new Tuple("59", "Nord"), new Tuple("75", "Paris"));

            assertThat(stats.statsPerDepartment.values())
                    .extracting(DepartmentStats::getTotalPopulation)
                    .containsOnly(1000000L, 700000L);

            assertThat(stats.statsPerDepartment.values())
                    .extracting(DepartmentStats::getBiggestCity)
                    .extracting(City::getName, City::getPopulation)
                    .containsOnly(new Tuple("Lille", 500000L), new Tuple("Paris", 1000000L));

        }

        @Test
        void should_compute_stats_with_missing_department_name_at_the_beginning() throws IOException {
            String filePath = new TestFileUtils().createFile()
                    .withLine("Code_Département;Commune;Population;Nom_Département")
                    .withLine("59;Dunkerque;200000;")
                    .withLine("75;Paris;1000000;Paris")
                    .withLine("59;Lille;500000;Nord")
                    .build();

            Stats stats = new StatsComputation(filePath).computeStats();

            assertEquals("Nord", stats.getLeastPopulatedDepartment().getName());
            assertEquals("59", stats.getLeastPopulatedDepartment().getCode());


            assertThat(stats.statsPerDepartment.keySet())
                    .extracting(Department::getCode, Department::getName)
                    .containsOnly(new Tuple("59", "Nord"), new Tuple("75", "Paris"));

            assertThat(stats.statsPerDepartment.values())
                    .extracting(DepartmentStats::getTotalPopulation)
                    .containsOnly(1000000L, 700000L);

            assertThat(stats.statsPerDepartment.values())
                    .extracting(DepartmentStats::getBiggestCity)
                    .extracting(City::getName, City::getPopulation)
                    .containsOnly(new Tuple("Lille", 500000L), new Tuple("Paris", 1000000L));

        }

        @Test
        void should_compute_stats_using_department_code_on_all_lines_missing_department_name() throws IOException {
            String filePath = new TestFileUtils().createFile()
                    .withLine("Code_Département;Commune;Population;Nom_Département")
                    .withLine("59;Dunkerque;200000;")
                    .withLine("75;Paris;1000000;Paris")
                    .withLine("59;Lille;500000;")
                    .build();

            Stats stats = new StatsComputation(filePath).computeStats();

            assertEquals("59", stats.getLeastPopulatedDepartment().getName());
            assertEquals("59", stats.getLeastPopulatedDepartment().getCode());


            assertThat(stats.statsPerDepartment.keySet())
                    .extracting(Department::getCode, Department::getName)
                    .containsOnly(new Tuple("59", "59"), new Tuple("75", "Paris"));

            assertThat(stats.statsPerDepartment.values())
                    .extracting(DepartmentStats::getTotalPopulation)
                    .containsOnly(1000000L, 700000L);

            assertThat(stats.statsPerDepartment.values())
                    .extracting(DepartmentStats::getBiggestCity)
                    .extracting(City::getName, City::getPopulation)
                    .containsOnly(new Tuple("Lille", 500000L), new Tuple("Paris", 1000000L));

        }

        @Test
        void should_compute_500_cities_file() throws IOException {
            String filePath = StatsComputationTest.class.getResource("/population_2019_500.csv").getPath();

            Stats stats = new StatsComputation(filePath).computeStats();

            assertThat(stats.getLeastPopulatedDepartment()).extracting(Department::getCode, Department::getName)
                    .containsOnly("19", "CORREZE");

        }

        @Test
        void should_compute_cities_file() throws IOException {
            String filePath = StatsComputationTest.class.getResource("/population_2019.csv").getPath();

            Stats stats = new StatsComputation(filePath).computeStats();

            assertThat(stats.getLeastPopulatedDepartment()).extracting(Department::getCode, Department::getName)
                    .containsOnly("48", "LOZERE");

        }

    }




}