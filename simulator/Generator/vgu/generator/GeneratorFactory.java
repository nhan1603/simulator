package vgu.generator;

import java.util.ArrayList;

import interfaces.AbstractComponent;

/***
 *
 * This Factory should return one or several of the self-implemented.
 * Power-Supplies, derived from 'interfaces.AbstractComponent'.
 * It is a convenient place to implement distribution and frequency of
 * different generator types.
 * 
 *
 */
public class GeneratorFactory {
	/**
	 * Generate a single generator. 
	 * @param name the name of the generator
	 * @param maxPower maximum possible power of the generator
	 * @param minPower minimum possible power of the generator
	 * @param maxChange maximum possible change in the power of the generator
	 * @param minChange minimum possible change in the power of the generator
	 * @return a generator using the constructor of the class generator
	 */

	public static AbstractComponent generate(String name, double maxPower, double minPower, double maxChange,
			double minChange) {
		return new Generator(name, maxPower, minPower, maxChange, minChange);
	}

	/**
	 * Generate a range of designated amount of generators.
	 * @param amount	Number of generator
	 * @param totalPower	the total power of all the generators
	 * @param startPower	the limit that restrict the maximum possible power of the generator
	 * @return an ArrayList that contains a range of designated amount of generators
	 */
	public static ArrayList<AbstractComponent> generate(int amount, double totalPower, double startPower) {
		ArrayList<AbstractComponent> generators = new ArrayList<>();
		double power = startPower;
		double avgPower = totalPower / amount;
		AbstractComponent generator;
		
		generator = new Generator("generator_1", avgPower, 0, avgPower, 0);
		generators.add(generator);
			
		for (int i = 2; i <= amount; i++) {
			generator = new Generator("generator_" + String.valueOf(i), avgPower, 0, avgPower / 2, avgPower * 0.275);
			if (power > avgPower * 0.5) {
				generator.setPower(avgPower * 0.5);
				power -= avgPower * 0.5;
			}
			generators.add(generator);
		}
		
		return generators;
	}
}
