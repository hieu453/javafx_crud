package gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

import core.BookDAO;
import core.Book;
import core.UserSession;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class HomeController implements Initializable {
	@FXML
	private TableView<Book> bookTBV = new TableView<Book>();
	@FXML
	private TableColumn<Book, String> codeCol;
	@FXML
	private TableColumn<Book, String> nameCol;
	@FXML
	private TableColumn<Book, String> authorCol;
	@FXML
	private TableColumn<Book, String> priceCol;
	@FXML
	private TableColumn<Book, Integer> quantityCol;
	@FXML
	private TableColumn<Book, Integer> publishedYearCol;
	@FXML
	private TextField textCode;
	@FXML
	private TextField textName;
	@FXML
	private TextField textAuthor;
	@FXML
	private TextField textPrice;
	@FXML
	private TextField textQuantity;
	@FXML
	private TextField textPublishedYear;
	@FXML
	private TextField imagePathTF;
	@FXML
	private Button btnAdd;
	@FXML
	private Button btnUpdate;
	@FXML
	private Button btnDelete;
	@FXML
	private Button chooseImage;
	@FXML
	private Label loginMessage;
	@FXML
	private Button exportBtn;
	@FXML
	private Hyperlink exitBtn;
	@FXML
	private ImageView excelImageView;
	@FXML
	private ImageView addImageView;
	@FXML
	private ImageView editImageView;
	@FXML
	private ImageView deleteImageView;
	@FXML
	private ImageView bookImageView;
	@FXML
	private ImageView chooseImageIconView;
	
	
	List<Book> bookList = BookDAO.listAllBook();
	File excelIconFile = new File("button_icons/excel.png");
	File addIconFile = new File("button_icons/add.png");
	File editIconFile = new File("button_icons/edit.png");
	File deleteIconFile = new File("button_icons/delete.png");
	File chooseImgIconFile = new File("button_icons/chooseImage.png");
	
	FileChooser fileChooser;
	File file;
	
    Image excelIcon = new Image(excelIconFile.toURI().toString());
    Image addIcon = new Image(addIconFile.toURI().toString());
    Image editIcon = new Image(editIconFile.toURI().toString());
    Image deleteIcon = new Image(deleteIconFile.toURI().toString());
    Image chooseImgIcon = new Image(chooseImgIconFile.toURI().toString());
    
    Image bookImage;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		Platform.runLater(() -> {
			excelImageView.setImage(excelIcon);
			addImageView.setImage(addIcon);
			editImageView.setImage(editIcon);
			deleteImageView.setImage(deleteIcon);
			chooseImageIconView.setImage(chooseImgIcon);
					
			codeCol.setCellValueFactory(new PropertyValueFactory<Book, String>("code"));
			nameCol.setCellValueFactory(new PropertyValueFactory<Book, String>("bookName"));
			authorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
			
			priceCol.setCellValueFactory(cellData -> {
				DecimalFormat formatter = new DecimalFormat("###,###,###");
			    String formattedCost = formatter.format(cellData.getValue().getPrice());
			    return new SimpleStringProperty(formattedCost + " VND");
			});
			
			quantityCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("quantity"));
			publishedYearCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("publishedYear"));
			
			
			ObservableList<Book> obsBookList = FXCollections.observableArrayList(bookList);
			bookTBV.setItems(obsBookList);
			
			if (UserSession.getRole() == 0) {
				btnAdd.setDisable(true);
				btnUpdate.setDisable(true);
				btnDelete.setDisable(true);
				chooseImage.setDisable(true);
				
				loginMessage.setText("Người dùng đang đăng nhập: " + UserSession.getFullname());
			} 
			
			if (UserSession.getRole() == 1){
				loginMessage.setText("Admin đang đăng nhập: " + UserSession.getFullname());
			}
		});
	}
	
	@FXML
	public void onClickRow() {
		Book bookSelected = bookTBV.getSelectionModel().getSelectedItem();
		textCode.setText(bookSelected.getCode());
		textName.setText(bookSelected.getBookName());
		textAuthor.setText(bookSelected.getAuthor());
		textPrice.setText(Float.toString(bookSelected.getPrice()));
		textQuantity.setText(Integer.toString(bookSelected.getQuantity()));
		imagePathTF.setText(bookSelected.getImagePath());
		
		File bookImage = new File(bookSelected.getImagePath());
		bookImageView.setImage(new Image(bookImage.toURI().toString()));
		
		textPublishedYear.setText(Integer.toString(bookSelected.getPublishedYear()));
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
	public void onClickChooseImageBtn(ActionEvent event) {
		fileChooser = new FileChooser();
		
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
		);
		
		Stage homeStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		file = fileChooser.showOpenDialog(homeStage);
		
		if (file != null) {
			imagePathTF.setText("img/" + file.getName());
			bookImage = new Image(file.toURI().toString());
			bookImageView.setImage(bookImage);
			
			try {
				String destinationFilePath = "img" + File.separator + file.getName();
				File destinationFile = new File(destinationFilePath);
				
				Path sourcePath = file.toPath();
	            Path destinationPath = destinationFile.toPath();
	            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	@FXML
	public void onClickAddButton() {
		Alert alert;
	
		String codeTF = textCode.getText();
		String nameTF = textName.getText();
		String authorTF = textAuthor.getText();
		String priceTF = textPrice.getText();
		String quantityTF = textQuantity.getText();
		String imagePath = imagePathTF.getText();
		String yearTF = textPublishedYear.getText();
		
		boolean isNotEmptyTF = !"".equals(codeTF) && !"".equals(nameTF) && !"".equals(authorTF) 
				&& !"".equals(priceTF) && !"".equals(quantityTF) && !"".equals(yearTF);
		
		if (isNotEmptyTF) {
			Book book = new Book(codeTF);
			book.setBookName(nameTF);
			book.setAuthor(authorTF);
			book.setPrice(Float.parseFloat(priceTF));
			book.setQuantity(Integer.parseInt(quantityTF));	
			book.setImagePath(imagePath);
			book.setPublishedYear(Integer.parseInt(yearTF));
			
			if (BookDAO.getBook(book.getCode()) == null) {
				boolean insertResult = BookDAO.insertBook(book);
				if (insertResult) {
					bookTBV.getItems().add(book);
					alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("Đã thêm sách!");
					alert.show();
				} else {
					alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("Thêm sách thất bại!");
					alert.show();
				}
			} else {
				alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Mã sách đã tồn tại!");
				alert.show();
			}
		} else {
			alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Có thông tin còn trống chưa điền!");
			alert.show();
		}
	
	}
	
	@FXML
	public void onClickUpdateButton() {
		Alert alert;
		String codeTF = textCode.getText();
		String nameTF = textName.getText();
		String authorTF = textAuthor.getText();
		String priceTF = textPrice.getText();
		String quantityTF = textQuantity.getText();
		String imagePath = imagePathTF.getText();
		String yearTF = textPublishedYear.getText();
		
		
		boolean isEmptyTF = "".equals(codeTF) || "".equals(nameTF) || "".equals(authorTF) 
				|| "".equals(priceTF) || "".equals(quantityTF) || "".equals(yearTF);
		
		if (isEmptyTF) {
			alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Có thông tin nào đó trống!");
			alert.show();
			return;
		}
		
		
		int selectedIndex = bookTBV.getSelectionModel().getSelectedIndex();
		Book selectedBook = bookTBV.getItems().get(selectedIndex);
		String oldCode = selectedBook.getCode();
		
		//Truong hop tim thay sach voi ma sach cu va textcode bang voi code cu
		//Tuc la cap nhat sach voi ma sach giu nguyen
		if (BookDAO.getBook(oldCode) != null && textCode.getText().equals(oldCode)) {
			if (selectedIndex >=0) {
				selectedBook.setCode(textCode.getText());
				selectedBook.setBookName(textName.getText());
				selectedBook.setAuthor(authorTF);
				selectedBook.setPrice(Float.parseFloat(priceTF));
				selectedBook.setQuantity(Integer.parseInt(quantityTF));
				selectedBook.setImagePath(imagePath);
				selectedBook.setPublishedYear(Integer.parseInt(yearTF));
				
				boolean updatedResult = BookDAO.updateBook(selectedBook, oldCode);
				
				if (updatedResult) {
					bookTBV.getItems().set(selectedIndex, selectedBook);
					
					textCode.setText("");
					textName.setText("");
					textAuthor.setText("");
					textPrice.setText("");
					textQuantity.setText("");
					imagePathTF.setText("");
					textPublishedYear.setText("");
					
					alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("Cập nhật sách thành công!");
					alert.show();
				} else {
					alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("Cập nhật sách thất bại!");
					alert.show();
				}
			} 
		//Truong hop khong tim thay sach voi ma sach o textCode va textCode khac code cu
		//Tuc la cap nhat sach voi ma sach moi khong trung voi ma sach khac trong database
		} else if (BookDAO.getBook(textCode.getText()) == null && !textCode.getText().equals(oldCode)) {
			if (selectedIndex >=0) {
				selectedBook.setCode(textCode.getText());
				selectedBook.setBookName(textName.getText());
				selectedBook.setAuthor(authorTF);
				selectedBook.setPrice(Float.parseFloat(priceTF));
				selectedBook.setQuantity(Integer.parseInt(quantityTF));
				selectedBook.setImagePath(imagePath);
				selectedBook.setPublishedYear(Integer.parseInt(yearTF));
				boolean updatedResult = BookDAO.updateBook(selectedBook, oldCode);
				
				if (updatedResult) {
					bookTBV.getItems().set(selectedIndex, selectedBook);
					
					textCode.setText("");
					textName.setText("");
					textAuthor.setText("");
					textPrice.setText("");
					textQuantity.setText("");
					imagePathTF.setText("");
					textPublishedYear.setText("");
					
					alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("Cập nhật sách thành công!");
					alert.show();
				} else {
					alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("Cập nhật sách thất bại!");
					alert.show();
				}
			} 
		} else {
			alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Mã sách đã tồn tại!");
			alert.show();
		}
	}
	
	@FXML
	public void onClickDeleteButton() {
		Alert alert;
		String codeTF = textCode.getText();
		String nameTF = textName.getText();
		String authorTF = textAuthor.getText();
		String priceTF = textPrice.getText();
		String quantityTF = textQuantity.getText();
		String yearTF = textPublishedYear.getText();
		
		boolean isEmptyTF = "".equals(codeTF) || "".equals(nameTF) || "".equals(authorTF) 
				|| "".equals(priceTF) || "".equals(quantityTF) || "".equals(yearTF);
		
		if (isEmptyTF) {
			alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Có thông tin nào đó trống!");
			alert.show();
			return;
		}
		
		alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("Bạn có chắc chắn muốn xóa sách đang chọn không?");
		
		if (alert.showAndWait().get() == ButtonType.OK) {
			int selectedIndex = bookTBV.getSelectionModel().getSelectedIndex();
			Book book = bookTBV.getItems().get(selectedIndex);
			String bookCode = book.getCode(); 
			
			if (selectedIndex >=0) {
				boolean deletedResult = BookDAO.deleteBook(bookCode);
				if (deletedResult) {
					bookTBV.getItems().remove(selectedIndex);
					
					textCode.setText("");
					textName.setText("");
					textAuthor.setText("");
					textPrice.setText("");
					textQuantity.setText("");
					imagePathTF.setText("");
					bookImageView.setImage(null);
					textPublishedYear.setText("");
					
					alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("Đã xóa sách!");
					alert.show();
				} else {
					alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("Xóa sách thất bại!");
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

        for (int j = 0; j < bookTBV.getColumns().size(); j++) {
            row.createCell(j).setCellValue(bookTBV.getColumns().get(j).getText());
        }

        for (int i = 0; i < bookTBV.getItems().size(); i++) {
            row = spreadsheet.createRow(i + 1);
            for (int j = 0; j < bookTBV.getColumns().size(); j++) {
                if(bookTBV.getColumns().get(j).getCellData(i) != null) { 
                    row.createCell(j).setCellValue(bookTBV.getColumns().get(j).getCellData(i).toString()); 
                }
                else {
                    row.createCell(j).setCellValue("");
                }   
            }
        }

        try {
			FileOutputStream fileOut = new FileOutputStream("ds.xls");
			workbook.write(fileOut);
			fileOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText("Xuất file excel thành công!");
        alert.setContentText("Đã xuất file excel danh sách các cuốn sách.");
        alert.show();
	}
}
