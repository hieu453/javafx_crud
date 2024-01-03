package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import core.HRMDAO;
import core.Student;
import core.UserSession;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class HomeController implements Initializable {
	@FXML
	private TableView<Student> studentTBV = new TableView<Student>();
	@FXML
	private TableColumn<Student, String> fullnameCol;
	@FXML
	private TableColumn<Student, String> codeCol;
	@FXML
	private TableColumn<Student, String> classCol;
	@FXML
	private TextField textCode;
	@FXML
	private TextField textName;
	@FXML
	private TextField textClass;
	@FXML
	private Button btnAdd;
	@FXML
	private Button btnUpdate;
	@FXML
	private Button btnDelete;
	@FXML
	private Label loginMessage;
	@FXML
	private Button exportBtn;
	@FXML
	private ImageView excelImageView;
	@FXML
	private ImageView addImageView;
	@FXML
	private ImageView editImageView;
	@FXML
	private ImageView deleteImageView;
	
	
	List<Student> studentList = HRMDAO.listAllStudent();
	File excelIconFile = new File("button_icons/excel.png");
	File addIconFile = new File("button_icons/add.png");
	File editIconFile = new File("button_icons/edit.png");
	File deleteIconFile = new File("button_icons/delete.png");
	
    Image excelIcon = new Image(excelIconFile.toURI().toString());
    Image addIcon = new Image(addIconFile.toURI().toString());
    Image editIcon = new Image(editIconFile.toURI().toString());
    Image deleteIcon = new Image(deleteIconFile.toURI().toString());
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		Platform.runLater(() -> {
			excelImageView.setImage(excelIcon);
			addImageView.setImage(addIcon);
			editImageView.setImage(editIcon);
			deleteImageView.setImage(deleteIcon);
			
			
			codeCol.setCellValueFactory(new PropertyValueFactory<Student, String>("code"));
			fullnameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("fullname"));
			classCol.setCellValueFactory(new PropertyValueFactory<Student, String>("class_"));
			
			
			ObservableList<Student> obsStudentList = FXCollections.observableArrayList(studentList);
			studentTBV.setItems(obsStudentList);
			
			if (UserSession.getRole() == 0) {
				btnAdd.setDisable(true);
				btnUpdate.setDisable(true);
				btnDelete.setDisable(true);
				
				loginMessage.setText("Học sinh đang đăng nhập: " + UserSession.getFullname());
			} 
			
			if (UserSession.getRole() == 1){
				loginMessage.setText("Giảng viên đang đăng nhập: " + UserSession.getFullname());
			}
		});
	}
	
	@FXML
	public void onClickRow() {
		Student userSelected = studentTBV.getSelectionModel().getSelectedItem();
		textCode.setText(userSelected.getCode());
		textName.setText(userSelected.getFullname());
		textClass.setText(userSelected.getClass_());
	}
	
	@FXML
	public void onCLickExitButton(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("Bạn có chắc chắn muốn thoát?");
		if (alert.showAndWait().get() == ButtonType.OK) {
			((Node)event.getSource()).getScene().getWindow().hide();
		}
	}
	
	@FXML
	public void onClickAddButton() {
		String codeTF = textCode.getText();
		String nameTF = textName.getText();
		String classTF = textClass.getText();
		Alert alert;
		
		if (!"".equals(codeTF) && !"".equals(nameTF) && !"".equals(classTF)) {
			Student std = new Student(codeTF);
			std.setFullname(nameTF);
			std.setClass_(classTF);
			
			if (HRMDAO.getStudent(std.getCode()) == null) {
				boolean insertResult = HRMDAO.insertStudent(std);
				if (insertResult) {
					studentTBV.getItems().add(std);
					alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("Đã thêm sinh viên!");
					alert.show();
				} else {
					alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("Thêm sinh viên thất bại!");
					alert.show();
				}
			} else {
				alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Mã sinh viên đã tồn tại!");
				alert.show();
			}
		} else {
			alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Chưa điền mã hoặc tên hoặc lớp!");
			alert.show();
		}
	}
	
	@FXML
	public void onClickUpdateButton() {
		Alert alert;
		String codeTF = textCode.getText();
		String nameTF = textName.getText();
		String classTF = textClass.getText();
		
		if ("".equals(codeTF) || "".equals(nameTF) || "".equals(classTF)) {
			alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Chưa có mã hoặc tên hoặc lớp!");
			alert.show();
			return;
		}
		
		
		int selectedIndex = studentTBV.getSelectionModel().getSelectedIndex();
		Student selectedStudent = studentTBV.getItems().get(selectedIndex);
		String oldCode = selectedStudent.getCode();
		
		//Truong hop tim thay sinh vien voi msv cu va textcode bang voi code cu
		//Tuc la cap nhat sinh vien voi msv giu nguyen
		if (HRMDAO.getStudent(oldCode) != null && textCode.getText().equals(oldCode)) {
			if (selectedIndex >=0) {
				selectedStudent.setCode(textCode.getText());
				selectedStudent.setFullname(textName.getText());
				selectedStudent.setClass_(textClass.getText());
				boolean updatedResult = HRMDAO.updateStudent(selectedStudent, oldCode);
				
				if (updatedResult) {
					studentTBV.getItems().set(selectedIndex, selectedStudent);
					textCode.setText("");
					textName.setText("");
					textClass.setText("");
					alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("Cập nhật sinh viên thành công!");
					alert.show();
				} else {
					alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("Cập nhật sinh viên thất bại!");
					alert.show();
				}
			} 
		//Truong hop khong tim thay sinh vien voi ma sinh vien o textCode va textCode khac code cu
		//Tuc la cap nhat sinh vien voi ma sinh vien moi khong trung voi msv khac trong database
		} else if (HRMDAO.getStudent(textCode.getText()) == null && !textCode.getText().equals(oldCode)) {
			if (selectedIndex >=0) {
				selectedStudent.setCode(textCode.getText());
				selectedStudent.setFullname(textName.getText());
				selectedStudent.setClass_(textClass.getText());
				boolean updatedResult = HRMDAO.updateStudent(selectedStudent, oldCode);
				
				if (updatedResult) {
					studentTBV.getItems().set(selectedIndex, selectedStudent);
					textCode.setText("");
					textName.setText("");
					textClass.setText("");
					alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("Cập nhật sinh viên thành công!");
					alert.show();
				} else {
					alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("Cập nhật sinh viên thất bại!");
					alert.show();
				}
			} 
		} else {
			alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Mã sinh viên đã tồn tại!");
			alert.show();
		}
	}
	
	@FXML
	public void onClickDeleteButton() {
		Alert alert;
		String codeTF = textCode.getText();
		String nameTF = textName.getText();
		String classTF = textClass.getText();
		
		if ("".equals(codeTF) || "".equals(nameTF) || "".equals(classTF)) {
			alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Chưa có mã hoặc tên hoặc lớp!");
			alert.show();
			return;
		}
		
		alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("Bạn có chắc chắn muốn xóa sinh viên đang chọn không?");
		
		if (alert.showAndWait().get() == ButtonType.OK) {
			int selectedIndex = studentTBV.getSelectionModel().getSelectedIndex();
			Student std = studentTBV.getItems().get(selectedIndex);
			String studentCode = std.getCode(); 
			
			if (selectedIndex >=0) {
				boolean deletedResult = HRMDAO.deleteStudent(studentCode);
				if (deletedResult) {
					studentTBV.getItems().remove(selectedIndex);
					textCode.setText("");
					textName.setText("");
					textClass.setText("");
					alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("Đã xóa sinh viên!");
					alert.show();
				} else {
					alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("Xóa sinh viên thất bại!");
					alert.show();
				}
			} 
		}
	}
	
	@FXML
	public void onClickExportButton() {
	 	Workbook workbook = new HSSFWorkbook();
        Sheet spreadsheet = workbook.createSheet("sample");

        Row row = spreadsheet.createRow(0);

        for (int j = 0; j < studentTBV.getColumns().size(); j++) {
            row.createCell(j).setCellValue(studentTBV.getColumns().get(j).getText());
        }

        for (int i = 0; i < studentTBV.getItems().size(); i++) {
            row = spreadsheet.createRow(i + 1);
            for (int j = 0; j < studentTBV.getColumns().size(); j++) {
                if(studentTBV.getColumns().get(j).getCellData(i) != null) { 
                    row.createCell(j).setCellValue(studentTBV.getColumns().get(j).getCellData(i).toString()); 
                }
                else {
                    row.createCell(j).setCellValue("");
                }   
            }
        }

        try {
			FileOutputStream fileOut = new FileOutputStream("dssv.xls");
			workbook.write(fileOut);
			fileOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText("Xuất file excel thành công!");
        alert.setContentText("Đã xuất file excel danh sách sinh viên.");
        alert.show();
	}
}
