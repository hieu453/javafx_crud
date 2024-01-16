package gui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import core.Human;
import core.HumanDAO;
import core.User;
import core.Admin;
import core.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
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
	private RadioButton userRadio;
	@FXML
	private RadioButton adminRadio;
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
	private Hyperlink registerLink;
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
	
	@FXML
	public void onClickLogin(ActionEvent event) {
		if (validateForm()) {
			Human human;
			
			if (!passwordTF.isDisabled()) {
				human = new Admin(codeTF.getText(), passwordTF.getText());
			} else {
				human = new User(codeTF.getText());
			}
			
			if (authService.login(human)) {
				((Node)event.getSource()).getScene().getWindow().hide();
				String loggedInName = null;
				
				if (human instanceof Admin) {
					loggedInName = HumanDAO.getAdmin(codeTF.getText()).getFullname(); 
				}
				
				if (human instanceof User) {
					loggedInName = HumanDAO.getUser(codeTF.getText()).getFullname();
				}
				
				int role = group.getSelectedToggle().equals(userRadio) ? 0 : 1;
				UserSession.getInstance(loggedInName, role);
				showHomeGUI();
			} else {
				invalidLogin.setText("Code hoặc mật khẩu không đúng");
			}
		}
	}
	
	@FXML
	public void onClickCancel(ActionEvent event) {
		((Node)event.getSource()).getScene().getWindow().hide();
	}
	
	@FXML
	public void radioButtonChanged() {
		if (group.getSelectedToggle().equals(userRadio)) {
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
	
	@FXML
	public void showRegisterGUI(ActionEvent event) {
		((Node)event.getSource()).getScene().getWindow().hide();
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterScene.fxml"));
			Parent root = loader.load();
			Stage homeStage = new Stage();
			homeStage.setScene(new Scene(root));
			homeStage.setResizable(false);
			homeStage.setTitle("Register");
			homeStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
