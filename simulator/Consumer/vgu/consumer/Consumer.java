package vgu.consumer;

import java.util.Random;

import interfaces.AbstractComponent;
import vgu.price.Price;

public class Consumer extends AbstractComponent {
	
	private String name;
	private double maxPower;
	private double minPower;
	private double maxChange;
	private double minChange;
	private double[] runTime;
	private double power;
	private Price price;
	
	private int iteration = 0;
	private static boolean isOn = false;
	/**
	 * The constructor allows the initialization of the Consumer object which will be used for the Factory Method Pattern.
	 * @param name name of the consumer
	 * @param maxPower	maximum possible power
	 * @param minPower	minimum possible power
	 * @param maxChange	maximum possible shifting power
	 * @param minChange	minimum possible shifting power
	 * @param runTime	behavior in each iteration of the consumer
	 */
	public Consumer(String name, double maxPower, double minPower, double maxChange, double minChange, double[] runTime) {
		this.name = name;
		this.maxPower = maxPower;
		this.minPower = minPower;
		this.maxChange = maxChange;
		this.minChange = minChange;
		this.runTime = runTime;
		this.power = minPower;
		this.price = new Price(this);
	}
	/**
	 *
	 * @return the name of the consumer.
	 */
	public String getName() {
		return name;
	}
	/**
	 *  set the name of the consumer.
	 * @param name the designating name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * return the maximum possible power of the consumer.
	 */
	@Override
	public double getMaxPower() {
		return this.maxPower;
	}
	/**
	 * setting the maximum possible power of the consumer.
	 */
	@Override
	public void setMaxPower(double maxPower) {
		super.setMaxPower(maxPower);
		this.maxPower = maxPower;
	}
	/**
	 * return the minimum power of the consumer.
	 */
	@Override
	public double getMinPower() {
		return this.minPower;
	}
	/**
	 * setting the minimum possible power of the consumer.
	 */
	@Override
	public void setMinPower(double minPower) {
		super.setMinPower(minPower);
		this.minPower = minPower;
	}
	/**
	 * return the maximum power shift of the consumer.
	 */
	@Override
	public double getMaxChange() {
		return this.maxChange;
	}
	/**
	 * setting the maximum power shift of the consumer.
	 */
	@Override
	public void setMaxChange(double maxChange) {
		super.setMaxChange(maxChange);
		this.maxChange = maxChange;
	}
	/**
	 * return the minimum power shift of the consumer.
	 */
	@Override
	public double getMinChange() {
		return this.minChange;
	}
	/**
	 * setting the minimum power shift of the consumer.
	 */
	@Override
	public void setMinChange(double minChange) {
		super.setMinChange(minChange);
		this.minChange = minChange;
	}
	/**
	 * return the current power of the consumer.
	 */
	@Override
	public double getPower() {
		return power;
	}

	@Override
	public void setPower(double power) {
		if (Math.abs(this.power - power) > getMaxChange()) {
			this.power += getMaxChange();
		} else if (Math.abs(this.power - power) < getMinChange()) {
			this.power = (power + getMinChange());
		}
		
		if (this.power > getMaxPower()) {
			this.power = getMaxPower();
		} else if (this.power < getMinPower()) {
			this.power = getMinPower();
		} else {
			this.power = power;
		}
	}
	/**
	 * Simulating the behavior of the consumer in the next iteration.
	 * It create a random double variable between 0.0 and 1.0. 
	 * If this variable is smaller than the value of the iteration array at that iteration
	 * the consumer is considered using electricity and the power value is set to be the maximum possible value 
	 * and it is set to the minimum possible value otherwise
	 */
	@Override
	public void next() {
		Random random = new Random();
		
		if (random.nextDouble() < runTime[iteration]) {
			isOn = true;
		} else {
			isOn = false;
		}
		
		if (isOn) {
			this.power = getMaxPower();
		} else {
			this.power = getMinPower();
		}
		
		iteration++;
	}
	/**
	 * return the cost of the consumer's power.
	 */
	@Override
	public double getCost() {
		return this.price.calculatePrice();
	}
	
}
