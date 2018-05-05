package tech.coinbub.daemon.bitcoin;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDetail {
    public String address;
    public Category category;
    public BigDecimal amount;
    public BigDecimal fee;
    public Long vout;
    public Boolean abandoned;
    
    public enum Category {
        immature,
        send
    }
}
