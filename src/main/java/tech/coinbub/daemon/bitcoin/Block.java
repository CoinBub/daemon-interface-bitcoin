package tech.coinbub.daemon.bitcoin;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Block {
    public String hash;
    public Long confirmations;
    public Long strippedsize;
    public Long size;
    public Long weight;
    public Long height;
    public Long version;
    public String versionHex;
    public String merkleroot;
    public List<Transaction> tx;
    public Long time;
    public Long mediantime;
    public Long nonce;
    public String bits;
    public BigDecimal difficulty;
    public String chainwork;
    public String previousblockhash;
}
