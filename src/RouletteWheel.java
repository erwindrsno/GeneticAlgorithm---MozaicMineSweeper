import java.util.ArrayList;

// Konsep roulette wheel:
// roulette wheel akan dibuat dalam bentuk arraylist dengan panjang total fitness score dari popoulasi.
// contoh: jika pada c1 memiliki fitness bernilai 3, c2 bernilai 5, c3 bernilai 2 dan panjang roulette wheel ada 10,
// maka roulette wheel menjadi: c1 c1 c1 c2 c2 c2 c2 c2 c3 c3.
// roulette wheel hanya memiliki 1 fixed point



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

        //assign calon parents ke dalam roulette wheel untuk pemilihan (penjelasan terdapat di atas program)
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
        //proses pemilihan parents dengan cara memutarkan roulette wheel
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

    //pemasangan parents
    public void pairingParents(){
        //looping selama masih ada parents
        while(!this.listOfParents.isEmpty()){
            //akan dipilih pasangan secara acak dengan bound ukuran list of parents.
            int p1 = this.randGenerator.getBoundedRand(this.listOfParents.size());
            int p2 = this.randGenerator.getBoundedRand(this.listOfParents.size());
            //looping jika p1 = p2 untuk menghindari perkawinan tanpa pasangan
            while(p1 == p2){
                p2 = this.randGenerator.getBoundedRand(this.listOfParents.size());
            }

            //pengambilan variabel dan diassign ke dalam sebuah static array
            Chromosome c1 = this.listOfParents.get(p1);
            Chromosome c2 = this.listOfParents.get(p2);
            Chromosome[] arrChromosome = {c1,c2};

            //membuang pasangan yang akan dikawinkan pada list of parents
            this.listOfParents.remove(c1);
            this.listOfParents.remove(c2);

            //menambahkan pasangan ke list baru yaitu listofpairs
            this.listOfPairs.add(arrChromosome);
        }
    }

    //method getter listofpairs
    public ArrayList<Chromosome[]> getListOfPairs(){
        return this.listOfPairs;
    }

    //method pengosongan residu list
    public void clearLists(){
        this.listOfParents.clear();
        this.listOfPairs.clear();
    }
}