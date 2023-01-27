import java.util.Random;

public class RandomGenerator {
    private Random random;
    private long seed;

    public RandomGenerator(long seed){
        this.random = new Random();
        this.random.setSeed(seed);
    }

    public int getRand(){
        return this.random.nextInt();
    }

    public int getBoundedRand(int bound){
        return this.random.nextInt(bound);
    }

    public double getDoubleBoundedRand(int bound){
        return this.random.nextDouble(bound);
    }
}
