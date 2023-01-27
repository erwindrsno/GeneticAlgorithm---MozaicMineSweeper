import java.util.ArrayList;

public class RouletteWheel {
    private ArrayList<Chromosome> listOfParents;
    private ArrayList<Chromosome> rouletteWheel;
    private ArrayList<Chromosome[]> listOfPairs;
    private Population population;
    private RandomGenerator randGenerator;
    private int totalMating;
    private int popSize;

    public RouletteWheel(int popSize, Population population, RandomGenerator randGenerator){
        this.popSize = popSize;
        this.totalMating = this.popSize/2;
        this.listOfParents = new ArrayList<>();
        this.rouletteWheel = new ArrayList<>();
        this.population = population;
        this.randGenerator = randGenerator;

        this.listOfPairs = new ArrayList<>();
    }

    public void addParentsToWheel() {
        //pilih sebanyak setengah populasi sebagai parents untuk proses mating dalam roulette wheel selection
        //sehingga offspring yang akan dihasilkan sebanyak 20 yang akan menggantikan beberapa chromosome (generasi baru)

        //jumlah semua fitness score yang kemudian akan dijadikan sebagai panjang sebuah array (array ini akan sebagai roulette wheel)
        int totalFitness = 0;
        for (int i = 0; i < this.population.getPopSize(); i++) {
            totalFitness += (int)this.population.getChromosome(i).getFitnessScore();
        }

        //assign calon parents ke dalam roulette wheel untuk pemilihan
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

        int idxParents = 0;
        while(idxParents < this.totalMating){
            int randomNum = this.randGenerator.getBoundedRand(totalFitness);
            Chromosome c = rouletteWheel.get(randomNum);

            //jika calon parents sudah ada
            if(!this.listOfParents.isEmpty()){
                //inisialisasi variabel hasSame untuk penandaan parent yang duplikat
                boolean hasSame = false;
                //iterasi untuk memeriksa parent yang duplikat
                for (int i = 0; i < this.listOfParents.size(); i++) {
                    Chromosome c1 = this.listOfParents.get(i);
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
                    this.listOfParents.add(rouletteWheel.get(randomNum));
                    idxParents++;
                }
            }
            //jika calon parents belum ada
            else{
                this.listOfParents.add(rouletteWheel.get(randomNum));
                idxParents++;
            }
        }
    }

    public void pairingParents(){
        while(!this.listOfParents.isEmpty()){
            int p1 = this.randGenerator.getBoundedRand(this.listOfParents.size());
            int p2 = this.randGenerator.getBoundedRand(this.listOfParents.size());
            while(p1 == p2){
                p2 = this.randGenerator.getBoundedRand(this.listOfParents.size());
            }

            Chromosome c1 = this.listOfParents.get(p1);
            Chromosome c2 = this.listOfParents.get(p2);
            Chromosome[] arrChromosome = {c1,c2};

            this.listOfParents.remove(c1);
            this.listOfParents.remove(c2);

            this.listOfPairs.add(arrChromosome);
        }
    }

    public ArrayList<Chromosome[]> getListOfPairs(){
        return this.listOfPairs;
    }

    public void clearLists(){
        this.listOfParents.clear();
        this.listOfPairs.clear();
    }
}