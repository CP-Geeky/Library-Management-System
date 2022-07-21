package Library;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) {
		
		System.out.println("------ LIBRARY MANAGEMENT SYSTEM ------");
		Scanner sc = new Scanner(System.in);
        while (true) {
            printMenu();
            int input = sc.nextInt();
            if (input == 9)
                return;
    
            if (input >= 1 && input <7)
                handleInput(input);
            else
                System.out.println("error... invalid input");
        }

	}
	
	 /*-----------  MENU ---------*/
	public static void printMenu() {

		System.out.println("Press 1 to add book");
        System.out.println("Press 2 to issue a book");
        System.out.println("Press 3 to return book");
        System.out.println("Press 4 to print complete issue details");
        System.out.println("Press 5 to display available books");
        System.out.println("Press 6 to display the book which is at circulation");
        
	}
	 /*----------- COUNT THE NUMBER OF DIGITS IN ISBN ---------*/
    
    public static int length(int num) {
    	int digits = 0;
    	while(num != 0) {
    		digits++;
    		num/=10;		
    	}
    	return digits;
    }
    
    /*-----------  COUNT THE NUMBER OF DAYS BETWEEN ISSUED DATE AND RETURNED DATE ---------*/
   
    public static long findDays(String endDate) {
    	final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//    	Date date = new Date();
//    	LocalDateTime now = LocalDateTime.now();
    	LocalDate lt = LocalDate.now(); 
    	final LocalDate firstDate = LocalDate.parse(endDate, formatter);
        final long days = ChronoUnit.DAYS.between(firstDate, lt);
        return days;
    }
 
 /*----------- CHECK WHETHER THE DATE IS IN VALID FORMAT OR NOT ---------*/
    public static boolean isValidFormat(String value) {
    	String format = "dd/MM/yyyy";
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }
    
    /*----------- CALCULATE DUE  ---------*/
    public static long getDue(long days) {
    	return 10*days;
    }
    
    
    /*-----------  INPUT HANDLING ---------*/
	public static void handleInput(int input){
		Scanner sc = new Scanner(System.in);
		int userID, isbn;
		String bookName, userName, issueDate, returnDate;
		
		LibraryDAO dao = new LibraryDAO();
		
		switch(input) {
		
			// For adding the book information into the database
			case 1:
				System.out.println("Enter the name of the book : ");
				bookName = sc.next();
				System.out.println("Enter the ISBN no : ");
				isbn = sc.nextInt();
				
				//Validating the inputs
				if(length(isbn) == 6 && bookName.length() >= 5) {
					Book book = new Book(isbn, bookName);
					dao.addBook(book);	
				}
				else if(length(isbn) < 6){
					System.out.println("Enter the correct ISBN number!");
				}
				else {
					System.out.println("Enter the book name or author name of valid length");
				}
				break;
				
			// Issue the book to the user 
			case 2:
				System.out.println("Enter the ISBN number : ");
				isbn = sc.nextInt();
				System.out.println("Enter the name of the book : ");
				bookName = sc.next();
				System.out.println("Enter the user name : ");
				userName = sc.next();
				System.out.println("Enter the user ID : ");
				userID = sc.nextInt();
				System.out.println("Enter the issued date (dd/MM/yyyy) : ");
				issueDate = sc.next();
				System.out.println("Enter the expected returned date (dd/MM/yyyy): ");
				returnDate = sc.next();
				
				// --> 12/03/2022 
				//Validating input and date formats
				if(length(isbn) == 6 && userName.length() >= 3 && length(userID)==4 && isValidFormat(issueDate) && isValidFormat(returnDate)) {
					dao.issueBook(new IssuedBooks(isbn, bookName, userName, userID, issueDate, returnDate));
					dao.addUser(new User(userID, userName));
					dao.removeBook(isbn);
				}else if(!isValidFormat(issueDate) || !isValidFormat(returnDate)){
					System.out.println("Invalid date format");
				}else {
					System.out.println("Enter the correct details of user/book.");
				}
				break;
				
				
			// Submission of book to the library 	
			case 3:
				System.out.println("Enter the user ID : ");
				userID = sc.nextInt();
				if(length(userID) == 4) {
					//Fetching the expected return date, isbnNo and book name from database
					List<String> l = dao.getDetails(userID);
					String date = l.get(0);
					String isbnInStringFormat = l.get(1);
					String book = l.get(2);
					
					int isbnNo = Integer.parseInt(isbnInStringFormat);
										
					//calculate the number of days between the current date and the expected return date
					long days = findDays(date);
					
					//Calculate the due
					long due = getDue(days);
					
					//If due is greater than 0, then we will tell user to pay the due as he had not submitted the book before the deadline.
					if(due > 0) {
						System.out.println("You have to pay "+ due + "/- as you have not returned the book before the deadline");
					}
					
					//Adding the book in the books table through DAO layer
					dao.addBook(new Book(isbnNo, book));
					
					//After returning the book, we will remove the details of the book from issue_books table
					dao.removeCustomerFromIssueDetails(userID);	
					
					
				}else {
					System.out.println("Enter the correct user ID!!");
				}
				break;
				
			case 4:
				List<IssuedBooks> ib = dao.getIssuedBooksInfo();
    			for(IssuedBooks i:ib) {
    				System.out.println(i);
    			}
    			break;
    			
			case 5:
				List<Book> bk = dao.getAllBooks();
				System.out.println("--------------------------------");
				System.out.println("        Available Books         ");
				System.out.println("--------------------------------");
				for(Book i:bk) {
					System.out.println(i);
				}
				break;
			
			case 6:
				List<String> isbks = dao.getBookAtCirculation();
				
				if(isbks.size() == 0) {
					System.out.println("No book is at circulation!!");
				}else {
					System.out.println("--------------------------------");
					System.out.println("      Books at Circulation      ");
					System.out.println("--------------------------------");
					for(String i : isbks) {
						System.out.println("      "+ i);
					}
				}
				
				break;
			
		}
	
	}

}
