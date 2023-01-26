import java.util.ArrayList;

public class Chromosome {
    private ArrayList<Integer> arrayListOfAlele;
    private int chromSize;
    private double score;

    public Chromosome(int chromSize){
        this.arrayListOfAlele = new ArrayList<>();
        this.chromSize = chromSize;
    }

    public void addAleleToChromosome(int check){
        this.arrayListOfAlele.add(check);
    }

    public int getChromSize(){
        return this.chromSize;
    }

    public int getAlele(int idx){
        return this.arrayListOfAlele.get(idx);
    }

    public double getFitnessScore(){
        return this.score;
    }

    public void setScore(double score){
        this.score = score;
    }
}
