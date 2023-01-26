import java.util.ArrayList;
import java.util.Random;

//Komponen
// Population size: 80
// Chromosome size: 25
//

public class GeneticAlgorithm implements RouletteWheelSelection{
    private final int popSize;
    private final int chromSize;
    private ArrayList<String> board;
    private Population population;
    private RandomGenerator randGenerator;
    private Chromosome global;
    private int totalMating;

    public GeneticAlgorithm(ArrayList<String> board, int popSize, int chromSize, Chromosome global){
        this.board = board;
        this.popSize = popSize;
        this.chromSize = chromSize;
        this.population = new Population(this.popSize, this.chromSize);
        this.global = global;
        this.totalMating = this.popSize/2;
        //atur seed secara langsung (hardcode) pada argument
        this.randGenerator = new RandomGenerator(100);
    }

    public void createInitialPopulation(){
        for (int i = 0; i < this.popSize; i++) {
            Chromosome chromosome = new Chromosome(this.chromSize);
            for (int j = 0; j < chromosome.getChromSize(); j++) {
                int randomNumber = this.randGenerator.getBoundedRand(2);
                chromosome.addAleleToChromosome(randomNumber);
            }
            this.population.addChromosome(i,chromosome);
        }
//        printInfo();
    }

    public void calculateFitness(){
        int idx = 0;
        while(idx < this.popSize){
            Chromosome c = this.population.getChromosome(idx);

            int tempScore = 0;
            for (int i = 0; i < c.getChromSize(); i++) {
                if(this.global.getAlele(i) == c.getAlele(i)){
                    tempScore++;
                }
            }

            double score = (tempScore/(double)c.getChromSize())*100;
            c.setScore(score);
            printInfoWithFitness(idx);
            idx++;
        }
    }

    public void initializeSelection(){
        ArrayList<Chromosome> listOfParents = new ArrayList<>();
        chooseParents();
    }
    
    public void printInfoWithFitness(int idx){
        Chromosome c = this.population.getChromosome(idx);
        for (int i = 0; i < c.getChromSize(); i++) {
            System.out.print(c.getAlele(i));
        }
        System.out.print(", fitness score : " + c.getFitnessScore() + "\n");
    }

    public void printInfo(){
        for (int i = 0; i < this.population.getPopSize(); i++) {
            System.out.print("Populasi ke - " + (i+1) + " : ");
            for (int j = 0; j < this.population.getChromSize(); j++) {
                System.out.print(this.population.getChromosome(i).getAlele(j));
            }
            System.out.println();
        }
    }

    @Override
    public void chooseParents() {
        //pilih sebanyak setengah populasi sebagai parents untuk proses mating dalam roulette wheel selection
        //sehiniga offspring yang akan dihasilkan sebanyak 20 yang akan menggantikan beberapa chromosome (generasi baru)

        //jumlah semua fitness score yang kemudian akan dijadikan sebagai panjang sebuah array (array ini akan sebagai roulette wheel)
        int totalFitness = 0;
        for (int i = 0; i < this.population.getPopSize(); i++) {
            totalFitness += (int)this.population.getChromosome(i).getFitnessScore();
        }

        //assign calon parents ke dalam roulette wheel untuk pemilihan
        ArrayList<Chromosome> rouletteWheel = new ArrayList<>();
        int iterationIdx = 1;
        int chromosomeIdx = 0;
        for (int i = 0; i < totalFitness; i++) {
            Chromosome c = this.population.getChromosome(chromosomeIdx);
            if(iterationIdx == (int)c.getFitnessScore()){
                iterationIdx = 1;
                chromosomeIdx++;
            }
            else{
                iterationIdx++;
            }
            rouletteWheel.add(c);
        }

        ArrayList<Chromosome> listOfParents = new ArrayList<>();
        int idxParents = 0;
        while(idxParents < this.totalMating){
            int randomNum = this.randGenerator.getBoundedRand(totalFitness);
            Chromosome c = rouletteWheel.get(randomNum);

            //jika calon parents sudah ada
            if(!listOfParents.isEmpty()){
                //inisialisasi variabel hasSame untuk penandaan parent yang duplikat
                boolean hasSame = false;
                //iterasi untuk memeriksa parent yang duplikat
                for (int i = 0; i < listOfParents.size(); i++) {
                    Chromosome c1 = listOfParents.get(i);
                    //jika terdapat parents yang sama dengan yang sudah ada
                    if(c.equals(c1)){
                        //maka variabel hasSame ditandai dengan true
                        hasSame = true;
                        //looping berhenti
                        break;
                    }
                }
                //jika tidak ada parents yang duplikat
                if(!hasSame){
                    //maka proses penambahan parents dapat dilakukan
                    listOfParents.add(rouletteWheel.get(randomNum));
                    idxParents++;
                }
            }
            //jika calon parents belum ada
            else{
                listOfParents.add(rouletteWheel.get(randomNum));
                idxParents++;
            }
        }

//        for (int i = 0; i < listOfParents.size(); i++) {
//            for (int j = 0; j < listOfParents.get(i).getChromSize(); j++) {
//                System.out.print(listOfParents.get(i).getAlele(j));
//            }
//            System.out.println();
//        }
    }
}