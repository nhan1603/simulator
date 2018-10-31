package vgu.price;

import interfaces.AbstractComponent;
import vgu.consumer.Consumer;
import vgu.generator.Generator;

public class Price {
	
	private AbstractComponent component;
	
	public Price(AbstractComponent component) {
		this.component = component;
	}
	
	/**
	 * Get type of the component
	 * @param component		either the consumer or a generator , each type will return different value for calculation
	 * @return 1 if type of component is consumer 
	 * 			2 if its type is generator
	 * @throws IllegalArgumentException for if the type of the component is neither the Consumer nor the Generator, the system will not crash  
	 */
	public int getType(AbstractComponent component) {
		if (component instanceof Consumer) {
			return 1;
		} else if (component instanceof Generator) {
			return 2;
		} else {
			throw new IllegalArgumentException("Unknown type: " + component.toString());
		}
	}
	/**
	 * Calculate the price of the providing power or the profit from the used power
	 * @return the calculated cost of the consumer if the type is 1
	 * @return the calculated cost of the generator if the type is 2
	 * @return 0 if the type is neither 1 nor 2
	 */
	
	public double calculatePrice() {
		if (getType(this.component) == 1) {
			return consumerCost();
		} else if (getType(this.component) == 2) {
			return generatorCost();
		} else {
			return 0;
		}
	}
	
	/**
	 * Calculate cost for consumer.
	 * @return used power of consumer. (since its cost equals to power times 1)
	 */
	private double consumerCost() {
		return this.component.getPower();
	}
	
	/**
	 * Calculate cost for generator.
	 * @return generated power of generator.
	 */
	private double generatorCost() {
		double baseCost = 50;
		
		if (this.component.getMaxPower() < 2500 || 
				this.component.getMaxChange() > (this.component.getMaxPower() / 2) ||
				this.component.getMinChange() < (this.component.getMaxPower() / 2)) {
			baseCost += 25;
		}
		
		return baseCost * this.component.getPower() / 100;
	}
	
}
