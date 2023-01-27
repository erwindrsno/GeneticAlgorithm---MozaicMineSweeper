import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

//Komponen
// Population size: 80
// Chromosome size: 25
//

public class GeneticAlgorithm{
    private final int popSize;
    private final int chromSize;
    private ArrayList<String> board;
    private Population population;
    private RandomGenerator randGenerator;
    private Chromosome global;
    private ArrayList<Chromosome[]> listOfPairs;
    private ArrayList<Chromosome> listOfOffspring;
    private int generation;
    private RouletteWheel wheelSelection;
    //atur mutation rate secara langsung (hardcode) pada line 22
    private final double mutationRate = 1.0;

    public GeneticAlgorithm(ArrayList<String> board, int popSize, int chromSize, Chromosome global){
        this.board = board;
        this.popSize = popSize;
        this.chromSize = chromSize;
        this.population = new Population(this.popSize, this.chromSize);
        this.global = global;
        this.listOfOffspring = new ArrayList<>();
        this.generation = 1;
        //atur seed secara langsung (hardcode) pada argument di line 33
        this.randGenerator = new RandomGenerator(100);
        this.wheelSelection = new RouletteWheel(this.popSize,this.population,this.randGenerator);
    }

    //pembuatan generasi pertama
    public void createInitialPopulation(){
        for (int i = 0; i < this.popSize; i++) {
            //inisialisasi chromosome baru
            Chromosome chromosome = new Chromosome(this.chromSize);
            for (int j = 0; j < chromosome.getChromSize(); j++) {
                int randomNumber = this.randGenerator.getBoundedRand(2);
                //assign random value antara 0 dan 1 ke dalam chromosome.
                chromosome.addAleleToChromosome(randomNumber);
            }
            //menambahkan chromosome ke dalam populasi
            this.population.addChromosome(i,chromosome);
        }
    }

    //penghitungan fitness setiap chromosome
    public void calculateFitness(){
        //index untuk iterasi
        int idx = 0;
        System.out.println("\n===GENERASI KE-"+this.generation+"===");
        while(idx < this.popSize){
            //pengambilan chromosome pada index ke-idx
            Chromosome c = this.population.getChromosome(idx);
            //variabel temporary score
            int tempScore = 0;
            for (int i = 0; i < c.getChromSize(); i++) {
                //jika terdapat elemen yang sama pada indeks ke-i antar chromosome ke-idx dan global chromosome
                if(this.global.getAlele(i) == c.getAlele(i)){
                    //maka increment tempScore
                    tempScore++;
                }
            }

            //konversi score menjadi dalam bentuk persen
            double score = (tempScore/(double)c.getChromSize())*100;
            //set score chromosome
            c.setScore(score);
            //print alel chromosome dan fitnessnya
            printInfoWithFitness(idx);
            //increment indexnya
            idx++;
        }
    }

    //output chromosome ke-idx beserta fitness scorenya
    public void printInfoWithFitness(int idx){
        Chromosome c = this.population.getChromosome(idx);
        for (int i = 0; i < c.getChromSize(); i++) {
            System.out.print(c.getAlele(i));
        }
        System.out.print(", fitness score : " + c.getFitnessScore() + "\n");
    }

    //inisialisasi roultte wheel
    public void initializeSelection(){
        //menambahkan semua chromosome ke dalam wheel untuk dipilih sebagai parents. Pemutaran roulette wheel akan berlangsung dalam method ini.
        this.wheelSelection.addParentsToWheel();
        //pemasangan parents yang sudah terpilih saat pemutaran roulette wheels
        this.wheelSelection.pairingParents();

        //mendapatkan setiap pasangan dari hasil pairing
        this.listOfPairs = this.wheelSelection.getListOfPairs();
    }

    //perkawinan
    public void crossOver(){
        for (int i = 0; i < this.listOfPairs.size(); i++) {
            //pengambilan chromosome dari masing-masing pasangan
            Chromosome c1 = this.listOfPairs.get(i)[0];
            Chromosome c2 = this.listOfPairs.get(i)[1];

            //inisialisasi string untuk proses crossover dengan menggunakan method substring
            String strC1 = "";
            String strC2 = "";

            //pengambilan alel untuk ditaruh pada string
            for (int j = 0; j < c1.getChromSize(); j++) {
                strC1 += c1.getAlele(j);
                strC2 += c2.getAlele(j);
            }

            //single point crossover
            //pemilihan antara chromosome a dan b untuk siapa yang akan menyilang siapa dengan cara random
            int choose = this.randGenerator.getBoundedRand(2);

            //inisialisasi string
            String portionC1 = "";
            String portionC2 = "";

            //jika 1 terpilih, maka c1 yang harus menyilang dengan c2
            if(choose == 1){
                portionC1 += strC1.substring(0,strC1.length()/2);
                portionC2 += strC2.substring(strC2.length()/2);
            }
            //jika 2 terpilih, maka c2 yang harus menyilang dengan c1
            else{
                portionC1 += strC1.substring(strC1.length()/2);
                portionC2 += strC2.substring(0,strC2.length()/2);
            }

            //penyusunan chromosome baru yang terbentuk dalam bentuk string
            String newChromosomeStr = portionC1+portionC2;

            //pembuatan object chromosome baru
            Chromosome offspring = new Chromosome(this.chromSize);
            //assign chromosome baru dengan string chromosome hasil persilangan
            for (int j = 0; j < newChromosomeStr.length(); j++) {
                //pengambilan alele yang dari bentuk String konversi menjadi integer
                int alele = Integer.parseInt(newChromosomeStr.charAt(j)+"");
                //penambahan alel ke chromosome
                offspring.addAleleToChromosome(alele);
            }
            //penambahan offspring yang baru ke arraylist listOfOffSpring
            this.listOfOffspring.add(offspring);
        }
    }

    //mutasi
    public void mutation(){
        //looping sepanjang list offspring
        for (int i = 0; i < this.listOfOffspring.size(); i++) {
            //pengambilan random value dari 0 - 100, jika random value <= rasio mutasi (1.0), maka mutasi akan dilakukan dengan flip alele pada index yang random juga
            if(this.randGenerator.getDoubleBoundedRand(100) <= this.mutationRate){
                //flip random index pada chromosome ke-i
                int randIdx = this.randGenerator.getBoundedRand(this.chromSize);
                this.listOfOffspring.get(i).flipAlele(randIdx);
            }
        }
    }

    //penggantian populasi dengan cara menggantikan chromosome yang memiliki fitness yang rendah dengan chromosome fitness yang lebih baik
    public void replacePopulation(){
        int idx = 0;
        while(idx < this.listOfOffspring.size()){
            for (int i = 0; i < this.popSize; i++) {
                //pengambilan chromosome pada inisial populasi
                Chromosome c1 = this.population.getChromosome(i);
                //pengambilan chromosome pada offspring
                Chromosome c2 = this.listOfOffspring.get(idx);
                //jika fitness pada offspring lebih bagus, maka chromosome ke-i pada populasi akan diganti dengan offspring tersebut
                if(c1.getFitnessScore() < c2.getFitnessScore()){
                    this.population.setNewChromosome(c2,i);
                }
            }
            //increment index
            idx++;
        }
        //increment generation karena repopulasi berhasil dilakukan.
        this.generation++;
    }

    //pembuangan residu list yang ada untuk menghindari konflik
    public void clearAllUnnecessaryLists(){
        this.listOfPairs.clear();
        this.listOfOffspring.clear();
        this.wheelSelection.clearLists();
    }

    //pemeriksaan global maksimum
    public boolean checkGlobalMax(){
        //looping sepanjang populasi
        for (int i = 0; i < this.popSize; i++) {
            Chromosome c = this.population.getChromosome(i);
            //jika chromosome dengan fitness 100, maka looping utama akan break dan menampilkan chromosome global maksimum.
            if(c.getFitnessScore() == 100){
                return true;
            }
        }
        //mengembalikan false jika global maksimum belum ditemukan dan looping utama akan iterasi lagi.
        return false;
    }

//    public void printPairsInfo(){
//        for (int i = 0; i < this.listOfPairs.size(); i++) {
//            for (int j = 0; j < listOfPairs.get(i).length; j++) {
//                Chromosome c = this.listOfPairs.get(i)[j];
//                for (int k = 0; k < c.getChromSize(); k++) {
//                    System.out.print(c.getAlele(k));
//                }
//                System.out.print(" ");
//            }
//            System.out.println();
//        }
//    }

    //method getter generation
    public int getGeneration(){
        return this.generation;
    }
}