package vgu.generator;

import interfaces.AbstractComponent;
import vgu.price.Price;

public class Generator extends AbstractComponent {
	
	private String name;
	private double maxPower;
	private double minPower;
	private double maxChange;
	private double minChange;
	private double power;
	
	private Price price;
	/**
	 * The constructor allows the initialization of the Generator object which will be used for the Factory Method Pattern.
	 * @param name name of the generator
	 * @param maxPower	maximum possible power
	 * @param minPower	minimum possible power
	 * @param maxChange	maximum possible shifting power
	 * @param minChange	minimum possible shifting power
	 */
	
	public Generator(String name, double maxPower, double minPower, double maxChange, double minChange) {
		this.name = name;
		this.maxPower = maxPower;
		this.minPower = minPower;
		this.maxChange = maxChange;
		this.minChange = minChange;
		this.power = minPower;
		this.price = new Price(this);
	}
	/**
	 * 
	 * @return the name of the generator.
	 */
	public String getName() {
		return name;
	}
	/**
	 * set the name of the generator.
	 * @param name the designating name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * return the maximum possible power of the generator.
	 */
	@Override
	public double getMaxPower() {
		return this.maxPower;
	}
	/**
	 * setting the maximum possible power of the generator.
	 */
	@Override
	public void setMaxPower(double maxPower) {
		super.setMaxPower(maxPower);
		this.maxPower = maxPower;
	}
	/**
	 * return the minimum possible power of the generator.
	 */
	@Override
	public double getMinPower() {
		return this.minPower;
	}
	/**
	 * setting the minimum possible power of the generator.
	 */
	@Override
	public void setMinPower(double minPower) {
		super.setMinPower(minPower);
		this.minPower = minPower;
	}
	/**
	 * return the maximum power shift of the generator.
	 */
	@Override
	public double getMaxChange() {
		return this.maxChange;
	}
	/**
	 * setting the maximum power shift of the generator.
	 */
	@Override
	public void setMaxChange(double maxChange) {
		super.setMaxChange(maxChange);
		this.maxChange = maxChange;
	}
	/**
	 * return the minimum power shift of the generator.
	 */
	@Override
	public double getMinChange() {
		return this.minChange;
	}
	/**
	 * setting the minimum power shift of the generator.
	 */
	@Override
	public void setMinChange(double minChange) {
		super.setMinChange(minChange);
		this.minChange = minChange;
	}
	/**
	 * return the current power of the generator.
	 */
	@Override
	public double getPower() {
		return power;
	}
	/**
	 * modify the power of the generator based on requirements of the project.
	 */
	@Override
	public void setPower(double power) {
		// Check the difference between current power and the previous power
		// If it is greater than maximum change, then add the maximum change to current power.
		if (Math.abs(this.power - power) > getMaxChange()) {
			this.power += getMaxChange();
		}
		// If it is smaller than minimum change, then the current power is equal to the sum of
		// previous power and the minimum change.
		else if (Math.abs(this.power - power) < getMinChange()) {
			this.power = (power + getMinChange());
		}
		
		// Then check the current power
		// If the current power is greater than maximum power, then assign the current power to maximum power.
		if (this.power > getMaxPower()) {
			this.power = getMaxPower();
		} 
		// If it is smaller than minimum power, then it equals to minimum power.
		else if (this.power < getMinPower()) {
			this.power = getMinPower();
		} 
		// And if it is between the interval of minimum and maximum power, simply assign it to that value.
		else {
			this.power = power;
		}
	}

	@Override
	public void next() {}
	/**
	 * return the cost of the generator's providing power.
	 */
	@Override
	public double getCost() {
		return price.calculatePrice();
	}
	
}
