package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import interfaces.AbstractComponent;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import vgu.consumer.ConsumerFactory;
/**
 * Control the data flow and the behavior of the GeneratorsView.
 * 
 */
public class GeneratorsViewController implements Initializable {
	
	@FXML
	private MenuBar menuBar;
	
	@FXML
	private TableView<AbstractComponent> table;
	
	@FXML
	private VBox vBox;
	
	@FXML
	private TableColumn<AbstractComponent, String> nameColumn;
	@FXML
	private TableColumn<AbstractComponent, Double> maxPowerColumn;
	@FXML
	private TableColumn<AbstractComponent, Double> minPowerColumn;
	@FXML
	private TableColumn<AbstractComponent, Double> maxChangeColumn;
	@FXML
	private TableColumn<AbstractComponent, Double> minChangeColumn;
	
	@FXML
	private HBox hBox;
	
	@FXML
	private TextField nameInput;
	@FXML
	private TextField maxPowerInput;
	@FXML
	private TextField minPowerInput;
	@FXML
	private TextField maxChangeInput;
	@FXML
	private TextField minChangeInput;
	
	@FXML
	private Button addButton;
	@FXML
	private Button deleteButton;
	@FXML
	private Button saveButton;
	
	@FXML
	private Text numOfGenerators;
	
	/**
	 * Handle event when user open the ConsumersView.
	 * In particular, the consumer view is show in the form of a table that contains all 
	 * the information of every consumer in the system by loading the ConsumersView.fxml file.
	 * @param event click the tab ConsumersView.
	 * @throws IOException
	 */
	@FXML
    void onConsumersViewClicked(ActionEvent event) throws IOException {
		Parent consumersViewParent = FXMLLoader.load(getClass().getResource("ConsumersView.fxml"));
		Scene consumersViewScene = new Scene(consumersViewParent);
		
		Stage stage = (Stage) menuBar.getScene().getWindow();
		stage.setScene(consumersViewScene);
		stage.setTitle("Consumers");
		stage.show();
    }
	
	/**
	 * Handle event when user open the GeneratorsView.
	 * In particular, the consumer view is show in the form of a table that contains all 
	 * the information of every generator in the system by loading the GeneratorsView.fxml file .
	 * @param event click the tab GeneratorsView.
	 * @throws IOException
	 */
    @FXML
    void onGeneratorsViewClicked(ActionEvent event) throws IOException {
    	Parent generatorsViewParent = FXMLLoader.load(getClass().getResource("GeneratorsView.fxml"));
		Scene generatorsViewScene = new Scene(generatorsViewParent);
		
		Stage stage = (Stage) menuBar.getScene().getWindow();
		stage.setScene(generatorsViewScene);
		stage.setTitle("Generators");
		stage.show();
    }
    /**
     * Manually adding a new generator directly in the GeneratorsView.
     * Data of this generator is directly decided by the user ( instead of generate ).
     * @param event click the add button in the GeneratorsView 
     */
    @FXML
    public void onAddConsumerClicked(ActionEvent event) {
    	String name;
    	Double maxPower, minPower, maxChange, minChange;
    	
    	try {
    		name = nameInput.getText().trim();
    		if (name.equals("")) {
    			AlertDialog.display("Missing name", "You must enter generator's name!");
    		}
    		maxPower = Double.parseDouble(maxPowerInput.getText());
        	minPower = Double.parseDouble(minPowerInput.getText());
        	maxChange = Double.parseDouble(maxChangeInput.getText());
        	minChange = Double.parseDouble(minChangeInput.getText());
    	
	    	AbstractComponent component = ConsumerFactory.generate(name, maxPower, minPower, maxChange, minChange);
	    	table.getItems().add(component);
	    	
	    	nameInput.clear();
	    	maxPowerInput.clear();
	    	minPowerInput.clear();
	    	maxChangeInput.clear();
	    	minChangeInput.clear();
	    	
	    	numOfGenerators.setText("Total: " + table.getItems().size());
    	} catch (NumberFormatException e) {
    		AlertDialog.display("Missing number", "You must perform inputting number!!!");
		}
    }
    /**
     * Manually removing a chosen generator directly in the GeneratorsView. 
     * @param event click the delete button in the GeneratorsView
     */
    @FXML
    public void onDeleteConsumerClicked(ActionEvent event) {
    	ObservableList<AbstractComponent> selectedComponents, allComponents;
    	allComponents = table.getItems();
    	selectedComponents = table.getSelectionModel().getSelectedItems();
    	
    	selectedComponents.forEach(allComponents::remove);
    	
    	numOfGenerators.setText("Total: " + table.getItems().size());
    }
    /**
     * Save the data of the GeneratorsView back to the generators.csv file 
     * and close the GeneratorsView to return to the main page of the GUI.
     * @param event click the save button in the GeneratorsView
     * @throws IOException
     */
    @FXML
    public void onSaveButtonClicked(ActionEvent event) throws IOException {
    	Parent mainParent = FXMLLoader.load(getClass().getResource("MainView.fxml"));
    	
    	DataUtils.generateGenerators(table.getItems());
    	
		Scene mainScene = new Scene(mainParent);
		Stage stage = (Stage) menuBar.getScene().getWindow();
		stage.setScene(mainScene);
		stage.setTitle("Main");
		stage.show();
    }
    /**
     * Load data of the generators from the generators.csv file for showing and proceeding.
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			List<AbstractComponent> generators = DataUtils.getGeneratorsFromCSV("generators.csv");
			table.getItems().addAll(generators);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (numOfGenerators != null) {
			numOfGenerators.setText("Total: " + table.getItems().size());
		}
	}

}
