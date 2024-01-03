package gui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import core.HRMDAO;
import core.Human;
import core.Lecturer;
import core.Student;
import core.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import services.AuthenticationService;
import services.AuthenticationServiceImp;

public class LoginController implements Initializable {
	@FXML
	private RadioButton studentRadio;
	@FXML
	private RadioButton lecturerRadio;
	@FXML
	private ToggleGroup group;
	@FXML
	private TextField codeTF;
	@FXML
	private Label codeEmpty;
	@FXML
	private TextField passwordTF;
	@FXML
	private Label passwordEmpty;
	@FXML
	private Label invalidLogin;
	@FXML
	private Button loginButton;
	@FXML
	private Button cancelButton;
	@FXML
	private ImageView loginImageView;
	@FXML
	private ImageView cancelImageView;
	
	
	File loginIconFile = new File("button_icons/login.png");
	File cancelIconFile = new File("button_icons/cancel.png");
	
    Image loginIcon = new Image(loginIconFile.toURI().toString());
    Image cancelIcon = new Image(cancelIconFile.toURI().toString());
	
	private AuthenticationService authService = new AuthenticationServiceImp();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		loginImageView.setImage(loginIcon);
		cancelImageView.setImage(cancelIcon);
	}
	
	
	public void onClickLogin(ActionEvent event) {
		if (validateForm()) {
			Human user;
			
			if (!passwordTF.isDisabled()) {
				user = new Lecturer(codeTF.getText(), passwordTF.getText());
			} else {
				user = new Student(codeTF.getText());
			}
			
			if (authService.login(user)) {
				((Node)event.getSource()).getScene().getWindow().hide();
				String loggedInName = null;
				
				if (user instanceof Lecturer) {
					loggedInName = HRMDAO.getLecturer(codeTF.getText()).getFullname(); 
				}
				
				if (user instanceof Student) {
					loggedInName = HRMDAO.getStudent(codeTF.getText()).getFullname();
				}
				
				int role = group.getSelectedToggle().equals(studentRadio) ? 0 : 1;
				UserSession.getInstance(loggedInName, role);
				showHomeGUI();
			} else {
				invalidLogin.setText("Code hoặc mật khẩu không đúng");
			}
		}
	}
	
	public void onClickCancel(ActionEvent event) {
		((Node)event.getSource()).getScene().getWindow().hide();
	}
	
	public void radioButtonChanged() {
		if (group.getSelectedToggle().equals(studentRadio)) {
			resetInput();
			passwordTF.setDisable(true);
		} else {
			resetInput();
			passwordTF.setDisable(false);
		}
	}
	
	public boolean validateForm() {
		boolean result = true;
		
		resetMessage();
		
		if ("".equals(codeTF.getText())) {
			codeEmpty.setText("Mã trống!");
			result = false;
		}
		
		if (!passwordTF.isDisabled() && "".equals(passwordTF.getText())) {
			passwordEmpty.setText("Mật khẩu trống!");
			result = false;
		}
		return result;
	}
	
	public void resetInput() {
		codeTF.setText("");
		passwordTF.setText("");
	}
	
	public void resetMessage() {
		codeEmpty.setText("");
		passwordEmpty.setText("");
		invalidLogin.setText("");
	}
	
	public void showHomeGUI() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeScene.fxml"));
			Parent root = loader.load();
			Stage homeStage = new Stage();
			homeStage.setScene(new Scene(root));
			homeStage.setResizable(false);
			homeStage.setTitle("Home");
			homeStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
