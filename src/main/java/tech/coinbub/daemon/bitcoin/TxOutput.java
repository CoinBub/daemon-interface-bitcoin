package tech.coinbub.daemon.bitcoin;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TxOutput {
    public BigDecimal value;
    public Long n;
    public ScriptPublicKey scriptPubKey;
}
