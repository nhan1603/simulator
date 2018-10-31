package gui;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

import interfaces.AbstractComponent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import vgu.consumer.ConsumerFactory;
import vgu.control.Control;
import vgu.generator.GeneratorFactory;

/**
 * Control the data flow and the behavior of the main view of the GUI.
 * 
 */
public class MainViewController implements Initializable {
	
	private Control control;
	
	@FXML
	private MenuBar menuBar;
	
	@FXML
	private TextField consumerSize;
	@FXML
	private TextField consumerAvgPower;
	@FXML
	private TextField consumerStd;
	
	@FXML
	private TextField generatorSize;
	@FXML
	private TextField generatorTotalPower;
	@FXML
	private TextField generatorStartPower;
	
	@FXML
	private Button submitConsumer;
	
	@FXML
	private Button submitGenerator;
	
	@FXML
	private Button resultButton;
	
	/**
	 * Handle event when user open the ConsumersView.
	 * In particular, the consumer view is show in the form of a table that contains all 
	 * the information of every consumer in the system by loading the ConsumersView.fxml file.
	 * @param event click the tab ConsumersView.
	 * @throws IOException
	 */
	@FXML
    void onConsumersViewClicked(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ConsumersView.fxml"));
    	Parent consumersViewParent = loader.load();
    	
		Scene consumersViewScene = new Scene(consumersViewParent);
		Stage stage = (Stage) menuBar.getScene().getWindow();
		stage.setScene(consumersViewScene);
		stage.setTitle("Consumers");
		stage.show();
    }
	
	/**
	 * Handle event when user open the GeneratorsView.
	 * In particular, the consumer view is show in the form of a table that contains all 
	 * the information of every generator in the system by loading the GeneratorsView.fxml file.
	 * @param event click the tab GeneratorsView.
	 * @throws IOException
	 */
    @FXML
    void onGeneratorsViewClicked(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GeneratorsView.fxml"));
    	Parent generatorsViewParent = loader.load();
    	
		Scene generatorsViewScene = new Scene(generatorsViewParent);
		Stage stage = (Stage) menuBar.getScene().getWindow();
		stage.setScene(generatorsViewScene);
		stage.setTitle("Generators");
		stage.show();
    }
    /**
     * Handle event when user click the submit consumer button.
     * In particular, when the user click on the submit consumer button after inputing all the required fields, 
     * the main view switch into the ConsumersView.
     * A range of consumers is generated based on the input numbers and is shown in the ConsumersView.
     * If the user does not input all the required fields, a message that reminds the user to do so is pop-up.
     * @param event click the submit consumer button.
     * @throws IOException
     */
    @FXML
    public void onSubmitConsumerClicked(ActionEvent event) throws IOException {
    	Integer amount, avgPower, std;
    	
    	try {
    		amount = Integer.parseInt(consumerSize.getText().trim());
        	avgPower = Integer.parseInt(consumerAvgPower.getText());
        	std = Integer.parseInt(consumerStd.getText());
        	
        	List<AbstractComponent> consumers = ConsumerFactory.generate(amount, avgPower, std);
        	DataUtils.addConsumers(control, consumers);
        	DataUtils.generateConsumers(control.getConsumers());
        	
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("ConsumersView.fxml"));
        	Parent consumersViewParent = loader.load();
    		Scene consumersViewScene = new Scene(consumersViewParent);
    		
    		Stage stage = (Stage) menuBar.getScene().getWindow();
    		stage.setScene(consumersViewScene);
    		stage.setTitle("Consumers");
    		stage.show();
    	} catch (NumberFormatException nfe) {
			AlertDialog.display("Missing Number", "You must perform inputting number!!!");
		}
    }
    /**
     * Handle event when user click the submit generator button.
     * In particular, when the user click on the submit generator button after inputing all the required fields, 
     * the main view switch into the GeneratorsView.
     * A range of generators is generated based on the input numbers and is shown in the GeneratorsView.
     * If the user does not input all the required fields, a message that reminds the user to do so is pop-up.
     * @param event click the submit generator button
     * @throws IOException
     */
    @FXML
    public void onSubmitGeneratorClicked(ActionEvent event) throws IOException {
    	Integer amount;
    	Double totalPower, startPower;
    	
    	try {
    		amount = Integer.parseInt(generatorSize.getText().trim());
        	totalPower = Double.parseDouble(generatorTotalPower.getText());
        	startPower = Double.parseDouble(generatorStartPower.getText());
    		List<AbstractComponent> generators = GeneratorFactory.generate(amount, totalPower, startPower);
        	DataUtils.addGenerators(control, generators);
        	DataUtils.generateGenerators(control.getGenerators());
        	
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("GeneratorsView.fxml"));
        	Parent generatorsViewParent = loader.load();
    		Scene generatorsViewScene = new Scene(generatorsViewParent);
    		
    		Stage stage = (Stage) menuBar.getScene().getWindow();
    		stage.setScene(generatorsViewScene);
    		stage.setTitle("Generators");
    		stage.show();
    	} catch (NumberFormatException nfe) {
			AlertDialog.display("Missing Number", "You must perform inputting number!!!");
		}
    }
    /**
     * Handle event when user click the result button.
     * In particular, when the user click on the result button, the StatisticsView is pop-up to show the simulation 
     * of the PowerGrid.
     * If the user does not initialize either the consumers or the generators, a message that reminds the user to do so is pop-up.
     * @param event click the result button
     * @throws IOException
     */
    @FXML
    public void onResultButtonClicked(ActionEvent event) throws Exception {
    	Path consumersFilePath = Paths.get("consumers.csv");
    	Path generatorsFilePath = Paths.get("generators.csv");
    	
    	if (Files.exists(consumersFilePath) && Files.exists(generatorsFilePath)) {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("StatisticsView.fxml"));
        	Parent statisticsViewParent = loader.load();
    		Scene statisticsViewScene = new Scene(statisticsViewParent);
    		
    		Stage stage = new Stage();
    		stage.initModality(Modality.APPLICATION_MODAL);
    		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
    			@Override
    			public void handle(WindowEvent event) {
    				StatisticsViewController.resetIteration();
    			}
    		});
    		
    		stage.setScene(statisticsViewScene);
    		stage.setTitle("Statistics");
    		stage.showAndWait();
    	} else {
    		AlertDialog.display("Missing file", "You must provide information before seeing its result!");
    	}
    }
    /**
     * Initialize the control object that controls all the data flow in the system.
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		control = new Control();
	}

}
