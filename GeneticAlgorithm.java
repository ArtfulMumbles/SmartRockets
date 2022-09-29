import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

public class GeneticAlgorithm {
    public final int STEPS = 90; 
    public final int POP_SIZE = 10000;
    public final float MUTATION_RATE = 0.0001f;
    public final int NUM_GENERATIONS = 15;
    public final int STARTING_SPOT = 600;
    public final double RETAIN = 0.1;
    public int currentStep;
    public Vector2D target;
    public Rocket[] population;
    public int generation;
    public Random rand = new Random();

    public GeneticAlgorithm(double targetX, double targetY) {
        this.target = new Vector2D(targetX, targetY);
        this.generation = 0;
        this.population = new Rocket[POP_SIZE];
        this.currentStep = 0;
        for( int i = 0; i < POP_SIZE; i++ ) {
            this.population[i] = new Rocket(STARTING_SPOT, STARTING_SPOT, STEPS);
        }

    }

    public boolean updateRockets() {
        if(currentStep == STEPS) {
            if(generation()) return true;
            currentStep = 0;
            return false;
        }


        for(int i = 0; i < POP_SIZE; i++) {
            population[i].vel = population[i].velArr[currentStep];
        }
        currentStep++;
        return false;
    }

    public void moveRockets() {
        for(int i = 0; i < POP_SIZE; i++) {
            population[i].move();
        }
    }

    public boolean generation() {
        generation++;
        if (generation > NUM_GENERATIONS    ) {
            return true;
        }

        Rocket[] elitePop = Arrays.copyOfRange(sortByFitness(), 0, (int) (POP_SIZE * RETAIN));

        
        printInfo(elitePop[0]);

        if(elitePop[0].calculateFitness(target) <= 25) {
            return true;
        } 

        Rocket[] newPopulation = new Rocket[POP_SIZE];

        for(int i = 0; i < POP_SIZE; i++) {
            Rocket parent1 = getRandom(elitePop);
            Rocket parent2 = getRandom(elitePop);
            newPopulation[i] = mate(parent1, parent2);
        }

        for(int i = 0; i < elitePop.length; i++) {
            newPopulation[i] = new Rocket(STARTING_SPOT, STARTING_SPOT, elitePop[i].velArr);
        }

       this.population = newPopulation;

        return false;
    }

    public Rocket mate(Rocket one, Rocket two) {
        Vector2D[] newVelArr = new Vector2D[STEPS];
        for(int i = 0; i < STEPS; i++) {
            float chance = rand.nextFloat();
            if(chance < (1f - MUTATION_RATE) / 2f) {
                newVelArr[i] = one.getVel(i);
            } else if ((1f - MUTATION_RATE) / 2f <= chance && chance < 1f - MUTATION_RATE) {
                newVelArr[i] = two.getVel(i);
            } else {
                newVelArr[i] = Vector2D.random();
            }
        }

        return new Rocket(STARTING_SPOT, STARTING_SPOT, newVelArr);
    } 

    private Rocket[] sortByFitness() {
        this.population = Stream.of(population)
            .sorted((x, y) -> Double.compare(x.calculateFitness(target), y.calculateFitness(target)))
            .toArray(Rocket[]::new);
        return this.population;
    }

    public static Rocket getRandom(Rocket[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }
    public void printInfo() {
        this.printInfo(population[0]);
    }
    public void printInfo(Rocket best) {
       System.out.println("Generation: " + generation + " Fitness: " + best.calculateFitness(target));
    }

    public void drawRockets(Graphics2D g2d) {
        for(int i = 0; i < 10; i++) {
            g2d.setColor(Color.white);
            g2d.fillPolygon(population[i].getShape());
        }
    }
}