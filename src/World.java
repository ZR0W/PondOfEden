import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class World {

    private int width;
    private int length;
    private ArrayList<Creature> creatures;
    private int defaultNum = 100;

    public World(int width, int length) {
        creatures = new ArrayList<Creature>();
        this.width = width;
        this.length = length;

        initCreatures(width, length, defaultNum);
    }

    public World() {
        creatures = new ArrayList<Creature>();
        initCreatures(defaultNum);
    }

    private void initCreatures(int width, int length, int numCreatures) {
        Random random = new Random();
        for(int i = 0; i < numCreatures; i++) {
            //width of the bell curve is 2.355 times standard deviation
            //nextGuassian() * standard deviation + mean
            Creature c = new Creature(Math.max(Math.min(random.nextGaussian()*2 + 5.0, 10), 0));

            c.setLocation(random.nextInt(width), random.nextInt(length));

            creatures.add(c);
        }
    }

    private void initCreatures(int numCreatures) {
        Random random = new Random();
        for(int i = 0; i < numCreatures; i++) {
            //MV of all creatures generated according to normal distribution
            Creature c = new Creature(Math.max(Math.min(random.nextGaussian()*2 + 5.0, 10), 0));

            creatures.add(c);
        }
    }

    public ArrayList<Creature> getCreatures() {
        return creatures;
    }

    public Creature getPotentialMateFor(Creature c) {
        int randomIndex = -1;
        do {
            randomIndex = new Random().nextInt(creatures.size());
        }
        while(randomIndex < 0 || creatures.get(randomIndex).equals(c));
        return creatures.get(randomIndex);
    }

    public boolean interact(Creature A, Creature B) {
        //A is approaching B
        if(!B.beApproachedBy(A)) {
            A.getRejected(B);
            return false;
        }else{
            return true;
        }
    }

    /*
    console print all SPMV
     */
    public void getStats() {
        for(int i = 0; i < creatures.size(); i++) {
            Creature c = creatures.get(i);
            Double mv = c.getMV();
            Double spmv = c.getSPMV();
            System.out.println(String.format("Creature number %d: MV %f, SPMV %f, diff %f", i, mv, spmv, mv-spmv));
        }
    }

    public static void main(String[] args) {
        //init world
        World world = new World();
        ArrayList<Creature> creatures = world.getCreatures();
        //init statRecord with initial world state
        StatRecord statRecord = new StatRecord(creatures);
        for(Creature c : creatures) {
            statRecord.addEntry(c, null, false, c.getMV(), c.getSPMV());
        }
        int epoch = 200;
        for(int i = 0; i < epoch; i++) {
            //for every creature give them a partner
            //A will make a decision on if it wants to mate. B will accept or not
            for(Creature foo : creatures) {
                Creature pursuingTarget = world.getPotentialMateFor(foo);
                boolean result = world.interact(foo, pursuingTarget);
                statRecord.addEntry(foo, pursuingTarget, result, foo.getMV(), foo.getSPMV());
            }
            System.out.println("epoch " + i + " =============================");
            world.getStats();
        }

        //query the result with console
        Scanner in = new Scanner(System.in);
        System.out.print("Enter creature history query: ");
        String consoleIn = in.nextLine();
        String exitCode = "exit()";
        while(!consoleIn.equals(exitCode)) {
            //parse query
            String[] inputArgs = consoleIn.split("\\s+");
            if(inputArgs.length != 2){
                System.out.println("Error input length: " + inputArgs.length);
            }else{
                int creatureIndex = Integer.valueOf(inputArgs[0]);
                String operation = inputArgs[1];
                switch(operation.toLowerCase()) {
                    case "mv":
                        System.out.println(creatures.get(creatureIndex).getMV());
                        break;
                    case "spmv":
                        Double[] d = statRecord.getSPMVHistory(creatures.get(creatureIndex));
                        String[] s = new String[d.length];
                        for(int i = 0; i < d.length; i++) {
                            s[i] = String.valueOf(d[i]);
                        }
                        System.out.println(String.join(", ", s));
                        break;
                    default:
                        System.out.println("default operation");
                }
            }
            //query again to restart the process
            System.out.print("Query: ");
            consoleIn = in.nextLine();
        }
        System.exit(0);
    }
}
