package vgu.consumer;
import java.util.ArrayList;
import java.util.Random;

import interfaces.AbstractComponent;

/***
 *
 * This Factory should return one or several of the self-implemented.
 * Consumers, derived from 'interfaces.AbstractComponent'.
 * It is a convenient place implement distribution and frequency of
 * consumer types.
 *
 */
public class ConsumerFactory {

	/**
	 * Stores the amount of consumers running at every iteration in percentage.
	 */
	static double[] run = new double[] {.5,.2,.15,.45,.75,.60,.55,.40,.45,.65,.95,.75};

	public static void setRunBehaviour(double[] runTime) {
		run = runTime;
	}

	/**
	 * Generate a single consumer.
	 * @param name	name of the consumer
	 * @param maxPower	maximum possible consuming power 
	 * @param minPower	minimum possible consuming power
	 * @param maxChange   maximum possible change of power
	 * @param minChange   minimum possible change of power
	 * @return a new consumer object based on the parameters
	 */
	public static AbstractComponent generate(String name, double maxPower, double minPower, double maxChange,
			double minChange) {
		return new Consumer(name, maxPower, minPower, maxChange, minChange, run);
	}

	/**
	 *
	 * Generate a set of consumers.
	 * @param amount	Number of consumers to generate
	 * @param avgPower	Mean Power of the determined amount of consumers
	 * @param std		Standard Deviation of power
	 * @return an ArrayList that contains a range of designated amount of consumers
	 */
	public static ArrayList<AbstractComponent> generate(int amount, int avgPower, int std) {
		Random random = new Random();
		ArrayList<AbstractComponent> consumers = new ArrayList<>();
		double power;
		
		for (int i = 0; i < amount; i++) {
			// Calculate the power based on normally distribution
			power = random.nextGaussian() * std + avgPower;
			consumers.add(new Consumer("consumer_" + String.valueOf(i + 1), power, 0, power, 0, run));
		}
		
		return consumers;
	}

}