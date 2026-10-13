package ie.universityofgalway.group11.expensestracker.budgeting.domain.expense;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense {


    private String userId;          //who owns this expense
    private BigDecimal amount;      //Money spent
    private LocalDate date;         //Date of the expanse
    private String category;        //For example : food or transport


    public Expense(String userId, BigDecimal amount, LocalDate date, String category){
        this.userId = userId;
        this.amount= amount;
        this.date= date;
        this.category= category;

    }

    public String getUserId(){
        return userId;
    }

    public BigDecimal getAmount(){
        return amount;

    }
    public LocalDate getDate(){
        return date;
    }

    public String getCategory(){
        return category;

    }

}
