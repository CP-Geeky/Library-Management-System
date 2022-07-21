package Library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibraryDAO {

	int count = 0;
	Connection conn = null;
    
	// For establishing the connection
    public Connection getConnection(){

    	Connection conn = null;
        try
        {
        	Class.forName("org.postgresql.Driver");
        	conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LibraryMgmtSys", "postgres", "psql@123");
        	if(conn.isClosed()) {
        		System.out.println("Connection is closed...");
        	}else {
        		if(count < 0) {
        			System.out.println("Connection established successfully!!");
        			count++;
        		}
        	}
            
        } catch (Exception e) {
        	System.out.println(e + "Error in connecting ...");
        	
        }
		return conn; 
}
    
    
    // Adding the book 
	public void addBook(Book book) {
		
		String query = "INSERT INTO books VALUES(?, ?)";
		try {
			PreparedStatement ps = getConnection().prepareStatement(query);
			ps.setInt(1, book.getISBN());
			ps.setString(2, book.getBookName());
			
			ps.executeUpdate();
			System.out.println("Book added successfully!!");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	
	// Adding the user to the database
	public void addUser(User user) {
		
		String query = "INSERT INTO users VALUES(?, ?)";
		try {
			PreparedStatement ps = getConnection().prepareStatement(query);
			ps.setInt(1, user.getUserID());
			ps.setString(2, user.getUserName());
			
			ps.executeUpdate();
			System.out.println("User added successfully");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	
	// Issue the book to the user
	public void issueBook(IssuedBooks book) {
		 String query = "INSERT INTO issued_books VALUES(?, ?, ?, ?, ?, ?)";
		 try {
				PreparedStatement ps = getConnection().prepareStatement(query);
				ps.setInt(1, book.getIsbn());
				ps.setString(2, book.getBookName());
				ps.setString(3, book.getUserName());
				ps.setInt(4, book.getUserId());
				ps.setString(5, book.getIssueDate());
				ps.setString(6, book.getReturnDate());
			
				ps.executeUpdate();
				System.out.println("Book issued!!");
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	// Remove customer from issued_books table(when he will return the book)
	public void removeCustomerFromIssueDetails(int userId) {
		String query = "DELETE FROM issued_books WHERE user_id=?";
		try {
    		PreparedStatement ps = getConnection().prepareStatement(query);
    		ps.setInt(1, userId);
    		
    		ps.executeUpdate();	
    		System.out.println("Book returned successfully!");
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
		
	}


	// Fetching all the issued_books information
	public List<IssuedBooks> getIssuedBooksInfo() {
		List<IssuedBooks> list = null;
    	
    	try {
    		list = new ArrayList<IssuedBooks>();
    		String query = "SELECT isbn, book_name, user_id, user_name, issue_date, return_date FROM issued_books";
    		PreparedStatement pst = getConnection().prepareStatement(query);
    		ResultSet rs = pst.executeQuery();
    		while(rs.next()) {
    			IssuedBooks rm = new IssuedBooks();
    			rm.setIsbn(rs.getInt(1));
    			rm.setBookName(rs.getString(2));
    			rm.setUserId(rs.getInt(3));
    			rm.setUserName(rs.getString(4));
    			rm.setIssueDate(rs.getString(5));
    			rm.setReturnDate(rs.getString(6));
    			list.add(rm);		   			
    		}
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
		return list;
	}



	// Fetching all the details of the book
	public List<Book> getAllBooks() {
		List<Book> list = null;
    	
    	try {
    		list = new ArrayList<Book>();
    		String query = "SELECT ISBN, Book_Name FROM books";
    		PreparedStatement pst = getConnection().prepareStatement(query);
    		ResultSet rs = pst.executeQuery();
    		while(rs.next()) {
    			Book bk = new Book();
    			bk.setISBN(rs.getInt(1));
    			bk.setBookName(rs.getString(2));
    			list.add(bk);		   			
    		}
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
		return list;
	}
	

	// Removing the book from books table whenever the book is issued to the user.
	public void removeBook(int isbn) {
		String query = "DELETE FROM books WHERE isbn=?";
		try {
    		PreparedStatement ps = getConnection().prepareStatement(query);
    		ps.setInt(1, isbn);
    		
    		ps.executeUpdate();	
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
		
	}


	// For fetching the books which are at circulation.
	public List<String> getBookAtCirculation() {
		List<String> list = null;
    	try {
    		list = new ArrayList<String>();
    		String query = "SELECT book_name from issued_books";
			PreparedStatement pstmt = getConnection().prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				IssuedBooks rm = new IssuedBooks();
				
				rm.setBookName(rs.getString(1));				
				list.add(rm.getBookName());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	
	// Fetching the details when user wants to submit the book
	public List<String> getDetails(int userId) {
		String query = "SELECT user_id, return_date, isbn, book_name FROM issued_books WHERE user_id=?";
		List<String> temp = null;
		try {
			temp = new ArrayList<String>();
			PreparedStatement pstmt =  getConnection().prepareStatement(query);
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				IssuedBooks ib = new IssuedBooks();
				
				ib.setUserId(rs.getInt(1));
				
				if(ib.getUserId() == userId) {
					ib.setReturnDate(rs.getString(2));
					String date = ib.getReturnDate();
					ib.setIsbn(rs.getInt(3));
					int isbnNo = ib.getIsbn();
					ib.setBookName(rs.getString(4));
					String book = ib.getBookName();
					String ISBN = String.valueOf(isbnNo);
					temp.add(date);
					temp.add(ISBN);
					temp.add(book);
				}				
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
			
		}
		return temp;
			
	}
	
	
}
