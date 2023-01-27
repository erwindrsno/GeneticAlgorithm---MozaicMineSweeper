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

// Sebelum menjalankan file, perlu mengubah file yang berisi input puzzle terlebih dahulu dengan cara mengganti filepathnya yang ada pada di line-33.

// Format input (5x5)
// *,*,5,*,2,
// *,4,*,*,*,
// 3,*,*,*,6,
// *,*,3,4,*,
// *,3,*,*,*,

// tanda '*' sebagai kotak yang tidak berisi angka

// List perbaikan dari program sebelumnya:
// * Mengubah kotak yang kosong dari awalnya -1 menjadi tanda '*' dengan alasan agar mudah dibaca
// * Mengubah program dari awalnya memiliki coupling yang sangat tinggi menjadi low coupling
// * Mengubah program menjadi lebih mudah dibaca
// * Mengubah cara prosedur mutasi
// * Menyusun semua komponen menjadi sebuah kelas terpisah / OOP yang lebih rapih (pada program sebelumnya hanya bergantung pada struktur data. Hal ini menyebabkan program sulit dibaca)

public class Main {
    public static void main(String[] args) {
        File inputFile = new File("D://MozaicMineSweeper/src/Input.txt");
        //inisialisasi beberapa arraylist
        //board sebagai papan keseluruhan
        ArrayList<String> board = new ArrayList<>();
        //listTarget yang akan dijadian sebagai global maksimum
        ArrayList<Integer> listTarget = new ArrayList<>();

        try {
            //Scanner untuk menerima input yang akan disimpan dalam sebuah array bernama arr
            Scanner sc = new Scanner(inputFile);
            while(sc.hasNextLine()){
                String[] arr = sc.nextLine().split(",");
                //iterasi setiap elemen dan disimpan ke board
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
                    //semua kotak harus diinisialisasi dengan bisajadi terlebih dahulu.
                    arrBoardCheck[i][j] = "bisajadi";
                }
            }

            //pembuatan kelas solver untuk menyelesaikan puzzle tanpa menggunakan algoritma genetik
            Solver solver = new Solver(arrBoard, arrBoardCheck, answer, (int)Math.pow(boardLength,2));
            //proses penyelesaian dimulai
            solver.begin();

            //Konversi solusi menjadi array
            int[] arrTarget = solver.convertToArray();
            for (int i = 0; i < arrTarget.length; i++) {
                //menambahkan setiap elemen pada arrTarget ke dalam sebuah list baru
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
        //proses penyelesaian puzzle dengan algoritma genetik
        processor.createInitialPopulation();
        processor.calculateFitness();
        processor.initializeSelection();
        processor.crossOver();
        processor.mutation();
        processor.replacePopulation();
        //method dibawah ini berfungsi untuk membuang residu list agar dapat repopulate lagi.
        processor.clearAllUnnecessaryLists();
        while(!processor.checkGlobalMax()){
            processor.calculateFitness();
            processor.initializeSelection();
            processor.crossOver();
            processor.mutation();
            processor.replacePopulation();
            processor.clearAllUnnecessaryLists();
        }
        if(processor.checkGlobalMax()){
            System.out.println("\nGlobal maksimum ditemukan!");
            System.out.println("Ditemukan pada generasi ke-"+processor.getGeneration());
        }
    }
}