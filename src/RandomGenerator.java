import java.util.Random;

public class RandomGenerator {
    private Random random;
    private long seed;

    public RandomGenerator(long seed){
        this.random = new Random();
        this.random.setSeed(seed);
    }

    //mengambil sebuah nilai random tanpa range
    public int getRand(){
        return this.random.nextInt();
    }

    //mengambil sebuah nilai random dengan range
    public int getBoundedRand(int bound){
        return this.random.nextInt(bound);
    }

    //mengambil sebuah nilai double random dengan range
    public double getDoubleBoundedRand(int bound){
        return this.random.nextDouble(bound);
    }
}
