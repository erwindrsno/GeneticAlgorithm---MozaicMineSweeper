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
    //atur mutation rate secara langsung (hardcode) pada line 19
    private final double mutationRate = 1.0;

    public GeneticAlgorithm(ArrayList<String> board, int popSize, int chromSize, Chromosome global){
        this.board = board;
        this.popSize = popSize;
        this.chromSize = chromSize;
        this.population = new Population(this.popSize, this.chromSize);
        this.global = global;
        this.listOfOffspring = new ArrayList<>();
        this.generation = 1;
        //atur seed secara langsung (hardcode) pada argument di line 29
        this.randGenerator = new RandomGenerator(100);
        this.wheelSelection = new RouletteWheel(this.popSize,this.population,this.randGenerator);
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
    }

    public void calculateFitness(){
        int idx = 0;
        System.out.println("\n===GENERASI KE-"+this.generation+"===");
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
    
    public void printInfoWithFitness(int idx){
        Chromosome c = this.population.getChromosome(idx);
        for (int i = 0; i < c.getChromSize(); i++) {
            System.out.print(c.getAlele(i));
        }
        System.out.print(", fitness score : " + c.getFitnessScore() + "\n");
    }

    public void initializeSelection(){
        this.wheelSelection.addParentsToWheel();
        this.wheelSelection.pairingParents();

        this.listOfPairs = this.wheelSelection.getListOfPairs();
    }

    public void crossOver(){
        for (int i = 0; i < this.listOfPairs.size(); i++) {
            Chromosome c1 = this.listOfPairs.get(i)[0];
            Chromosome c2 = this.listOfPairs.get(i)[1];

            String strC1 = "";
            String strC2 = "";

            for (int j = 0; j < c1.getChromSize(); j++) {
                strC1 += c1.getAlele(j);
                strC2 += c2.getAlele(j);
            }

            //single point crossover
            int choose = this.randGenerator.getBoundedRand(2);

            String portionC1 = "";
            String portionC2 = "";

            if(choose == 1){
                portionC1 += strC1.substring(0,strC1.length()/2);
                portionC2 += strC2.substring(strC2.length()/2);
            }
            else{
                portionC1 += strC1.substring(strC1.length()/2);
                portionC2 += strC2.substring(0,strC2.length()/2);
            }

            String newChromosomeStr = portionC1+portionC2;

            Chromosome offspring = new Chromosome(this.chromSize);
            for (int j = 0; j < newChromosomeStr.length(); j++) {
                int alele = Integer.parseInt(newChromosomeStr.charAt(j)+"");
                offspring.addAleleToChromosome(alele);
            }

            this.listOfOffspring.add(offspring);
        }
    }

    public void mutation(){
        for (int i = 0; i < this.listOfOffspring.size(); i++) {
            if(this.randGenerator.getDoubleBoundedRand(100) <= this.mutationRate){
                //flip random index pada chromosome ke-i
                int randIdx = this.randGenerator.getBoundedRand(this.chromSize);
                this.listOfOffspring.get(i).flipAlele(randIdx);
            }
        }
    }

    public void replacePopulation(){
        int idx = 0;
        while(idx < this.listOfOffspring.size()){
            for (int i = 0; i < this.popSize; i++) {
                Chromosome c1 = this.population.getChromosome(i);
                Chromosome c2 = this.listOfOffspring.get(idx);
                if(c1.getFitnessScore() < c2.getFitnessScore()){
                    this.population.setNewChromosome(c2,i);
                }
            }
            idx++;
        }
        this.generation++;
    }

    public void clearAllUnnecessaryLists(){
        this.listOfPairs.clear();
        this.listOfOffspring.clear();
        this.wheelSelection.clearLists();
    }

    public boolean checkGlobalMax(){
        for (int i = 0; i < this.popSize; i++) {
            Chromosome c = this.population.getChromosome(i);
            if(c.getFitnessScore() == 100){
                return true;
            }
        }
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

    public int getGeneration(){
        return this.generation;
    }
}