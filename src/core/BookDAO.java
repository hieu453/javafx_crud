package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
	private static String jdbcURL;
	private static String jdbcUsername;
	private static String jdbcPassword;
	private static Connection jdbcConnection;
	
	public BookDAO(String jdbcURL, String jdbcUsername, String jdbcPassword) {
		BookDAO.jdbcURL = jdbcURL;
		BookDAO.jdbcUsername = jdbcUsername;
		BookDAO.jdbcPassword = jdbcPassword;
	}
	
	protected static void connect() {
		try {
			if (jdbcConnection == null || jdbcConnection.isClosed()) {
				Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
//				Class.forName("com.mysql.cj.jdbc.Driver");
				jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	protected static void disconnect() {
		try {
			if (!jdbcConnection.isClosed() || jdbcConnection != null) {
				jdbcConnection.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean insertBook(Book book) {
		String sql = "insert into books (code, name, author, price, quantity, image, published_year) "
				+ "values (?, ?, ?, ?, ?, ?, ?)";
		
		connect();
	
		boolean rowInserted = false;
		try {
			PreparedStatement statement = jdbcConnection.prepareStatement(sql);
			statement.setString(1, book.getCode());
			statement.setString(2, book.getBookName());
			statement.setString(3, book.getAuthor());
			statement.setFloat(4, book.getPrice());
			statement.setInt(5, book.getQuantity());
			statement.setString(6, book.getImagePath());
			statement.setInt(7, book.getPublishedYear());
			
			rowInserted = statement.executeUpdate() > 0;
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
		
		return rowInserted;
	}
	
	public static List<Book> listAllBook() {
		List<Book> listBook = new ArrayList<>();
		
		String sql = "select * from books";
		
		connect();
		
		try {
			Statement statement = jdbcConnection.createStatement();
			ResultSet results = statement.executeQuery(sql);
			
			while (results.next()) {
				String bookCode = results.getString("code");
				String bookName = results.getString("name");
				String bookAuthor = results.getString("author");
				float bookPrice = results.getFloat("price");
				int bookQuantity = results.getInt("quantity");
				String imagePath = results.getString("image");
				int bookPublishedYear = results.getInt("published_year");
				
				Book book = new Book(bookCode);
				book.setBookName(bookName);
				book.setAuthor(bookAuthor);
				book.setPrice(bookPrice);
				book.setQuantity(bookQuantity);
				book.setImagePath(imagePath);
				book.setPublishedYear(bookPublishedYear);
				
				listBook.add(book);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listBook;
	}
	
	public static boolean deleteBook(String code) {
		String sql = "delete from books where code = ?";
		
		connect();
		
		boolean rowDeleted = false;
		try {
			PreparedStatement statement = jdbcConnection.prepareStatement(sql);
			
			statement.setString(1, code);
			
			rowDeleted = statement.executeUpdate() > 0;
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
		
		return rowDeleted;
	}
	
	public static boolean updateBook(Book book, String oldCode) {
		String sql = "update books set code=?, name=?, author=?, price=?, quantity=?, image=?, published_year=? "
				+ "where code = ?";
		
		connect();
		
		boolean rowUpdated = false;
		try {
			PreparedStatement statement = jdbcConnection.prepareStatement(sql);
			statement.setString(1, book.getCode());
			statement.setString(2, book.getBookName());
			statement.setString(3, book.getAuthor());
			statement.setFloat(4, book.getPrice());
			statement.setInt(5, book.getQuantity());
			statement.setString(6, book.getImagePath());
			statement.setInt(7, book.getPublishedYear());
			statement.setString(8, oldCode);
			
			rowUpdated = statement.executeUpdate() > 0;
			
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
		
		return rowUpdated;
	}
	
	public static Book getBook(String code) {
		Book book = null;
        String sql = "select * from books where code = ?";
         
        connect();
         
        try {
			PreparedStatement statement = jdbcConnection.prepareStatement(sql);
			statement.setString(1, code);
			 
			ResultSet result = statement.executeQuery();
			 
			if (result.next()) {
			    String bookCode = result.getString("code");
			    String bookName = result.getString("name");
			    String bookAuthor = result.getString("author");
			    float bookPrice = result.getFloat("price");
			    int bookQuantity = result.getInt("quantity");
			    String imagePath = result.getString("image");
			    int bookPublishedYear = result.getInt("published_year");
			    
			    book = new Book(bookCode);
			    book.setBookName(bookName);
			    book.setAuthor(bookAuthor);
			    book.setPrice(bookPrice);
			    book.setQuantity(bookQuantity);
			    book.setImagePath(imagePath);
			    book.setPublishedYear(bookPublishedYear);
			}
			 
			result.close();
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
        return book;
	}
}
