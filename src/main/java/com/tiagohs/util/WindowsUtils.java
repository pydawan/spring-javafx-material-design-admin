package com.tiagohs.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.tiagohs.MainApplication;
import com.tiagohs.controller.BaseController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WindowsUtils {
	
	public static final String BASE_APPLICATION_CSS_PATH = MainApplication.class.getResource("/css/application.css").toExternalForm();
	public static final String ICON_APP_PATH = MainApplication.class.getResource("/images/icon.png").toExternalForm();
	
	public static void replaceFxmlOnWindow(Pane root, String path) throws Exception {
        root.getChildren().clear();
        root.getChildren().add(loadFxml(path).load());
	}
	
	public static Stage openNewWindow(String fxmlPath, String title) throws Exception {
		
		Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        
        return openNewWindow(stage, fxmlPath, title);
	}
	
	public static Stage openNewWindow(Stage stage, String fxmlPath, String title) throws Exception {
		
        stage.getIcons().add(new Image(WindowsUtils.ICON_APP_PATH));
        stage.setTitle(title);
        stage.setResizable(false);
        
        FXMLLoader loader = loadFxml(fxmlPath);
		Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(BASE_APPLICATION_CSS_PATH);
        stage.setScene(scene);
        stage.show();
        
        BaseController baseController = loader.getController();
        baseController.init(stage);
        
        return stage;
	}
	
	public static FXMLLoader loadFxml(String url) {
		  
		 try (InputStream fxmlStream = WindowsUtils.class.getResourceAsStream(url)) {
			 FXMLLoader loader = new FXMLLoader();
			 loader.setLocation(WindowsUtils.class.getResource(url));
			 loader.setControllerFactory( clazz -> { return MainApplication.springContext.getBean(clazz);});
			   
			 return loader;
		 } catch (IOException ioException) {
			 throw new RuntimeException(ioException);
		 }
	}
	
	public static void createDefaultDialog(StackPane root, String title, String body, DialogAction action) {
		
		JFXDialogLayout content = new JFXDialogLayout();
		content.setHeading(new Text(title));
		content.setBody(new Text(body));
		
		JFXDialog dialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);
		
		JFXButton okButton = new JFXButton("Ok");
		okButton.setOnAction(event -> { dialog.close(); action.onAction(); });
		content.setActions(okButton);
		
		dialog.show();
	}
	
	public static void validateTextField(JFXTextField textField) {
		
		textField.focusedProperty().addListener((o, oldValue, newValue) -> {
			if (!newValue) {
				textField.validate();
			}
		});
	}

	public static void watchEvents(TextField textField, WatchListener listener) {
		textField.focusedProperty().addListener((o, oldValue, newValue) -> {
			listener.watch();
		});
	}
	
	public static boolean isTextFieldEmpty(TextField textField) {
		return textField.getText().trim().isEmpty();
	}
	
	public static boolean isTextAreaEmpty(TextArea textArea) {
		return textArea.getText().trim().isEmpty();
	}
	
	public static String getTextFromTextField(TextField textField) {
		return isTextFieldEmpty(textField) ? null : textField.getText();
	}
	
	public static double getDoubleFromTextField(TextField textField) {
		return isTextFieldEmpty(textField) ? 0.0 : Double.parseDouble(textField.getText());
	}

	public static String getTextFromTextArea(TextArea textArea) {
		return isTextAreaEmpty(textArea) ? null : textArea.getText();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" }) 
	public static void addComboBoxItens(ComboBox comboBox, List items) {
		comboBox.getItems().addAll(items);
	}
	
	@SuppressWarnings("rawtypes")
	public static Object getSelectedComboBoxItem(ComboBox comboBox) {
		return comboBox.getValue() != null ? comboBox.getValue() : null;
	}
	
	public static void textFieldNumberValidator(TextField textField) {
		textField.textProperty().addListener((observable, oldValue, newValue) -> {
		    
			if (!newValue.matches("[0-9]*")) {
				textField.setText("");
			}
			
		});
	}
	
}
