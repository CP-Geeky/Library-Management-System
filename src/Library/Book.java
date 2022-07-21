package Library;

public class Book {
	String bookName;
	int isbn;
	
	public Book(int isbn, String bookName) {
		super();
		this.isbn = isbn;
		this.bookName = bookName;
	}
	public Book() {
		
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public int getISBN() {
		return isbn;
	}
	public void setISBN(int isbn) {
		this.isbn = isbn;
	}

	@Override
	public String toString() {
		return "ISBN : " + isbn + "\n" +
				"Book Name : " + bookName +"\n"+
				"--------------------------------";
	}
	
	
	
}
