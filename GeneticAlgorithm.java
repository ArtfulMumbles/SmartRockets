import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

public class GeneticAlgorithm {
    public final int STEPS = 100; 
    public final int POP_SIZE = 100000;
    public final float MUTATION_RATE = 0.001f;
    public final int NUM_GENERATIONS = 15;
    public final int STARTING_SPOT = 600;
    public final double RETAIN = 0.1;
    public int currentStep;
    public Vector2D target;
    public Rocket[] population;
    public NoGoZone[] noGo;
    public int generation;
    public Random rand = new Random();
    public double highestFitness;
    public int generationsWithoutImprovement;

    public GeneticAlgorithm(double targetX, double targetY) {
        this.generationsWithoutImprovement = 0;
        this.highestFitness = 0;
        this.target = new Vector2D(targetX, targetY);
        this.generation = 0;
        this.population = new Rocket[POP_SIZE];
        this.currentStep = 0;
        for( int i = 0; i < POP_SIZE; i++ ) {
            this.population[i] = new Rocket(STARTING_SPOT, STARTING_SPOT, STEPS);
        }
        noGo = new NoGoZone[5];
        noGo[0] = new NoGoZone(475, 250, 50, 200);
        noGo[1] = new NoGoZone(0, 300, 450, 40);
        noGo[2] = new NoGoZone(550, 300, 450, 40);
        noGo[3] = new NoGoZone(100, 0, 40, 275);
        noGo[4] = new NoGoZone(200, 25, 40, 275);


    }

    public boolean updateRockets() {
        if(currentStep == STEPS) {
            if(generation()) return true;
            currentStep = 0;
            return false;
        }


        for(int i = 0; i < POP_SIZE; i++) {
            if(population[i].alive) {
                population[i].vel.add(population[i].velArr[currentStep]);
            }
            
        }
        currentStep++;
        return false;
    }

    public void moveRockets() {
        for(int i = 0; i < POP_SIZE; i++) {
            if(population[i].alive) {
                population[i].move();
                if(population[i].outOfBounds()) {
                    population[i].kill();
                }
            }
            
            for (int j = 0; j < noGo.length; j++) {
                if(population[i].alive) {
                    boolean test = noGo[j].testIntersect(population[i]);
                    if(!test) {
                        population[i].kill();
                    }
                } 
            }
        }
    }


    public boolean generation() {
        generation++;

        Rocket[] elitePop = Arrays.copyOfRange(sortByFitness(), 0, (int) (POP_SIZE * RETAIN));
        double totalFitness = Stream.of(elitePop).mapToDouble((x) -> x.calculateFitness(target)).sum();

        double[] probabilities = Stream.of(elitePop).mapToDouble((x) -> x.calculateFitness(target) / totalFitness).toArray();
        
        if(highestFitness == elitePop[0].calculateFitness(target)) {
            generationsWithoutImprovement++;
        } else {
            highestFitness = elitePop[0].calculateFitness(target);
        }
         
        printInfo(elitePop[0]);

        if(Vector2D.dist(new Vector2D(elitePop[0].x, elitePop[0].y), target) <= 25) {
            return true;
        } 

        Rocket[] newPopulation = new Rocket[POP_SIZE];

        for(int i = 0; i < POP_SIZE; i++) {
            Rocket parent1 = getRandomByFitness(elitePop, probabilities);
            Rocket parent2 = getRandomByFitness(elitePop, probabilities);
            newPopulation[i] = mate(parent1, parent2);
        }

        for(int i = 0; i < elitePop.length; i++) {
            newPopulation[i] = new Rocket(STARTING_SPOT, STARTING_SPOT, elitePop[i].velArr);
        }

       this.population = newPopulation;
       
       if (generation > NUM_GENERATIONS    ) {
        return true;
        }
        return false;
    }

    public Rocket mate(Rocket one, Rocket two) {
        float modifiedMutationChance = MUTATION_RATE * (float)generationsWithoutImprovement;
        if(modifiedMutationChance > 0.5f) {
            modifiedMutationChance = 0.5f;
        }
        Vector2D[] newVelArr = new Vector2D[STEPS];
        for(int i = 0; i < STEPS; i++) {
            float chance = rand.nextFloat();
            if(chance < (1f - modifiedMutationChance) / 2f) {
                newVelArr[i] = one.getVel(i);
            } else if ((1f - modifiedMutationChance) / 2f <= chance && chance < 1f - modifiedMutationChance) {
                newVelArr[i] = two.getVel(i);
            } else {
                newVelArr[i] = Vector2D.random();
            }
        }

        return new Rocket(STARTING_SPOT, STARTING_SPOT, newVelArr);
    } 

    private Rocket[] sortByFitness() {
        this.population = Stream.of(population)
            .sorted((x, y) -> Double.compare(y.calculateFitness(target), x.calculateFitness(target)))
            .toArray(Rocket[]::new);
        return this.population;
    }

    public static Rocket getRandom(Rocket[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    public static Rocket getRandomByFitness(Rocket[] array, double[] probs) {
        double p = Math.random();
        double cumulativeProbability = 0.0;
        for (int i = 0; i < array.length; i++) {
            cumulativeProbability += probs[i];
            if (p <= cumulativeProbability) {
                return array[i];
            }
        }
        throw new Error("Unreachable Code!!!");
    }

    public void printInfo() {
        this.printInfo(population[0]);
    }
    public void printInfo(Rocket best) {
       System.out.println("Generation: " + generation + " Fitness: " + Vector2D.dist(target, new Vector2D(best.x, best.y)));
    }

    public void drawRockets(Graphics2D g2d) {
        for(int i = 0; i < 10; i++) {
            g2d.setColor(Color.green);
            if(population[i].dead()) {
                g2d.setColor(Color.gray);
            }
            g2d.fillPolygon(population[i].getShape());
        }
        for(int i = 10; i < 25; i++) {
            g2d.setColor(Color.white);
            if(population[i].dead()) {
                g2d.setColor(Color.gray);
            }
            g2d.fillPolygon(population[i].getShape());
        }
    }

    public void drawNoGo(Graphics2D g) {
        for(NoGoZone zone : noGo) {
            g.setColor(Color.red);
            g.drawRect(zone.x, zone.y, zone.width, zone.height);
        }
    }
 }