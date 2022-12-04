import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class SA {

    private Solution best;
    private Solution current;
    private int number_of_iterations;
    private int number_of_neighbours;
    double begin_temperature;
    double end_temperature;
    double cooling_insensitivity;
    private MyLogger my_log;
    boolean is_inverse;
    boolean logs_enabled;
    boolean write_enabled;

    double current_temperature;

    public SA(int number_of_iterations, int number_of_neighbours, double begin_temperature, double end_temperature,
              double cooling_insensitivity, boolean is_inverse, boolean logs_enabled, boolean write_enabled) {
        this.current = Tools.getRandomSolution();
        this.best = new Solution(this.current);
        this.begin_temperature = begin_temperature;
        this.end_temperature = end_temperature;
        this.current_temperature = begin_temperature;
        this.cooling_insensitivity = cooling_insensitivity;
        this.number_of_iterations = number_of_iterations;
        this.number_of_neighbours = number_of_neighbours;
        this.logs_enabled = logs_enabled;
        this.write_enabled  = write_enabled;
        this.is_inverse = is_inverse;
        if (this.logs_enabled) {
            my_log = new MyLogger();
            my_log.initiateLogger(Problem.getProblem().problem_name + "_SA", 50);
        }

    }
    public double do_metaheuristic() {
        for (int iteration_number = 0; iteration_number < number_of_iterations; iteration_number++) {
            Solution best_neighbour = current.find_neighbour(is_inverse);;
            for (int neighbour_number = 1; neighbour_number < number_of_neighbours; neighbour_number++) {
                Solution new_neighbour = current.find_neighbour(is_inverse);
                new_neighbour.item_list = Tools.getItemsGreedy(new_neighbour.city_list);
                new_neighbour.evaluate();
                if (new_neighbour.instance_score > best_neighbour.instance_score) {
                    best_neighbour = new_neighbour;
                }
            }
            if (best_neighbour.instance_score > current.instance_score) {
                current = best_neighbour;
            }
             else {
                Random random = new Random();
                if (random.nextDouble() < Math.exp((best_neighbour.instance_score - current.instance_score)/current_temperature)) {
                    current = best_neighbour;
                }
            }
            if (current.instance_score > best.instance_score) {
                best = new Solution(current);
            }

            current_temperature *= cooling_insensitivity;
            if (current_temperature < end_temperature) {
                current_temperature = end_temperature;
            }

            //items are assigned to city and city order is changed during mutation so the items can be now not optimally assigned
//            current.item_list = Tools.getItemsGreedy(current.city_list);
//            current.evaluate();


            if (this.logs_enabled) {
                my_log.addMessage(new String[]{Double.toString(current.instance_score), Double.toString(best.instance_score)});
            }


        }
        if (logs_enabled) {
            my_log.writeAll();
            my_log.close();
        }
        return this.best.instance_score;
    }
}
