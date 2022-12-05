import java.util.ArrayList;

public class Tester {

    public static void do_EA_test(int repetitions, String problem_name,   int number_of_generations,
    int population_size,
    double mutation_chance,
    double crossover_chance,
    int tour_size ,
    boolean is_ordered_crossover,
    boolean is_swap,
    boolean logs_enabled,
    boolean write_enabled ) {
        MyLogger my_log = new MyLogger();


        ArrayList<Double> ea_solutions = new ArrayList<Double>();



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

    public static void do_tabu_test(int repetitions, String problem_name, int number_of_iterations,
    int number_of_neighbours,
    int tabu_size,
    boolean is_inverse,
    boolean logs_enabled,
    boolean write_enabled ) {
        MyLogger my_log = new MyLogger();


        ArrayList<Double> tabu_solutions = new ArrayList<Double>();



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

    public static void do_SA_test(int repetitions, String problem_name, int number_of_iterations,
    int number_of_neighbours,
    double end_temp,
    double begin_temp ,
    boolean is_inverse,
    boolean logs_enabled,
    boolean write_enabled) {
        MyLogger my_log = new MyLogger();

        ArrayList<Double> SA_solutions = new ArrayList<Double>();



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
