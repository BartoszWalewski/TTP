import java.io.*;

public class Problem {

    private static Problem problem = null;

    public String problem_name;
    public String knapsack_data_type;
    public int dimension;
    public int number_of_items;
    public int capacity_of_knapsack;
    public double min_speed;
    public double max_speed;
    public double ranting_ratio;

    public int number_of_items_per_city;
    public String edge_weight_type;
    public Double[][] nodes_coords;
    public int[][] items;

    public Problem(String file_path) {
        BufferedReader file_reader;
        try {
            file_reader = new BufferedReader(new FileReader(file_path));
            String line = file_reader.readLine();
            String[] parts = line.split(":");
            problem_name = parts[1].trim();

            line = file_reader.readLine();
            parts = line.split(":");
            knapsack_data_type = parts[1].trim();

            line = file_reader.readLine();
            parts = line.split(":");
            dimension = Integer.parseInt(parts[1].trim());

            line = file_reader.readLine();
            parts = line.split(":");
            number_of_items = Integer.parseInt(parts[1].trim());

            line = file_reader.readLine();
            parts = line.split(":");
            capacity_of_knapsack = Integer.parseInt(parts[1].trim());

            line = file_reader.readLine();
            parts = line.split(":");
            min_speed = Double.parseDouble(parts[1].trim());

            line = file_reader.readLine();
            parts = line.split(":");
            max_speed = Double.parseDouble(parts[1].trim());

            line = file_reader.readLine();
            parts = line.split(":");
            ranting_ratio = Double.parseDouble(parts[1].trim());

            line = file_reader.readLine();
            parts = line.split(":");
            edge_weight_type = parts[1].trim();

            int node_counter = 0;
            int item_counter = 0;

            nodes_coords = new Double[dimension][3];
            items = new int[number_of_items][5];

            line = file_reader.readLine();

            while (line != null && node_counter < dimension) {
                line = file_reader.readLine();
                parts = line.split("\t");
                nodes_coords[node_counter][0] = Double.parseDouble(parts[0]);
                nodes_coords[node_counter][1] = Double.parseDouble(parts[1]);
                nodes_coords[node_counter][2] = Double.parseDouble(parts[2]);
                node_counter++;
            }

            line = file_reader.readLine();

            while (line != null && item_counter < number_of_items) {
                line = file_reader.readLine();
                parts = line.split("\t");
                items[item_counter][0] = Integer.parseInt(parts[0]);
                items[item_counter][1] = Integer.parseInt(parts[1]);
                items[item_counter][2] = Integer.parseInt(parts[2]);
                items[item_counter][3] = Integer.parseInt(parts[3]);
                items[item_counter][4] = items[item_counter][1]/items[item_counter][2];
                item_counter++;
            }

            file_reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        problem = this;
        number_of_items_per_city = this.number_of_items / (this.dimension - 1);

    }

    public static Problem getProblem() {
        if (problem == null) {
            return null;
        }
        return problem;
    }

    public int calculateDistance(int i, int j) {
        return (int) Math.ceil(Math.sqrt(Math.pow(this.nodes_coords[i][1] - this.nodes_coords[j][1], 2) +
                Math.pow(this.nodes_coords[i][2] - this.nodes_coords[j][2], 2)));
    }
}
