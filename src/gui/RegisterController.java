package gui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import core.Admin;
import core.Human;
import core.HumanDAO;
import core.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class RegisterController implements Initializable {
	@FXML
	private RadioButton userRadio;
	@FXML
	private RadioButton adminRadio;
	@FXML
	private ToggleGroup group;
	@FXML
	private TextField codeTF;
	@FXML
	private Label codeCheck;
	@FXML
	private TextField usernameTF;
	@FXML
	private Label usernameEmpty;
	@FXML
	private TextField passwordTF;
	@FXML
	private Label passwordCheck;
	@FXML
	private Label invalidRegister;
	@FXML
	private ImageView registerImageView;
	@FXML
	private ImageView cancelImageView;
	@FXML
	private Button registerBtn;
	@FXML
	private Button cancelBtn;
	@FXML
	private Hyperlink loginLink;
	
	File cancelIconFile = new File("button_icons/cancel.png");
	Image cancelIcon = new Image(cancelIconFile.toURI().toString());
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		cancelImageView.setImage(cancelIcon);
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
	
	@FXML
	public void onClickCancel(ActionEvent event) {
		((Node)event.getSource()).getScene().getWindow().hide();
	}
	
	public boolean validateForm() {
		boolean result = true;
		
		resetMessage();

		if (!codeTF.getText().isEmpty()) {
			if (HumanDAO.getUser(codeTF.getText()) != null || HumanDAO.getAdmin(codeTF.getText()) != null) {
				codeCheck.setText("Mã đã tồn tại!");
				result = false;
			}
		}
		
		if ("".equals(codeTF.getText())) {
			codeCheck.setText("Code trống!");
			result = false;
		}
		
		if ("".equals(usernameTF.getText())) {
			usernameTF.setText("Username trống!");
			result = false;
		}
		
		if (!passwordTF.isDisabled() && "".equals(passwordTF.getText())) {
			passwordCheck.setText("Mật khẩu trống!");
			result = false;
		}
		
		if (!passwordTF.isDisabled() && passwordTF.getText().length() < 6 && passwordTF.getText().length() > 0) {
			passwordCheck.setText("Mật khẩu phải tối thiểu 6 ký tự!");
			result = false;
		}
		
		return result;
	}
	
	public void resetInput() {
		codeTF.setText("");
		usernameTF.setText("");
		passwordTF.setText("");
	}
	
	public void resetMessage() {
		codeCheck.setText("");
		usernameEmpty.setText("");
		passwordCheck.setText("");
		invalidRegister.setText("");
	}
	
	@FXML
	public void onClickRegister() {
		Alert alert;
		
		if (validateForm()) {
			resetMessage();
			
			int role = group.getSelectedToggle().equals(userRadio) ? 0 : 1;
			Human hm;
			
			if (role == 1) {
				hm = new Admin(codeTF.getText(), passwordTF.getText());
				hm.setFullname(usernameTF.getText());
				HumanDAO.insertHuman(hm);
				
				alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("Đăng ký admin thành công!");
				
				
				if (alert.showAndWait().get() == ButtonType.OK) {
					showLoginGUI();
				}
			}
			
			if (role == 0) {
				hm = new User(codeTF.getText());
				hm.setFullname(usernameTF.getText());
				HumanDAO.insertHuman(hm);
				
				alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("Đăng ký thành công!");
				
				if (alert.showAndWait().get() == ButtonType.OK) {
					showLoginGUI();
				}
			}
			
		} else {
			invalidRegister.setText("Lỗi đăng ký!");
		}
	}
	
	@FXML
	public void showLoginGUI() {
		codeTF.getScene().getWindow().hide();
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginScene.fxml"));
			Parent root = loader.load();
			Stage homeStage = new Stage();
			homeStage.setScene(new Scene(root));
			homeStage.setResizable(false);
			homeStage.setTitle("Login");
			homeStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
