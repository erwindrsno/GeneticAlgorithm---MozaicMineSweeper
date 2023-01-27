import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

//Komponen
// Population size: 80
// Chromosome size: 25
//

public class Main {
    public static void main(String[] args) {
        File inputFile = new File("D://MozaicMineSweeper/src/Input.txt");
        ArrayList<String> board = new ArrayList<>();
        ArrayList<Integer> listTarget = new ArrayList<>();

        try {
            Scanner sc = new Scanner(inputFile);
            while(sc.hasNextLine()){
                String[] arr = sc.nextLine().split(",");
                for (int i = 0; i < arr.length; i++) {
                    board.add(arr[i]);
                }
            }

            //panjang papan dalam 2 dimensi
            int boardLength = (int) Math.sqrt(board.size());

            //instansiasi beberapa array untuk kepentingan pencarian target yang akan dijadikan global maksimum
            //ditambah 2 pada masing" panjang dan lebar untuk mencegah error/exception out of bounds
            int[][] arrBoard = new int[boardLength+2][boardLength+2];
            String[][] arrBoardCheck = new String[boardLength+2][boardLength+2];
            int[][] answer = new int[boardLength+2][boardLength+2];

            //inisialisasi variabel idx untuk iterasi array assignment
            int idx = 0;

            //pre-assign board untuk diproses dalam objek solver
            for (int i = 0; i < arrBoard.length; i++) {
                for (int j = 0; j < arrBoard.length; j++) {
                    //jika i dan j berada di luar kotak
                    if(i == 0 || i == arrBoard.length-1 || j == 0 || j == arrBoard.length-1){
                        //maka diassign dengan -1
                        arrBoard[i][j] = -1;
                    }
                    //jika iterasi berada di dalam kotak
                    else if(i !=0 || i != arrBoard.length-1 || j != 0 || j != arrBoard.length-1){
                        if(!board.get(idx).equals("*")){
                            arrBoard[i][j] = Integer.parseInt(board.get(idx));
                        }
                        else{
                            arrBoard[i][j] = -1;
                        }
                        idx++;
                    }
                    arrBoardCheck[i][j] = "bisajadi";
                }
            }

            Solver solver = new Solver(arrBoard, arrBoardCheck, answer, (int)Math.pow(boardLength,2));
            solver.begin();

            int[] arrTarget = solver.convertToArray();
            for (int i = 0; i < arrTarget.length; i++) {
                listTarget.add(arrTarget[i]);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //instansiasi target chromosome yang akan dijadikan target global maksimum
        Chromosome global = new Chromosome(board.size());
        for (int i = 0; i < listTarget.size(); i++) {
            global.addAleleToChromosome(listTarget.get(i));
        }

        //inisialisasi kelas algoritma genetik
        GeneticAlgorithm processor = new GeneticAlgorithm(board, 80, board.size(), global);
        processor.createInitialPopulation();
        processor.calculateFitness();
        processor.addParentsToWheel();
    }
}