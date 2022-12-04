import java.util.ArrayList;
import java.util.Arrays;

public class TabuSearch {
    private Solution best;
    private Solution current;
    private ArrayList<Solution> tabu_list;
    private int number_of_iterations;
    private int number_of_neighbours;
    private int tabu_length;
    private MyLogger my_log;
    boolean logs_enabled;
    boolean write_enabled;

    boolean is_inverse;

    public TabuSearch(int number_of_iterations, int number_of_neighbours, int tabu_length,
                      boolean is_inverse, boolean logs_enabled, boolean write_enabled) {
        this.current = Tools.getRandomSolution();
        this.best = new Solution(this.current);
        this.tabu_list = new ArrayList<>(tabu_length);
        //po rpostu arraylist i sobie potem iterować zmienną i ją modulo tabu_length
        this.number_of_iterations = number_of_iterations;
        this.number_of_neighbours = number_of_neighbours;
        this.tabu_length = tabu_length;
        this.logs_enabled = logs_enabled;
        this.write_enabled  = write_enabled;
        this.is_inverse = is_inverse;
        if (this.logs_enabled) {
            my_log = new MyLogger();
            my_log.initiateLogger(Problem.getProblem().problem_name + "_tabu", 50);
        }

    }
    public double do_metaheuristic() {
        int tabu_index = 1;
        tabu_list.add(current);
        for (int iteration_number = 0; iteration_number < number_of_iterations; iteration_number++) {
            Solution[] neighbour_list = new Solution[number_of_neighbours];
            ArrayList<Integer> already_in_tabu = new ArrayList<Integer>(number_of_neighbours);
            for (int neighbour_number = 0; neighbour_number < number_of_neighbours; neighbour_number++) {
                neighbour_list[neighbour_number] = current.find_neighbour(is_inverse);
                neighbour_list[neighbour_number].item_list = Tools.getItemsGreedy(neighbour_list[neighbour_number].city_list);
                neighbour_list[neighbour_number].evaluate();
            }
            boolean is_found = false;
            int counter = 0;
            int best_neighbour = 0;
            double best_neighbour_score = - Double.MAX_VALUE;
            while (!is_found && counter < this.number_of_neighbours) {
                best_neighbour_score = - Double.MAX_VALUE;
                best_neighbour = 0;
                for (int neighbour_number = 1; neighbour_number < number_of_neighbours; neighbour_number++) {
                   if (neighbour_list[neighbour_number].instance_score > best_neighbour_score) {
                        if (!already_in_tabu.contains(best_neighbour)) {
                            best_neighbour = neighbour_number;
                            best_neighbour_score = neighbour_list[neighbour_number].instance_score;
                        }
                    }
                }
                if (!check_tabu(neighbour_list[best_neighbour])) {
                    is_found = true;
                }
                else {
                    already_in_tabu.add(best_neighbour);
                    counter ++;
                }
            }


            if (!is_found) {
                int worst_neighbour = 0;
                for (int neighbour_number = 1; neighbour_number < number_of_neighbours; neighbour_number++) {
                    if (neighbour_list[neighbour_number].instance_score < neighbour_list[best_neighbour].instance_score) {
                        worst_neighbour = neighbour_number;
                    }
                }
                Arrays.sort(neighbour_list);
                current = neighbour_list[neighbour_list.length/2]; //.find_neighbour(!is_inverse);
//                chosen_neighbour_index = 0; //neighbour_list.length - 1;
                //Aspiration - if all neighbours in tabu list choose worse solution as it has the biggest chance to escape local max
            }
            else {
                current = neighbour_list[best_neighbour];
            }

            tabu_list.add(tabu_index, current);
            tabu_index = (tabu_index + 1) % tabu_length;

//            //items are assigned to city and city order is changed during mutation so the items can be now not optimally assigned
//            current.item_list = Tools.getItemsGreedy(current.city_list);
//            current.evaluate();
            if (current.instance_score > best.instance_score) {
                best = new Solution(current);
            }



            if (this.logs_enabled) {
                my_log.addMessage(new String[]{Double.toString(current.instance_score), Double.toString(best.instance_score)});
            }
            if (write_enabled) {
                System.out.println("current: " + current.instance_score + " best " + best.instance_score);
            }
        }
        if (logs_enabled) {
            my_log.writeAll();
            my_log.close();
        }
        return this.best.instance_score;
    }

    private boolean check_tabu(Solution neighbour) {
        for (int checked_tabu_index = 0; checked_tabu_index < tabu_list.size(); checked_tabu_index++) {
            if (this.tabu_list.get(checked_tabu_index).equals(neighbour)) {
                return true;
            }
        }
        return false;
    }

}
