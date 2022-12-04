import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class Tools {
    public static Solution getGreedySolution(int start_city) {
        Problem problem = Problem.getProblem();

        ArrayList<Integer> city_list = new ArrayList<Integer>(problem.dimension + 1);

        int left_number_of_towns = problem.dimension - 1;
        int actual_city_index = start_city;

        city_list.add(start_city);
        while (left_number_of_towns > 0) {
            int best_distance = Integer.MAX_VALUE;
            int best_next_city = 0;
            for (int next_city_index = 0; next_city_index < problem.dimension; next_city_index++) {
                if (!city_list.contains(next_city_index)) {
                    int actual_distance = problem.calculateDistance(actual_city_index, next_city_index);
                    if (best_distance > actual_distance) {
                        best_next_city = next_city_index;
                    }
                }
            }
            city_list.add(best_next_city);
            actual_city_index = best_next_city;
            left_number_of_towns--;
        }
        city_list.add(start_city);
        ArrayList<ArrayList<Integer>> item_list = getItemsGreedy(city_list);


        return new Solution(city_list, item_list);
    }

    public static Solution getRandomSolution() { // Propably sould be extracted from this class
        Problem problem = Problem.getProblem();

        int dimension = problem.dimension;
        int number_of_items = problem.number_of_items;
        int items_per_city = number_of_items / (dimension - 1);

        ArrayList<Integer> city_list = new ArrayList<Integer>(dimension + 1); // +1 because last town is same as first
        ArrayList<ArrayList<Integer>> items_list = new ArrayList<ArrayList<Integer>>(dimension);

        for (int i = 0; i < dimension; i++) {
            items_list.add(new ArrayList<>());
        }

        city_list.add(0);

        for (int i =1; i < dimension; i++){
            city_list.add(i);
        }

        Collections.shuffle(city_list);

        city_list.add(city_list.get(0)); // add first city as last

        int current_weight = 0;

        for (int i = 1; i < dimension; i++ ) { // there is no item in town 0 but because of that if we want to know something about item in city X we must take city number X-1
            Random rand = new Random();
            int zero_roll = rand.nextInt(100);
            while (zero_roll >= 92) {
                int next_item = rand.nextInt(items_per_city) * (problem.dimension - 1) + i - 1;
                current_weight += problem.items[next_item][2];
                if (current_weight > problem.capacity_of_knapsack) {
                    current_weight -= problem.items[next_item][2];
//                    System.out.println(current_weight + '/' + problem.capacity_of_knapsack);
                }
                else {
                    if(!items_list.get(i).contains(next_item)) {
                        items_list.get(i).add(next_item);
                    }
                }
                zero_roll = rand.nextInt(10);
            }
        }

        return new Solution(city_list, items_list);
    }

    public static ArrayList<ArrayList<Integer>> getItemsGreedy(ArrayList<Integer> city_list) {
        Problem problem = Problem.getProblem();

        ArrayList<ArrayList<Integer>> item_list = new ArrayList<ArrayList<Integer>>(problem.dimension);

        for (int i = 0; i < problem.dimension; i++) {
            item_list.add(new ArrayList<>(problem.number_of_items_per_city));
        }

        int towns = problem.dimension;
        int number_of_items_in_city = problem.number_of_items_per_city;
        int total_weight = 0;

        // Get value weight-distance ratio of first item to compare first calculated value(s) to something
        double best_item_value_weight_ratio = calculateRatio(0, 0);

        Double capacity_ratio = 2.;

        for (int actual_city_index = 1; actual_city_index < towns; actual_city_index++) {
            if (city_list.get(actual_city_index) != 0) { //there are no items in first (0) city
                for (int item_number = 0; item_number < number_of_items_in_city; item_number++) {
                    int item_id = ((city_list.get(actual_city_index)) -1) + (problem.dimension - 1) * item_number;
                    Double current_ratio = calculateRatio(item_id, actual_city_index);
                    if (current_ratio * capacity_ratio >= best_item_value_weight_ratio) {
                        best_item_value_weight_ratio = current_ratio * capacity_ratio;
                        if (total_weight + problem.items[item_id][2] < problem.capacity_of_knapsack) {
                            item_list.get(city_list.get(actual_city_index)).add(item_id);
                            total_weight += problem.items[item_id][2];
                            capacity_ratio = 2. - (double)total_weight/problem.capacity_of_knapsack;
                        }
                    }
                }
            }
        }
        return item_list;
//                Problem problem = Problem.getProblem();
//
//        ArrayList<ArrayList<Integer>> item_list = new ArrayList<ArrayList<Integer>>();
//
//        for (int i = 0; i < problem.dimension; i++) {
//            item_list.add(new ArrayList<>());
//        }
//
//        int towns = problem.dimension;
//        int number_of_items_in_city = problem.number_of_items_per_city;
//        int total_weight = 0;
//
//        // Get value weight-distance ratio of first item to compare first calculated value(s) to something
//        double best_item_value_weight_ratio = calculateRatioHardAnEasy(0, 0);
//
//        for (int actual_city_index = 1; actual_city_index < towns; actual_city_index++) {
//            if (city_list.get(actual_city_index) != 0) { //there are no items in first (0) city
//                for (int item_number = 0; item_number < number_of_items_in_city; item_number++) {
//                    int item_id = ((city_list.get(actual_city_index)) -1) + (problem.dimension - 1) * item_number;
//                    Double current_ratio = calculateRatioHardAnEasy(item_id, actual_city_index);
//                    Double towns_ratio = Math.pow(1 + (double)actual_city_index / problem.dimension, 1);
//                    Double capacity_ratio = 1.5 - (double)total_weight/problem.capacity_of_knapsack;
//                    Double multiply =  Math.pow(towns_ratio * capacity_ratio, 1);
//
////                    System.out.println("Multiply: " + multiply + " current_ratio" + current_ratio + " result: "+ current_ratio * multiply);
////                    System.out.println("Best value ratio: " + best_item_value_weight_ratio);
//                    if (current_ratio * multiply >= 0.8) {
//                        best_item_value_weight_ratio = current_ratio * multiply / 1.3;
//                        if (total_weight + problem.items[item_id][2] < problem.capacity_of_knapsack) {
//                            item_list.get(city_list.get(actual_city_index)).add(item_id);
//                            total_weight += problem.items[item_id][2];
//                        }
//                    }
//                }
//            }
//        }
//        return item_list;
    }



    private static double calculateRatio(int item_id, int actual_city_index) {
        Problem problem = Problem.getProblem();
        return (double) problem.items[item_id][1] /
                (problem.items[item_id][2] * (problem.dimension - actual_city_index - 1)); // Math.pow(problem.dimension - actual_city_index - 1, 1));
    }

    private static double calculateRatioHardAnEasy(int item_id, int actual_city_index) {
        Problem problem = Problem.getProblem();
        //        System.out.println(ratio);
        return (double) problem.items[item_id][1] /
                (problem.items[item_id][2]);
    }


    public static double calculateOptimalCooling(double start_temp, double end_temp, int iterations, double delta) {
        double cooling = Math.round(Math.pow(end_temp/start_temp, 1.0 / iterations) * (1/delta)) * delta;
        return cooling;
    }

}
