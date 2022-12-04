import java.util.*;

public class Solution implements Comparable<Solution> {
    public ArrayList<Integer> city_list;
    public ArrayList<ArrayList<Integer>> item_list;
    public int total_profit;
    public double total_time;
    public int total_distance;
    public int total_weight;
    public double instance_score;

    public Solution (ArrayList<Integer> city_list, ArrayList<ArrayList<Integer>> item_list) {
        this.city_list = city_list;
        this.item_list = item_list;
        this.evaluate();
    }

    public Solution(Solution another) {
        Problem problem = Problem.getProblem();
        this.total_profit = another.total_profit;
        this.total_time = another.total_time;
        this.total_distance = another.total_distance;
        this.total_weight = another.total_weight;
        this.instance_score = another.instance_score;

        this.city_list = new ArrayList<Integer>(problem.dimension + 1);
        this.item_list = new ArrayList<ArrayList<Integer>>(problem.dimension + 1);

        for (Integer city: another.city_list) {
            this.city_list.add(city);
        }

        for (int index = 0; index < another.item_list.size(); index++ ) {
            this.item_list.add(new ArrayList<Integer>(problem.number_of_items_per_city));
            for (int inner_index = 0; inner_index < another.item_list.get(index).size(); inner_index++) {
                this.item_list.get(index).add(another.item_list.get(index).get(inner_index));
            }
        }
        //this.evaluate();
    }

    public void evaluate() {
        Problem problem = Problem.getProblem();

        int capacity_of_knapsack = problem.capacity_of_knapsack;
        double v_min = problem.min_speed;
        double v_max = problem.max_speed;
        double v = (v_max - v_min) / capacity_of_knapsack;

        this.total_weight = 0;
        this.total_profit = 0;
        this.total_distance = problem.calculateDistance(this.city_list.get(0), this.city_list.get(0));
        this.total_time = this.total_distance / (v_max - v * this.total_weight);

        for (int i = 0; i < this.city_list.size() - 1; ++i) {
            int current_city = this.city_list.get(i);
            if(current_city != 0) {
//                current_city -= 1;// subtract 1 as this must be complicated more and array starts from 0, but numbers from 1 - this is only for getting items info
                for (int item_index = 0; item_index < item_list.get(current_city).size(); item_index++) {
                    this.total_weight += problem.items[this.item_list.get(current_city).get(item_index)][2];
                    this.total_profit += problem.items[this.item_list.get(current_city).get(item_index)][1];
                }
            }
            int distance = problem.calculateDistance(this.city_list.get(i), this.city_list.get(i + 1));  // this.city_list[(i + 1) % (this.city_list.length - 1)]);
            this.total_distance += distance;
            double act_v = (v_max - v * this.total_weight);
            if(this.total_weight > capacity_of_knapsack) {
                System.out.println("jsakhdgjfhaijsl;dlfhkcgyhajksdjcgykuaiwklejfghcjiadsknchkhajlskdcgahskjdmncbhjgUYHSD");
            }
            this.total_time = this.total_time + (distance / act_v);
        }
        this.instance_score = this.total_profit - this.total_time;
    }

    public Solution mutateSwap(){
        Problem problem = Problem.getProblem();
        //Solution mutated_solution = new Solution(this);
        Random rand = new Random();
        int first_city_index = rand.nextInt(problem.dimension - 1) + 1; // -1 +1 in order not to choose start city
        int second_city_index = rand.nextInt(problem.dimension - 1) + 1;
        //Not checking if first_city_index == second_city_index because it isn't so important - it just lowers true probability of mutation a little
        Collections.swap(this.city_list, first_city_index, second_city_index);
        this.evaluate();
        return this;
    }

    public Solution mutateInverse() {
        Problem problem = Problem.getProblem();
        //Solution mutated_solution = new Solution(this);
        Random rand = new Random();
        int first_city_index = rand.nextInt(problem.dimension - 1) + 1; // -1 +1 in order not to choose start city
        int second_city_index = rand.nextInt(problem.dimension - 1) + 1;
        if (second_city_index < first_city_index) {
            int temp = first_city_index;
            first_city_index = second_city_index;
            second_city_index = temp;
        }
        //Not checking if first_city_index == second_city_index because it isn't so important - it just lowers true probability of mutation a little and only if we choose middle city
        for (int i = 0; second_city_index - first_city_index > 2 * i; i++) {
            Collections.swap(this.city_list, first_city_index + i, second_city_index - i);
        }
        this.evaluate();
        return this;
    }

    public Solution orderedCrossover(Solution other_parent) {
        Problem problem = Problem.getProblem();
        Random rand = new Random();
        int first_city_index = rand.nextInt(problem.dimension);
        int second_city_index = rand.nextInt(problem.dimension);
        if (second_city_index < first_city_index) {
            int temp = first_city_index;
            first_city_index = second_city_index;
            second_city_index = temp;
        }
        Integer[] child_city_list = new Integer[problem.dimension];

        for (int actual_index = first_city_index; actual_index <= second_city_index; actual_index++) {
            child_city_list[actual_index] = this.city_list.get(actual_index);
        }

        int first_free_index = 0;


        for (int other_parent_index = 0; other_parent_index < problem.dimension; other_parent_index++) {
            if (first_city_index == first_free_index) {
                first_free_index = second_city_index + 1;
            }
            if (!Arrays.asList(child_city_list).contains(other_parent.city_list.get(other_parent_index))) {
                child_city_list[first_free_index] = other_parent.city_list.get(other_parent_index);
                first_free_index++;
            }
        }
        ArrayList<Integer> array_list_cities = new ArrayList<Integer>(Arrays.asList(child_city_list));
        array_list_cities.add(array_list_cities.get(0));
        return new Solution(array_list_cities, Tools.getItemsGreedy(array_list_cities));
    }

    public static Solution[] partiallyMatchedCrossover(Solution first_parent, Solution second_parent) {
        Problem problem = Problem.getProblem();
        Random rand = new Random();
        int first_city_index = rand.nextInt(problem.dimension);
        int second_city_index = rand.nextInt(problem.dimension);
        if (second_city_index < first_city_index) {
            int temp = first_city_index;
            first_city_index = second_city_index;
            second_city_index = temp;
        }
        Integer[] first_child_city_list = new Integer[problem.dimension];
        Integer[] second_child_city_list = new Integer[problem.dimension];
        Integer[] replacement1 = new Integer[problem.dimension];
        Integer[] replacement2 = new Integer[problem.dimension];

        Arrays.fill(replacement1, -1);
        Arrays.fill(replacement2, -1);

        for (int i = first_city_index; i <= second_city_index; i++) {
            first_child_city_list[i] = second_parent.city_list.get(i);
            second_child_city_list[i] = first_parent.city_list.get(i);

            replacement1[second_parent.city_list.get(i)] = first_parent.city_list.get(i);
            replacement2[first_parent.city_list.get(i)] = second_parent.city_list.get(i);
        }

        // fill in remaining slots with replacements
        for (int i = 0; i < problem.dimension; i++) {
            if ((i < first_city_index) || (i > second_city_index)) {
                int n1 = first_parent.city_list.get(i);
                int m1 = replacement1[n1];

                int n2 = second_parent.city_list.get(i);
                int m2 = replacement2[n2];

                while (m1 != -1) {
                    n1 = m1;
                    m1 = replacement1[m1];
                }

                while (m2 != -1) {
                    n2 = m2;
                    m2 = replacement2[m2];
                }

                first_child_city_list[i] = n1;
                second_child_city_list[i] = n2;
            }
        }


        ArrayList<Integer> first_array_list_cities = new ArrayList<Integer>(Arrays.asList(first_child_city_list));
        first_array_list_cities.add(first_array_list_cities.get(0));

        ArrayList<Integer> second_array_list_cities = new ArrayList<Integer>(Arrays.asList(second_child_city_list));
        second_array_list_cities.add(second_array_list_cities.get(0));
        return new Solution[] {new Solution(first_array_list_cities, Tools.getItemsGreedy(first_array_list_cities)), new Solution(second_array_list_cities, Tools.getItemsGreedy(second_array_list_cities)) };
    }

    public Solution find_neighbour(boolean is_inverse) {
        Solution neighbour = new Solution(this);
        if (is_inverse) {
            neighbour.mutateInverse();
        }
        else {
            neighbour.mutateSwap();
        }
        return neighbour;
    }

    public double getInstance_score() {
        return instance_score;
    }

    @Override
    public int compareTo(Solution o) {
        return Double.compare(this.instance_score, (o.instance_score));
    }

    @Override
    public boolean equals(Object obj) {
        return this.city_list.equals(((Solution)obj).city_list);
    }

    public Solution mutateGeneSwap(double mutation_chance){
        Problem problem = Problem.getProblem();
        Random rand = new Random();
        boolean is_swapped = false;
        for (int actual_city = 0; actual_city < problem.dimension; actual_city++) {
            if (mutation_chance > rand.nextDouble()) {
                int second_city_index = rand.nextInt(problem.dimension - 1) + 1;
                Collections.swap(this.city_list, actual_city, second_city_index);
                is_swapped = true;
            }
        }
        if (is_swapped) {
            this.item_list = Tools.getItemsGreedy(this.city_list);
            this.evaluate();
        }
        return this;
    }

//    public boolean checkIfCityIsEmpty(int city_number) {
//        return this.item_list.get(this.city_list.get(city_number)).size() == 0;
//    }
}
