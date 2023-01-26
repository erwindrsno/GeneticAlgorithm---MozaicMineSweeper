import java.util.ArrayList;
import java.util.Random;

//Komponen
// Population size: 80
// Chromosome size: 25
//

public class GeneticAlgorithm {
    private final int popSize;
    private final int chromSize;
    private ArrayList<String> board;
    private Population population;
    private RandomGenerator randGenerator;
    private Chromosome global;

    public GeneticAlgorithm(ArrayList<String> board, int popSize, int chromSize, Chromosome global){
        this.board = board;
        this.popSize = popSize;
        this.chromSize = chromSize;
        this.population = new Population(this.popSize, this.chromSize);
        this.global = global;
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

    public void selectParents(){
        
        for (int i = 0; i < this.popSize; i++) {

        }
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
}