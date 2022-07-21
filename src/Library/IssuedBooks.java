package Library;

public class IssuedBooks {
	int isbn, due, userId;
	String bookName, userName, issueDate, returnDate;
	
	
	
	public IssuedBooks(int isbn, String bookName, String userName,int userId,  String issueDate, String returnDate) {
		super();
		this.isbn = isbn;
		this.bookName = bookName;
		this.userName = userName;
		this.userId = userId;
		this.issueDate = issueDate;
		this.returnDate = returnDate;
	}
	
	public IssuedBooks() {
	}

	
	
	public int getIsbn() {
		return isbn;
	}

	public void setIsbn(int isbn) {
		this.isbn = isbn;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	@Override
	public String toString() {

		return 
				"ISBN : " + isbn + "\n" +
				"User ID : " + userId +"\n"+
				"User Name : " + userName + "\n"+
				"Book Name : " + bookName + "\n" +
				"Issue Date : " + issueDate +"\n"+
				"Return Date : " + returnDate + "\n" +
				"------------------------------------------";
	}
	
	
	
	
}
