import java.util.Random;

public class EA {
    private Solution[] population;
    private Solution actual_best_solution;
    private int number_of_generations;
    private int population_size;
    private double mutation_chance;
    private double crossover_chance;
    private int tour_size;
    private Solution best_soultion;
    private boolean is_ordered_crossover;
    private boolean is_swap;

    private boolean logs_enabled;
    private boolean write_enabled;

    private MyLogger my_log;

    public EA(int number_of_generations, int population_size, double mutation_chance, double crossover_chance,
              int tour_size, boolean is_ordered_crossover, boolean is_swap, boolean logs_enabled, boolean write_enabled) {
        this.number_of_generations = number_of_generations;
        this.population_size = population_size;
        this.mutation_chance = mutation_chance;
        this.crossover_chance = crossover_chance;
        this.tour_size = tour_size;
        this.is_ordered_crossover = is_ordered_crossover;
        this.is_swap = is_swap;
        this.logs_enabled = logs_enabled;
        this.write_enabled = write_enabled;
        if (this.logs_enabled) {
            my_log = new MyLogger();
            my_log.initiateLogger(Problem.getProblem().problem_name, 50);
        }

        this.population = new Solution[population_size];

        for (int i = 0; i < population_size; i++) {
            this.population[i] = Tools.getRandomSolution();
//            this.population[i].item_list = Tools.getItemsGreedy(this.population[i].city_list);
        }
        this.best_soultion = new Solution(this.population[0]);
    }

    public double do_algorithm() {
        for(int generation_number = 0; generation_number < this.number_of_generations; generation_number++) {

            Solution[] new_population = new Solution[this.population_size];
            if (this.write_enabled) {
                System.out.println("Populacja "+ generation_number);
            }
            for(int specimen_number = 0; specimen_number < this.population_size; specimen_number++) {
                Random rand = new Random();
                Solution child;
                Solution child2 = null;
                int mother_index = selection();
                if (rand.nextDouble() < this.crossover_chance) {
                    int father_index = selection();
                    Solution[] children = crossover(mother_index, father_index, this.is_ordered_crossover);
                    child = children[0];
                    if (!this.is_ordered_crossover){
                        child2 = children[1];
                    }
                }
                else {
                    child = new Solution(this.population[mother_index]);
                }
                //if (rand.nextDouble() < this.mutation_chance) {
                    child = mutation(child, is_swap);
                    //Todo now it makes a deep copy - waste of everything
                    //Todo implement mutation change
                //}
                if (!this.is_ordered_crossover && child2 != null) {
//                    if (rand.nextDouble() < this.mutation_chance) {
                        child2 = mutation(child2, is_swap);
//                    }
                }

                new_population[specimen_number] = child;

                if(!this.is_ordered_crossover && child2 != null) {
                    if(specimen_number < this.population_size - 1) {
                        specimen_number++;
                        new_population[specimen_number] = child2;
                    }
                    else {
                        child2 = null;
                    }
                }
                if (child.instance_score > this.best_soultion.instance_score) {
                    this.best_soultion = new Solution(child);
                }
                if(!this.is_ordered_crossover && child2 != null) {
                    if (child2.instance_score > this.best_soultion.instance_score) {
                        this.best_soultion = new Solution(child2);
                    }
                }

            }
            this.population = new_population;

            Problem problem = Problem.getProblem();
            if (this.write_enabled) {
                System.out.println(this.best_soultion.instance_score + " ");
                System.out.println(best_soultion.total_weight + "/" + problem.capacity_of_knapsack);
            }

            double sum = 0;
            double worst = Double.MAX_VALUE;
            //double best = - Double.MAX_VALUE;

            for (int solution_number = 0; solution_number < this.population_size; solution_number++) {
                double current_score = this.population[solution_number].instance_score;
                sum += current_score;
//                if (best < current_score) {
//                    best = current_score;
//                }
                if(worst > current_score) {
                    worst = current_score;
                }

            }
            if (this.logs_enabled) {
                my_log.addMessage(new String[]{Double.toString(worst), Double.toString(best_soultion.instance_score), Double.toString(sum/this.population_size)});
            }

            if (this.write_enabled) {
                for (int i = 0; i < problem.dimension + 1; i++) {
                    System.out.print(best_soultion.city_list.get(i));
                    System.out.print(" Items: ");
                    for (int j=0; j<best_soultion.item_list.get(best_soultion.city_list.get(i)).size(); j++) {
                        System.out.print(best_soultion.item_list.get(best_soultion.city_list.get(i)).get(j));
                        System.out.print(" ");
                    }
                    System.out.print('\n');
                }
            }
        }
        if (logs_enabled) {
            my_log.writeAll();
            my_log.close();
        }
        return this.best_soultion.instance_score;
    }

    private int selection() {
        if (this.tour_size == 0) {
            return  roulette();
        }
        else {
            return tournament();
        }
    }

    private int roulette() {
        double sum = 0.0;
        double worst = Double.MAX_VALUE;

        for (Solution solution: this.population) {
            double score = solution.instance_score/10000;
            sum += score;
            if (worst > score) {
                worst = score;
            }
        }

        double[] chance = new double[this.population_size];

        sum = sum - (worst * this.population_size);

        double current_chance = 0;

        Random random = new Random();

        double choice = random.nextDouble();

        for (int i = 0; i < this.population_size; i++) {
            current_chance += (this.population[i].instance_score/10000 - worst)/sum;
            chance[i] = current_chance;
            if (choice < current_chance) {
                return i;
            }
        }

       return 0;
    }

    private int tournament() {
        Random rand = new Random();
        int best_participant = rand.nextInt(this.population_size);
        int actual_parcipant;
        for (int participant_number = 1; participant_number < this.tour_size; participant_number++) {
            actual_parcipant = rand.nextInt(this.population_size);
            if(this.population[actual_parcipant].instance_score > this.population[best_participant].instance_score) {
                best_participant = actual_parcipant;
            }
        }
        return best_participant;
    }

    private Solution[] crossover(int mother_index, int father_index, boolean if_easy){
        Solution[] children;
        if (if_easy) {
            children = new Solution[1];
            children[0] = this.population[mother_index].orderedCrossover(this.population[father_index]);
        }
        else {
            children = Solution.partiallyMatchedCrossover(this.population[mother_index], this.population[father_index]);
        }
        return children;
    }

    private Solution mutation(Solution solution, boolean is_swap) {
        if (is_swap) {
            Random rand = new Random();
            if (rand.nextDouble() > this.mutation_chance) {
                solution = solution.mutateSwap();
            }
            //solution = solution.mutateGeneSwap(this.mutation_chance);
        }
        else {
            solution = solution.mutateInverse();
        }
        solution.item_list = Tools.getItemsGreedy(solution.city_list);
        solution.evaluate();
        return solution;
    }
}
