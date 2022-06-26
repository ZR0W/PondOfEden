import java.util.ArrayList;
import java.util.Random;

public class World {

    private int width;
    private int length;
    private ArrayList<Creature> creatures;

    public World(int width, int length) {
        creatures = new ArrayList<Creature>();
        this.width = width;
        this.length = length;

        initCreatures(width, length);
    }

    public World() {
        creatures = new ArrayList<Creature>();

        initCreatures();
    }

    private void initCreatures(int width, int length) {
        Random random = new Random();
        for(int i = 0; i < 100; i++) {
            //width of the bell curve is 2.355 times standard deviation
            //nextGuassian * standard deviation + mean
            Creature c = new Creature(Math.max(Math.min(random.nextGaussian()*2 + 5.0, 10), 0));

            c.setLocation(random.nextInt(width), random.nextInt(length));

            creatures.add(c);
        }
    }

    private void initCreatures() {
        Random random = new Random();
        for(int i = 0; i < 100; i++) {
            //MV of all creatures generated according to normal distribution
            Creature c = new Creature(Math.max(Math.min(random.nextGaussian()*2 + 5.0, 10), 0));

            creatures.add(c);
        }
    }

    public ArrayList<Creature> getCreatures() {
        return creatures;
    }

    private int[] getPotentialMates(int numMates) {
        return new int[]{1,2,3};
    }

    private int getPotentialMate() {
        return 0;
    }

    private void interact(Creature A, Creature B) {
        //A is approaching B
        if(!B.beApproached(A)) {
            A.getRejected(B);
        }
    }

    /*
    console print all SPMV
     */
    private void getStats() {
        System.out.println("");
        for(int i = 0; i < creatures.size(); i++) {
            Creature foo = creatures.get(i);
            //the SPMV of the population
            System.out.print(foo.getSPMV() + ", ");
            //the MV and SPMV differnce in the population
            System.out.print(foo.getMV()-foo.getSPMV() + ", ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        World world = new World();
        world.initCreatures();
        ArrayList<Creature> creatures = world.getCreatures();
        int cycles = 200;
        for(int i = 0; i < cycles; i++) {
            //for every creature give them a partner
            //A will make a decision on if it wants to mate. B will accept or not
            for(int j = 0; j < creatures.size(); j++) {
                Creature foo = creatures.get(j);
                world.interact(foo, creatures.get(world.getPotentialMate()));
            }
            System.out.println("cycle " + i);
            world.getStats();
        }
    }
}
