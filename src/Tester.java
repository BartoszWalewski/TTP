import java.util.ArrayList;

public class Tester {

    public static void do_EA_test(int repetitions, String problem_name) {
        MyLogger my_log = new MyLogger();


        ArrayList<Double> ea_solutions = new ArrayList<Double>();

        int number_of_generations = 10000;
        int population_size = 100;
        double mutation_chance = 0.01;
        double crossover_chance = 0.7;
        int tour_size = 10;
        boolean is_ordered_crossover = true;
        boolean is_swap = true;
        boolean logs_enabled = false;
        boolean write_enabled = false;

        my_log.initiateLogger(problem_name + "EA_10_times_" + "number_of_generations " + number_of_generations +
                "population_size " + population_size +
                "mutation_chance " + mutation_chance +
                "crossover_chance " + crossover_chance +
                "tour_size " + tour_size +
                "is_ordered_crossover " + is_ordered_crossover +
                "is_swap " + is_swap, 50);

//        my_log.addMessage(new String[]{"number_of_generations " + number_of_generations});
//        my_log.addMessage(new String[]{"population_size " + population_size});
//        my_log.addMessage(new String[]{"mutation_chance " + mutation_chance});
//        my_log.addMessage(new String[]{"crossover_chance " + crossover_chance});
//        my_log.addMessage(new String[]{"tour_size " + tour_size});
//        my_log.addMessage(new String[]{"is_ordered_crossover " + is_ordered_crossover});
//        my_log.addMessage(new String[]{"is_swap " + is_swap});
//        my_log.addMessage(new String[]{"logs_enabled " + logs_enabled});
//        my_log.addMessage(new String[]{"write_enabled " + write_enabled});

        while (repetitions > 0) {
            EA easy = new EA(number_of_generations, population_size, mutation_chance, crossover_chance, tour_size,
                    is_ordered_crossover, is_swap, logs_enabled, write_enabled);
            ea_solutions.add(easy.do_algorithm());
            my_log.addMessage((new String[]{ea_solutions.get(ea_solutions.size()-1).toString()}));
            repetitions --;
        }

        my_log.writeAll();
        my_log.close();
    }

    public static void do_greedy(String problem_name) {
        MyLogger my_log = new MyLogger();
        my_log.initiateLogger("greedy " + problem_name, 50);
        Problem problem = Problem.getProblem();
        Solution best_solution = Tools.getGreedySolution(0);
        for (int i = 1; i < problem.dimension; i++) {
            Solution current_solution = Tools.getGreedySolution(i);
            if (current_solution.instance_score > best_solution.instance_score) {
                best_solution = current_solution;
            }
            my_log.addMessage(new String[] {String.valueOf(current_solution.instance_score)});
        }
        my_log.writeAll();
        my_log.close();
    }

    public static void do_random(String problem_name) {
        MyLogger my_log = new MyLogger();
        my_log.initiateLogger("random 10k " + problem_name, 50);
        Problem problem = Problem.getProblem();
        Solution best_solution = Tools.getRandomSolution();
        for (int i = 1; i < 10000; i++) {
            Solution current_solution = Tools.getRandomSolution();
            if (current_solution.instance_score > best_solution.instance_score) {
                best_solution = current_solution;
            }
            my_log.addMessage(new String[] {String.valueOf(current_solution.instance_score)});
        }
        my_log.writeAll();
        my_log.close();
    }

    public static void do_tabu_test(int repetitions, String problem_name) {
        MyLogger my_log = new MyLogger();


        ArrayList<Double> tabu_solutions = new ArrayList<Double>();

        int number_of_iterations = 25000;
        int number_of_neighbours = 200;
        int tabu_size = 5000;
        boolean is_inverse = false;
        boolean logs_enabled = false;
        boolean write_enabled = false;

        my_log.initiateLogger(problem_name + "_Tabu_10_times_with_" + number_of_iterations + "_iterations_" +
                number_of_neighbours + "_neighbours_" + tabu_size + "_tabu_list_inverse_" + is_inverse , 50);

        while (repetitions > 0) {
            TabuSearch problem = new TabuSearch(number_of_iterations, number_of_neighbours, tabu_size, is_inverse, logs_enabled, write_enabled);
            tabu_solutions.add(problem.do_metaheuristic());
            my_log.addMessage((new String[]{tabu_solutions.get(tabu_solutions.size()-1).toString()}));
            repetitions --;
        }

        my_log.writeAll();
        my_log.close();
    }

    public static void do_SA_test(int repetitions, String problem_name) {
        MyLogger my_log = new MyLogger();

        ArrayList<Double> SA_solutions = new ArrayList<Double>();

        int number_of_iterations = 10000;
        int number_of_neighbours = 10;
        double end_temp = 10;
        double begin_temp = 3500;
        boolean is_inverse = false;
        boolean logs_enabled = false;
        boolean write_enabled = false;

        my_log.initiateLogger(problem_name + "_SA_10_times_with_" + number_of_iterations + "_iterations_" +
                number_of_neighbours + "_neighbours_" + begin_temp + "begin_temperature"
                + end_temp + "end_temperature" + is_inverse , 50);

        while (repetitions > 0) {
            SA problem = new SA(number_of_iterations, number_of_neighbours, begin_temp, end_temp,
                    Tools.calculateOptimalCooling(begin_temp, end_temp, number_of_iterations, 0.000001),
                    is_inverse, logs_enabled, write_enabled);
            SA_solutions.add(problem.do_metaheuristic());
            my_log.addMessage((new String[]{SA_solutions.get(SA_solutions.size()-1).toString()}));
            repetitions --;
        }

        my_log.writeAll();
        my_log.close();
    }
}
