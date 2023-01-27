import java.util.ArrayList;

public class Population {
    private int popSize;
    private ArrayList<Chromosome> arrOfChromosome;
    private int chromSize;

    public Population(int popSize, int chromSize){
        this.popSize = popSize;
        this.chromSize = chromSize;
        this.arrOfChromosome = new ArrayList<>();
    }

    public void addChromosome(int idx, Chromosome chromosome){
        this.arrOfChromosome.add(idx,chromosome);
    }

    public Chromosome getChromosome(int idx){
        return this.arrOfChromosome.get(idx);
    }

    public int getPopSize(){
        return this.popSize;
    }

    public int getChromSize(){
        return this.chromSize;
    }

    public ArrayList<Chromosome> getArrOfChromosome(){
        return this.arrOfChromosome;
    }

    public void setNewChromosome(Chromosome c, int idx){
        arrOfChromosome.set(idx,c);
    }
}
