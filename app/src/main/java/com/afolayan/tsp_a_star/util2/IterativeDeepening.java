package com.afolayan.tsp_a_star.util2;

/**
 * Created by Oluwaseyi AFOLAYAN on 4/19/2018.
 */

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Stack;

public class IterativeDeepening {
    private Stack<Integer> stack;
    private int numberOfNodes;
    private int depth;
    private int maxDepth;
    private boolean goalFound = false;

    public IterativeDeepening() {
        stack = new Stack<Integer>();
    }

    public void iterativeDeeping(int adjacencyMatrix[][], int destination) {
        numberOfNodes = adjacencyMatrix[1].length - 1;
        while (!goalFound) {
            depthLimitedSearch(adjacencyMatrix, 1, destination);
            maxDepth++;
        }
        System.out.println("\nGoal Found at depth " + depth);
    }

    public void iterativeDeeping(double adjacencyMatrix[][], int destination) {
        numberOfNodes = adjacencyMatrix[1].length - 1;
        while (!goalFound) {
            depthLimitedSearch(adjacencyMatrix, 1, destination);
            maxDepth++;
        }
        System.out.println("\nGoal Found at depth " + depth);
    }

    private void depthLimitedSearch(int adjacencyMatrix[][], int source, int goal) {
        int element, destination = 1;
        int[] visited = new int[numberOfNodes + 1];
        stack.push(source);
        depth = 0;
        System.out.println("\nAt Depth " + maxDepth);
        System.out.print(source + "\t");

        while (!stack.isEmpty()) {
            element = stack.peek();
            while (destination <= numberOfNodes) {
                if (depth < maxDepth) {
                    if (adjacencyMatrix[element][destination] == 1) {
                        stack.push(destination);
                        visited[destination] = 1;
                        System.out.print(destination + "\t");
                        depth++;
                        if (goal == destination) {
                            goalFound = true;
                            return;
                        }
                        element = destination;
                        destination = 1;
                        continue;
                    }
                } else {
                    break;
                }
                destination++;
            }
            destination = stack.pop() + 1;
            depth--;
        }
    }

    private void depthLimitedSearch(double adjacencyMatrix[][], int source, int goal) {
        int element, destination = 1;
        int[] visited = new int[numberOfNodes + 1];
        stack.push(source);
        depth = 0;
        System.out.println("\nAt Depth " + maxDepth);
        System.out.print(source + "\t");

        while (!stack.isEmpty()) {
            element = stack.peek();
            while (destination <= numberOfNodes) {
                if (depth < maxDepth) {
                    if (adjacencyMatrix[element][destination] == 1) {
                        stack.push(destination);
                        visited[destination] = 1;
                        System.out.print(destination + "\t");
                        depth++;
                        if (goal == destination) {
                            goalFound = true;
                            return;
                        }
                        element = destination;
                        destination = 1;
                        continue;
                    }
                } else {
                    break;
                }
                destination++;
            }
            destination = stack.pop() + 1;
            depth--;
        }
    }

    public static void main(String... arg) {
        int number_of_nodes, destination;
        Scanner scanner = null;
        try {
            System.out.println("Enter the number of nodes in the graph");
            scanner = new Scanner(System.in);
            number_of_nodes = scanner.nextInt();

            int adjacency_matrix[][] = new int[number_of_nodes + 1][number_of_nodes + 1];
            System.out.println("Enter the adjacency matrix");
            for (int i = 1; i <= number_of_nodes; i++)
                for (int j = 1; j <= number_of_nodes; j++)
                    adjacency_matrix[i][j] = scanner.nextInt();


            System.out.println("Enter the destination for the graph");
            destination = scanner.nextInt();

            double[][] am = loadMatrix();
            IterativeDeepening iterativeDeepening = new IterativeDeepening();
            iterativeDeepening.iterativeDeeping(am, destination);
        } catch (InputMismatchException inputMismatch) {
            System.out.println("Wrong Input format");
        }
        scanner.close();
    }

    private static double[][] loadMatrix(){
        return new double[][]{
                {0.0,10749.49,20774.87,53370.48,18600.71,46091.37,47807.0,29191.5,31940.31,30289.48,30645.58,27603.7,33767.57,82264.78,98890.32},
                {10749.49,0.0,13208.96,49787.2,16388.1,38301.12,40445.62,20097.9,22752.02,21246.15,21616.88,18430.71,23231.37,72334.26,89919.14},
                {20774.87,13208.96,0.0,37282.65,9659.39,25476.77,27426.67,8994.48,11678.23,9958.37,10279.37,7685.06,17030.12,62136.67,78128.06},
                {53370.48,49787.2,37282.65,0.0,34833.7,23673.41,20980.19,35725.98,34997.93,35028.6,34811.7,36777.27,44142.76,57397.12,61695.89},
                {18600.71,16388.1,9659.39,34833.7,0.0,29753.87,30864.28,17592.28,19915.8,18272.86,18505.19,16693.64,26599.44,69087.95,83202.09},
                {46091.37,38301.12,25476.77,23673.41,29753.87,0.0,3098.73,18738.42,16571.02,17627.93,17271.84,20358.58,23440.71,40835.96,53449.44},
                {47807.0,40445.62,27426.67,20980.19,30864.28,3098.73,0.0,21266.24,19257.14,20188.01,19843.27,22843.95,26467.19,41261.46,52528.35},
                {29191.5,20097.9,8994.48,35725.98,17592.28,18738.42,21266.24,0.0,2751.62,1151.23,1522.61,1671.95,10157.3,53247.41,69896.89},
                {31940.31,22752.02,11678.23,34997.93,19915.8,16571.02,19257.14,2751.62,0.0,1721.64,1426.95,4357.57,9529.89,50507.18,67194.67},
                {30289.48,21246.15,9958.37,35028.6,18272.86,17627.93,20188.01,1151.23,1721.64,0.0,371.4,2823.18,10215.54,52211.73,68763.02},
                {30645.58,21616.88,10279.37,34811.7,18505.19,17271.84,19843.27,1522.61,1426.95,371.4,0.0,3194.56,10258.25,51877.06,68396.73},
                {27603.7,18430.71,7685.06,36777.27,16693.64,20358.58,22843.95,1671.95,4357.57,2823.18,3194.56,0.0,10306.56,54760.93,71545.08},
                {33767.57,23231.37,17030.12,44142.76,26599.44,23440.71,26467.19,10157.3,9529.89,10215.54,10258.25,10306.56,0.0,49648.3,68824.16},
                {82264.78,72334.26,62136.67,57397.12,69087.95,40835.96,41261.46,53247.41,50507.18,52211.73,51877.06,54760.93,49648.3,0.0,23584.8},
                {98890.32,89919.14,78128.06,61695.89,83202.09,53449.44,52528.35,69896.89,67194.67,68763.02,68396.73,71545.08,68824.16,23584.8,0.0}
        };
    }
}