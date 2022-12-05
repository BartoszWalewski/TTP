public class App {
    public static void main(String[] args) {
        String problem_name = "hard_4";
        Problem tppProblem = new Problem("/home/olsenolseno/TTP/student/" + problem_name + ".ttp");
//        Problem tppProblem = new Problem("C:/Users/Bartek/Downloads/ai-lab1-ttp_data/student/" + problem_name + ".ttp");

//        EA easy = new EA(20000, 200,
//                0.02, 0.7 , 15,
//                true, false, false,
//                false);
//        easy.do_algorithm();
//        EA easy1 = new EA(1000, 100,
//                0.013, 0.7 , 5,
//                true, true, true,
//                true);
//        easy1.do_algorithm();EA easy2 = new EA(1000, 100,
//                0.015, 0.7 , 5,
//                true, true, true,
//                true);
//        easy2.do_algorithm();EA easy3 = new EA(1000, 100,
//                0.018, 0.7 , 5,
//                true, true, true,
//                true);
//        easy3.do_algorithm();EA easy4 = new EA(1000, 100,
//                0.02, 0.7 , 5,
//                true, true, true,
//                true);
//        easy4.do_algorithm();
//        TabuSearch ts = new TabuSearch(35000,
//                150, 5000, false,
//                true, false);
//        ts.do_metaheuristic();
//
//        SA sa = new SA(5000, 75,
//                5000, 50,
//                Tools.calculateOptimalCooling(5000,
//                        50, 5000, 0.000001),
//                false, true, false);
//        sa.do_metaheuristic();
//        Solution test1 = Tools.getRandomSolution();
//        Solution test2 = Tools.getRandomSolution();
//        Solution[] test3 = Solution.partiallyMatchedCrossover(test1, test2);
//        Solution test4 = new Solution(test2);
//        test4.mutateGeneSwap(0.5);
//        System.out.println();
//        Tester.do_greedy(problem_name);
//        Tester.do_random(problem_name);
//        Tester.do_tabu_test(10, problem_name);
//        System.out.println(Tools.calculateOptimalCooling(4000, 5, 10000, 0.000001));
//    Tester.do_EA_test(10, problem_name, 20000, 200, 0.25, 0.7, 50, true, true, false, false);
    Tester.do_tabu_test(10, problem_name, 35000, 150, 5000, false, false, false);
    Tester.do_tabu_test(10, problem_name, 35000, 150, 10000, false, false, false);
    Tester.do_tabu_test(10, problem_name, 35000, 200, 5000, false, false, false);
    Tester.do_tabu_test(10, problem_name, 35000, 200, 10000, false, false, false);
    Tester.do_tabu_test(10, problem_name, 35000, 100, 5000, false, false, false);
    Tester.do_SA_test(10, problem_name, 45000, 75, 50, 5000, false, false, false);
    Tester.do_SA_test(10, problem_name, 45000, 100, 50, 5000, false, false, false);
    Tester.do_SA_test(10, problem_name, 45000, 75, 25, 5000, false, false, false);
    Tester.do_SA_test(10, problem_name, 45000, 75, 50, 10000, false, false, false);
    Tester.do_SA_test(10, problem_name, 45000, 75, 100, 15000, false, false, false);
//        Tester.do_greedy(problem_name);
    }
}
