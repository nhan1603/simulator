package gui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import interfaces.AbstractComponent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableView;
import vgu.consumer.Consumer;
import vgu.consumer.ConsumerFactory;
import vgu.control.Control;
import vgu.generator.Generator;
import vgu.generator.GeneratorFactory;

public class DataUtils {
	/**
	 * Write data of the consumers into the consumers.csv file.
	 * @param consumers parameter of a list that suppose to contains all consumers
	 * @throws FileNotFoundException handle the errors if the consumers.csv file is not exist
	 */
	public static void generateConsumers(List<AbstractComponent> consumers) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(new FileOutputStream("consumers.csv", false));
		
		for (AbstractComponent consumer : consumers) {
			writer.print(((Consumer) consumer).getName());
			writer.print(",");
			writer.print(consumer.getMaxPower());
			writer.print(",");
			writer.print(consumer.getMinPower());
			writer.print(",");
			writer.print(consumer.getMaxChange());
			writer.print(",");
			writer.print(consumer.getMinChange());
			writer.print("\n");
		}
		
		writer.close();
	}
	/**
	 * Write data of the generators into the generators.csv file.
	 * @param generators parameter of a list that suppose to contains all generators
	 * @throws FileNotFoundException handle the errors if the generators.csv file is not exist
	 */
	public static void generateGenerators(List<AbstractComponent> generators) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(new FileOutputStream("generators.csv", false));
		
		for (AbstractComponent generator : generators) {
			writer.print(((Generator) generator).getName());
			writer.print(",");
			writer.print(generator.getMaxPower());
			writer.print(",");
			writer.print(generator.getMinPower());
			writer.print(",");
			writer.print(generator.getMaxChange());
			writer.print(",");
			writer.print(generator.getMinChange());
			writer.print("\n");
		}
		
		writer.close();
	}
	/**
	 * Load the data of the consumers in the consumers.csv file into the variables in the code.
	 * @param fileName name of the csv file
	 * @return consumers which is the List of AbstractComponent that contains information of the consumers
	 * @throws IOException
	 */
	public static List<AbstractComponent> getConsumersFromCSV(String fileName) throws IOException {
		List<AbstractComponent> consumers = new ArrayList<>();
		Path path = Paths.get(fileName);
		BufferedReader reader = Files.newBufferedReader(path);
		String line = reader.readLine();
		
		while (line != null) {
			String[] attributes = line.split(",");
			AbstractComponent consumer = createConsumer(attributes);
			consumers.add(consumer);
			line = reader.readLine();
		}
		
		return consumers;
	}
	/**
	 * Manually create a new consumer object by input data by user.
	 * @param metadata list of data that is used for generating a single consumer component
	 * @return a consumer component by using the method from the ConsumerFactory
	 */
	private static AbstractComponent createConsumer(String[] metadata) {
		String name = metadata[0];
		Double maxPower = Double.parseDouble(metadata[1]);
		Double minPower = Double.parseDouble(metadata[2]);
		Double maxChange = Double.parseDouble(metadata[3]);
		Double minChange = Double.parseDouble(metadata[4]);
		
		return ConsumerFactory.generate(name, maxPower, minPower, maxChange, minChange);
	}
	/**
	 * Load the data of the generators in the generators.csv file into the variables in the code.
	 * @param fileName name of the csv file
	 * @return consumers which is the List of AbstractComponent that contains information of the generators
	 * @throws IOException
	 */
	public static List<AbstractComponent> getGeneratorsFromCSV(String fileName) throws IOException {
		List<AbstractComponent> generators = new ArrayList<>();
		Path path = Paths.get(fileName);
		BufferedReader reader = Files.newBufferedReader(path);
		String line = reader.readLine();
		
		while (line != null) {
			String[] attributes = line.split(",");
			AbstractComponent generator = createGenerator(attributes);
			generators.add(generator);
			line = reader.readLine();
		}
		
		return generators;
	}
	
	/**
	 * Manually create a new generator object by input data by user.
	 * @param metadata list of data that is used for generating a single generator component
	 * @return a generator component by using the method from the GeneratorFactory
	 */
	private static AbstractComponent createGenerator(String[] metadata) {
		String name = metadata[0];
		Double maxPower = Double.parseDouble(metadata[1]);
		Double minPower = Double.parseDouble(metadata[2]);
		Double maxChange = Double.parseDouble(metadata[3]);
		Double minChange = Double.parseDouble(metadata[4]);
		
		return GeneratorFactory.generate(name, maxPower, minPower, maxChange, minChange);
	}
	/**
	 * 
	 * @param iteration the iteration of the system
	 * @param control the control class in the system
	 * @return The list of data of the frequency for each iteration of the system
	 */
	public static ObservableList<XYChart.Series<Number, Number>> getFrequencyChartSeries(int iteration, Control control) {
		XYChart.Series<Number, Number> frequencySeries = new XYChart.Series<Number, Number>();
		frequencySeries.setName("Frequency");
		
		for (int i = 0; i < iteration; i++) {
			frequencySeries.getData().add(new XYChart.Data<>(i + 1, control.getFrequency()));
			
			for (AbstractComponent consumer : control.getConsumers()) {
				consumer.next();
			}
			
			control.nextIteration();
		}
		
		ObservableList<XYChart.Series<Number, Number>> observableData = 
				FXCollections.<XYChart.Series<Number, Number>>observableArrayList();
		observableData.add(frequencySeries);
		
		return observableData;
	}
	/**
	 * 
	 * @param iteration the iteration of the system
	 * @param control the control class in the system
	 * @return The list of data of the demand power and supply power for each iteration of the system
	 */
	public static List<XYChart.Series<String, Number>> getDemandSupplyChartSeries(int iteration, Control control) {
		List<XYChart.Series<String, Number>> demandSupplySeries = new ArrayList<>();
		XYChart.Series<String, Number> demandSeries = new XYChart.Series<>();
		XYChart.Series<String, Number> supplySeries = new XYChart.Series<>();
		demandSeries.setName("Demand");
		supplySeries.setName("Supply");
		
		for (int i = 1; i <= iteration; i++) {
			demandSeries.getData().add(new XYChart.Data<>(String.valueOf(i), control.getTotalDemand()));
			supplySeries.getData().add(new XYChart.Data<>(String.valueOf(i), control.getTotalSupply()));
			
			for (AbstractComponent consumer : control.getConsumers()) {
				consumer.next();
			}
			
			control.nextIteration();
		}
		
		demandSupplySeries.add(demandSeries);
		demandSupplySeries.add(supplySeries);
		
		return demandSupplySeries;
	}
	/**
	 * 
	 * @param iteration the iteration of the system
	 * @param control the control class in the system
	 * @return The list of data of the cost and profit for each iteration of the system
	 */
	public static List<XYChart.Series<String, Number>> getCostProfitChartSeries(int iteration, Control control) {
		List<XYChart.Series<String, Number>> costProfitSeries = new ArrayList<>();
		XYChart.Series<String, Number> costSeries = new XYChart.Series<>();
		XYChart.Series<String, Number> profitSeries = new XYChart.Series<>();
		costSeries.setName("Cost");
		profitSeries.setName("Profit");
		
		for (int i = 1; i <= iteration; i++) {
			costSeries.getData().add(new XYChart.Data<>(String.valueOf(i), control.getCost()));
			profitSeries.getData().add(new XYChart.Data<>(String.valueOf(i), control.getProfit()));
			
			for (AbstractComponent consumer : control.getConsumers()) {
				consumer.next();
			}
			
			control.nextIteration();
		}
		
		costProfitSeries.add(costSeries);
		costProfitSeries.add(profitSeries);
		
		return costProfitSeries;
	}
	
	/**
	 * Adding the additional consumers which are manually created into the consumers list in the control class.
	 * @param control the control class in the system
	 * @param consumers the list of the consumer parsed into the parameter
	 */
	public static void addConsumers(Control control, List<AbstractComponent> consumers) {
		consumers.forEach(consumer -> control.addConsumer(consumer));
	}
	
	/**
	 * Adding the additional generators which are manually created into the generators list in the control class.
	 * @param control the control class in the system
	 * @param generators the list of the generators parsed into the parameter
	 */
	public static void addGenerators(Control control, List<AbstractComponent> generators) {
		generators.forEach(generator -> control.addGenerator(generator));
	}
	/**
	 * Adding the additional consumers which are manually created into the showing table of consumers.
	 * @param control the control class in the system
	 * @param table the showing table which contains information of the consumers aka consumers view
	 */
	public static void addConsumersToTable(TableView<AbstractComponent> table, Control control) {
		table.getItems().addAll(FXCollections.observableArrayList(control.getConsumers()));
	}
	/**
	 * Adding the additional generators which are manually created into the showing table of generators.
	 * @param control the control class in the system
	 * @param table the showing table which contains information of the generators aka generators view
	 */
	public static void addGeneratorsToTable(TableView<AbstractComponent> table, Control control) {
		table.getItems().addAll(FXCollections.observableArrayList(control.getGenerators()));
	}
	
}
