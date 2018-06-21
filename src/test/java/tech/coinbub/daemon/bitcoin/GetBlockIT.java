package tech.coinbub.daemon.bitcoin;

import java.math.BigDecimal;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tech.coinbub.daemon.Bitcoin;
import static tech.coinbub.daemon.testutils.BeanMatcher.hasOnly;
import static tech.coinbub.daemon.testutils.BeanPropertyMatcher.property;
import tech.coinbub.daemon.testutils.Dockerized;

@ExtendWith(Dockerized.class)
public class GetBlockIT {
    @Test
    public void canGetBasicData(final Bitcoin bitcoin) {
        final Block block = bitcoin.getblock("3cf12c2be37b180d6c955647cc8b468f814f1c5941dda2283bd9d903882fb6aa");
        assertThat(block, hasOnly(
                property("hash", is(equalTo("3cf12c2be37b180d6c955647cc8b468f814f1c5941dda2283bd9d903882fb6aa"))),
                property("confirmations", is(equalTo(1L))),
                property("strippedsize", is(equalTo(217L))),
                property("size", is(equalTo(253L))),
                property("weight", is(equalTo(904L))),
                property("height", is(equalTo(102L))),
                property("version", is(equalTo(536870912L))),
                property("versionHex", is(equalTo("20000000"))),
                property("merkleroot", is(equalTo("2553da6a3f4130e040c2dddfd9d14b1e394309c393d7f6a7cee8c5005fbb3f87"))),
                property("nTx", is(equalTo(1L))),
                property("tx", hasSize(1)),
                property("time", is(equalTo(1525217577L))),
                property("mediantime", is(equalTo(1525217576L))),
                property("nonce", is(equalTo(1L))),
                property("bits", is(equalTo("207fffff"))),
                property("difficulty", is(equalTo(new BigDecimal("4.656542373906925E-10")))),
                property("chainwork", is(equalTo("00000000000000000000000000000000000000000000000000000000000000ce"))),
                property("previousblockhash", is(equalTo("3382d4f9f9f2c4268c7ad8db2330584dbcfc6c6f31e03894b3766e43ed67e020")))
        ));

        final Transaction tx = block.tx.get(0);
        assertThat(tx, hasOnly(
                property("txid", is(equalTo("2553da6a3f4130e040c2dddfd9d14b1e394309c393d7f6a7cee8c5005fbb3f87")))
        ));
    }

    @Test
    public void canGetVerboseData(final Bitcoin bitcoin) {
        final Block block = bitcoin.getblock("3cf12c2be37b180d6c955647cc8b468f814f1c5941dda2283bd9d903882fb6aa", 2);
        assertThat(block, hasOnly(
                property("hash", is(equalTo("3cf12c2be37b180d6c955647cc8b468f814f1c5941dda2283bd9d903882fb6aa"))),
                property("confirmations", is(equalTo(1L))),
                property("strippedsize", is(equalTo(217L))),
                property("size", is(equalTo(253L))),
                property("weight", is(equalTo(904L))),
                property("height", is(equalTo(102L))),
                property("version", is(equalTo(536870912L))),
                property("versionHex", is(equalTo("20000000"))),
                property("merkleroot", is(equalTo("2553da6a3f4130e040c2dddfd9d14b1e394309c393d7f6a7cee8c5005fbb3f87"))),
                property("nTx", is(equalTo(1L))),
                property("tx", hasSize(1)),
                property("time", is(equalTo(1525217577L))),
                property("mediantime", is(equalTo(1525217576L))),
                property("nonce", is(equalTo(1L))),
                property("bits", is(equalTo("207fffff"))),
                property("difficulty", is(equalTo(new BigDecimal("4.656542373906925E-10")))),
                property("chainwork", is(equalTo("00000000000000000000000000000000000000000000000000000000000000ce"))),
                property("previousblockhash", is(equalTo("3382d4f9f9f2c4268c7ad8db2330584dbcfc6c6f31e03894b3766e43ed67e020")))
        ));

        final Transaction tx = block.tx.get(0);
        assertThat(tx, hasOnly(
                property("txid", is(equalTo("2553da6a3f4130e040c2dddfd9d14b1e394309c393d7f6a7cee8c5005fbb3f87"))),
                property("hash", is(equalTo("89c62b6bcfc5697de7c00862e16bb69fafcbadf09f4fb6de594ac8c99094a990"))),
                property("version", is(equalTo(2L))),
                property("size", is(equalTo(172L))),
                property("vsize", is(equalTo(145L))),
                property("weight", is(equalTo(580L))),
                property("locktime", is(equalTo(0L))),
                property("vin", hasSize(1)),
                property("vout", hasSize(2)),
                property("hex", is(equalTo("020000000001010000000000000000000000000000000000000000000000000000000000000000ffffffff0401660101ffffffff0200f2052a010000001976a914b16dbe6d39f9d57758b64aeab4b462e84a39a87e88ac0000000000000000266a24aa21a9ede2f61c3f71d1defd3fa999dfa36953755c690689799962b48bebd836974e8cf90120000000000000000000000000000000000000000000000000000000000000000000000000")))
        ));

        final TxInput in = tx.vin.get(0);
        assertThat(in, hasOnly(
                property("coinbase", is(equalTo("01660101"))),
                property("sequence", is(equalTo(4294967295L)))
        ));

        final TxOutput first = tx.vout.get(0);
        assertThat(first, hasOnly(
                property("value", is(equalTo(new BigDecimal("50.0")))),
                property("n", is(equalTo(0L))),
                property("scriptPubKey", is(not(nullValue())))
        ));

        final ScriptPublicKey firstKey = first.scriptPubKey;
        assertThat(firstKey, hasOnly(
                property("asm", is(equalTo("OP_DUP OP_HASH160 b16dbe6d39f9d57758b64aeab4b462e84a39a87e OP_EQUALVERIFY OP_CHECKSIG"))),
                property("hex", is(equalTo("76a914b16dbe6d39f9d57758b64aeab4b462e84a39a87e88ac"))),
                property("reqSigs", is(equalTo(1L))),
                property("type", is(equalTo(ScriptPublicKey.Type.pubkeyhash))),
                property("addresses", hasSize(1)),
                property("addresses", contains("mwh7N9LvZEGsHVWsyfkSxrPepMMSFs4N24"))
        ));

        final TxOutput second = tx.vout.get(1);
        assertThat(second, hasOnly(
                property("value", is(equalTo(new BigDecimal("0.0")))),
                property("n", is(equalTo(1L))),
                property("scriptPubKey", is(not(nullValue())))
        ));

        final ScriptPublicKey secondKey = second.scriptPubKey;
        assertThat(secondKey, hasOnly(
                property("asm", is(equalTo("OP_RETURN aa21a9ede2f61c3f71d1defd3fa999dfa36953755c690689799962b48bebd836974e8cf9"))),
                property("hex", is(equalTo("6a24aa21a9ede2f61c3f71d1defd3fa999dfa36953755c690689799962b48bebd836974e8cf9"))),
                property("type", is(equalTo(ScriptPublicKey.Type.nulldata)))
        ));
    }
}

