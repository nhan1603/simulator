package vgu.control;
import java.util.ArrayList;
import java.util.List;

import interfaces.AbstractComponent;
import interfaces.IControl;
import vgu.consumer.Consumer;
import vgu.generator.Generator;

/**
 * This empty control template can be best implemented by evaluating all
 * JUnit-TestCases.
 */
//TODO request state changes in consumers for demand side management.
//TODO request supply and state changes in generators for demand side management.
//TODO request to start or shutdown generators for demand side management.
//TODO change the electricity price.
public class Control implements IControl {
	/**
	 * Local variables which is two List() of AbstractComponent in which 
	 * all the Generator objects and the Consumer objects will be passed and stored. 
	 * With such local variables, data will be handled and processed more easily
	 */
	private List<AbstractComponent> generators = new ArrayList<>();
	private List<AbstractComponent> consumers = new ArrayList<>();

	private int blackout = 0;
	private int overload = 0;
	/**
	 * Adding a generator component into the local generators List.
	 */
	@Override
	public void addGenerator(AbstractComponent generator) {
		if (generator instanceof Generator) {
			generators.add(generator);
		} else {
			throw new IllegalArgumentException("Wrong type: " + generator.getClass().toString());
		}
	}
	/**
	 * Removing a generator component from the local generators List.
	 */
	@Override
	public void removeGenerator(AbstractComponent generator) {
		if (generators.contains(generator)) {
			generators.remove(generator);
		} else {
			throw new IllegalArgumentException("There is no " + generator.toString() + " in generators.");
		}
	}
	/**
	 * Return the list of generators.
	 */
	@Override
	public List<AbstractComponent> getGenerators() {
		return generators;
	}
	/**
	 * Adding a consumer component into the local consumers List.
	 */
	@Override
	public void addConsumer(AbstractComponent consumer) {
		if (consumer instanceof Consumer) {
			consumers.add(consumer);
		} else {
			throw new IllegalArgumentException("Wrong type: " + consumer.getClass().toString());
		}
	}
	/**
	 * Removing a consumer component into the local consumers List.
	 */
	@Override
	public void removeConsumer(AbstractComponent consumer) {
		if (consumers.contains(consumer)) {
			consumers.remove(consumer);
		} else {
			throw new IllegalArgumentException("There is no " + consumer.toString() + " in consumers.");
		}
	}
	/**
	 * Return the list of consumer.
	 */
	@Override
	public List<AbstractComponent> getConsumers() {
		return consumers;
	}
	/**
	 * Calculate and return the total demand power of all the consumers in the consumers List.
	 */
	@Override
	public double getTotalDemand() {
		double total = 0;
		
		for (AbstractComponent consumer : consumers) {
			total += consumer.getPower();
		}
		
		return total;
	}
	/**
	 * Calculate and return the total supplying power of all the generators in the generators List.
	 */
	@Override
	public double getTotalSupply() {
		double total = 0;
		
		for (AbstractComponent generator : generators) {
			total += generator.getPower();
		}
		
		return total;
	}
	/**
	 * Manage the frequency based on certain conditions ( from the requirements) and return the value of the frequency.
	 * The standard frequency is 50Hz. 
	 * By each 10% the power supply is higher than the demand power, the frequency is decreased by 1Hz. 
	 * And by each 10% the power supply is lower than the demand power, the frequency is increased by 1Hz.
	 */
	@Override
	public double getFrequency() {
		double totalDemand = getTotalDemand();
		double totalSupply = getTotalSupply();
		
		double frequencyChange = 10.0 - (Math.min(totalDemand, totalSupply) * 10 / Math.max(totalDemand, totalSupply));
		
		if (totalDemand == 0 && totalSupply == 0 || isDefect()) {
			return 0;
		}
		
		if (totalDemand > totalSupply) {
			return 50 - frequencyChange;
		} else if (totalDemand < totalSupply) {
			return 50 + frequencyChange;
		} else {
			return 50;
		}
	}
	/**
	 * Return the cost of the generators' providing power.
	 */
	@Override
	public double getCost() {
		double total = 0;
		
		for (AbstractComponent generator : generators) {
			total += generator.getCost();
		}
		
		return total;
	}
	/**
	 * Return the profit from the consuming power of the consumers.
	 */
	@Override
	public double getProfit() {
		double total = 0;
		
		for (AbstractComponent consumer : consumers) {
			total += consumer.getCost();
		}
		
		return total;
	}
	/**
	 * Manage the change of the power supply of the generator based on
	 * the difference between the power supply and the demand power of the current iteration.
	 */
	@Override
	public void nextIteration() {
	    double diff = getTotalSupply() - getTotalDemand();
	    double powerChange = 0;
	    
	    for (int i = 0; i < generators.size(); i++) {
	    	AbstractComponent generator = generators.get(i);
	    	
	    	if (diff > 0) {
	    		powerChange = Math.min(generator.getMaxChange(), generator.getPower() - generator.getMinPower());
	    		if (Math.abs(diff) >= powerChange) {
	    			generator.setPower(generator.getPower() - powerChange);
	    			diff -= powerChange;
	    		} else {
	    			powerChange = Math.max(Math.abs(diff), generator.getMinChange());
	    			if (Math.abs(diff) >= powerChange) {
		    			generator.setPower(generator.getPower() - powerChange);
		    			diff -= powerChange;
		    		}
	    		}
	    	} else if (diff < 0) {
	    		powerChange = Math.min(generator.getMaxChange(), generator.getMaxPower() - generator.getPower());
	    		if (Math.abs(diff) >= powerChange) {
	    			generator.setPower(generator.getPower() + powerChange);
	    			diff += powerChange;
	    		} else {
	    			powerChange = Math.max(Math.abs(diff), generator.getMinChange());
	    			if (Math.abs(diff) >= powerChange) {
	    				generator.setPower(generator.getPower() + powerChange);
	    				diff += powerChange;
	    			}
	    		}
	    	}
	    }
		
		frequencyBalancer(getFrequency());
	}
	/**
	 * Check whether the system is overload ( frequency > 51 ). 
	 * @param frequency the current frequency of the system
	 * @return true if the system is overload
	 * @return false if the system is not overload
	 */
	private boolean isOverload(double frequency) {
		if (frequency > 51) return true;
		else return false;
	}
	
	/**
	 * Check whether the system is blackout ( frequency < 49 ). 
	 * @param frequency	the current frequency of the system
	 * @return true if the system is blackout
	 * @return false if the system is not blackout
	 */
	private boolean isBlackout(double frequency) {
		if (frequency < 49) return true;
		else return false;
	}
	/** 
	 * Check whether the system is defect :
	 * the frequency stays lower than 49 for 3 iterations or more 
	 * or the frequency stays higher than 51 for 3 iterations or more.
	 * @return true if the system is defect.
	 * @return false if the system if not defect.
	 */
	private boolean isDefect() {
		if (overload >= 3 || blackout >= 3) {
			return true;
		}
		
		return false;
	}
	/**
	 * Managing the state of the system by calculating the amount of continuously blackout and overload frequency over iterations.
	 * Shut down the system if it is defect by removing all the consumers and generators.
	 * @param frequency the current frequency of the system.
	 */
	private void frequencyBalancer(double frequency) {
		int unregisterComponents = 0;
		
		if (isOverload(frequency)) {
			overload += 1;
			blackout = 0;
			unregisterComponents = (int) Math.ceil(generators.size() * 0.1);
			generators.subList(0, unregisterComponents).clear();
		} else if (isBlackout(frequency)) {
			blackout += 1;
			overload = 0;
			unregisterComponents = (int) Math.ceil(consumers.size() * 0.15);
			consumers.subList(0, unregisterComponents).clear();
		} else {
			overload = 0;
			blackout = 0;
		}
		
		if (isDefect()) {
			consumers.clear();
			generators.clear();
		}
	}

}
