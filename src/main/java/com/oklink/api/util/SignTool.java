package com.oklink.api.util;

import com.google.common.base.Strings;
import com.oklink.bitcoinj.core.OKTransaction;
import com.oklink.bitcoinj.params.OKMainNetParams;
import com.oklink.bitcoinj.params.OKTestNetParams;
import com.oklink.bitcoinj.script.OKScript;
import org.bitcoinj.core.*;
import org.bitcoinj.core.ECKey.ECDSASignature;
import org.bitcoinj.core.Transaction.SigHash;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Some utility methods for sign transaction.
 */
public class SignTool {

    public static final int COIN_BTC = 0;
    public static final int COIN_TOKEN = 1;

    /**
     * Decode privateKey WIF string.
     * 
     * @param encryptedECKey encrypted privateKey string, base64 encode.
     * @param paymentPassword payment password.
     * @param salt salt used in the system.
     * @return
     * @throws Exception
     */
    public static String decodePrivateKeyWIF(String encryptedECKey, String paymentPassword,
            String salt) throws Exception {
        String password = AESForCryptoJS.getEncodedPass(paymentPassword, salt);
        return AESForCryptoJS.decrypt(encryptedECKey, password);
    }

    /**
     * Verify the params and sign transaction.
     * <p>
     * Only support the tranHex from the API, and all inputs are from the same address.
     *
     * @param tranHex the transaction in hex string format.
     * @param coinType 0:btc;1:token.
     * @param privateKeyWIF privateKey for the input address in WIF format.
     * @param redeemScript if the input address is MultiSig, the redeemScript is needed.
     * @return
     * @throws Exception
     */
    public static String verifyAndSignTransaction(String tranHex, int coinType,
            String privateKeyWIF, String redeemScript) throws Exception {
        return verifyAndSignTransaction(tranHex, coinType, privateKeyWIF, redeemScript, false);
    }

    /**
     * Verify the params and sign transaction.
     * <p>
     * Only support the tranHex from the API, and all inputs are from the same address.
     * 
     * @param tranHex the transaction in hex string format.
     * @param coinType 0:btc;1:token.
     * @param privateKeyWIF privateKey for the input address in WIF format.
     * @param redeemScript if the input address is MultiSig, the redeemScript is needed.
     * @param isTestNet true:use test parameters; false:use mainnet parameters.
     * @return
     * @throws Exception
     */
    public static String verifyAndSignTransaction(String tranHex, int coinType,
            String privateKeyWIF, String redeemScript, boolean isTestNet) throws Exception {
        if (Strings.isNullOrEmpty(tranHex) || Strings.isNullOrEmpty(privateKeyWIF)) {
            throw new IllegalArgumentException("tranHex and privateKeyWIF can't be null! ");
        }

        NetworkParameters params = getNetworkParameters(coinType, isTestNet);
        if (params == null) {
            throw new IllegalArgumentException("coinType must be 0(btc) or 1(token)");
        }

        Transaction transaction = null;
        if (coinType == COIN_BTC) {
            transaction = new Transaction(params, Utils.HEX.decode(tranHex));
        } else {
            transaction = new OKTransaction(params, Utils.HEX.decode(tranHex));
        }

        List<TransactionInput> inputList = transaction.getInputs();
        List<TransactionOutput> outputList = transaction.getOutputs();
        if (inputList == null || inputList.size() == 0 || outputList == null
                || outputList.size() == 0) {
            throw new IllegalArgumentException("Illegal tranHex, input and output can't be empty!");
        }

        DumpedPrivateKey privateKey = DumpedPrivateKey.fromBase58(params, privateKeyWIF);
        ECKey ecKey = privateKey.getKey();
        byte[] addressHash160;
        if (!Strings.isNullOrEmpty(redeemScript)) {
            if (redeemScript.indexOf(ecKey.getPublicKeyAsHex()) == -1) {
                throw new IllegalArgumentException(
                        "The privateKeyWIF is not the component of redeemScript! ");
            }
            Script script = new Script(Utils.HEX.decode(redeemScript));
            Script p2shScript = ScriptBuilder.createP2SHOutputScript(script);
            addressHash160 = Address.fromP2SHScript(params, p2shScript).getHash160();
        } else {
            addressHash160 = ecKey.toAddress(params).getHash160();
        }

        for (TransactionInput input : inputList) {
            OKScript script = new OKScript(input.getScriptSig().getChunks());
            if (script == null) {
                throw new Exception("Only support tranHex from API! ");
            }
            // TODO to check if the eckey is for the input.
            if (!Arrays.equals(addressHash160, script.getPubKeyHash())) {
                throw new IllegalArgumentException(
                        "The privateKeyWIF is not fit the transaction input! ");
            }
        }

        for (int i = 0, len = inputList.size(); i < len; i++) {
            signInput(transaction, i, ecKey, redeemScript, params);
        }

        return Utils.HEX.encode(transaction.bitcoinSerialize());
    }

    /**
     * Sign the input point out by the index.
     *
     * @param transaction transaction from API.
     * @param index to sign which input.
     * @param ecKey private key for the input.
     * @param redeemScript redeem script for input address.
     * @param params the NetworkParameters
     * @return
     */
    private static void signInput(Transaction transaction, int index, ECKey ecKey,
            String redeemScript, NetworkParameters params) {
        TransactionInput input = transaction.getInput(index);
        Script script = null;
        if (!Strings.isNullOrEmpty(redeemScript)) {
            script = new Script(Utils.HEX.decode(redeemScript));
        } else {
            script = input.getScriptSig();
        }

        Sha256Hash hashForSign = transaction.hashForSignature(index, script, SigHash.ALL, false);
        ECDSASignature signature = ecKey.sign(hashForSign);
        TransactionSignature tranSign = new TransactionSignature(signature, SigHash.ALL, false);

        Script scriptSig = null;
        if (!Strings.isNullOrEmpty(redeemScript)) {
            List<TransactionSignature> signatureList = new ArrayList<>();
            signatureList.add(tranSign);

            Script multiSigScript = new Script(Utils.HEX.decode(redeemScript));
            scriptSig = ScriptBuilder.createP2SHMultiSigInputScript(signatureList, multiSigScript);
        } else {
            scriptSig = ScriptBuilder.createInputScript(tranSign, ecKey);
        }
        transaction.getInput(index).setScriptSig(scriptSig);
    }

    /**
     * get NetworkParameters by coinType.
     * 
     * @param coinType 0:btc;1:token.
     * @param isTestNet true:need test parameters; false:need mainnet parameters.
     * @return
     */
    private static NetworkParameters getNetworkParameters(int coinType, boolean isTestNet) {
        
        if (coinType == COIN_BTC) {
            return MainNetParams.get();
        } else if (coinType == COIN_TOKEN) {
            return OKMainNetParams.get();
        }
        return null;
    }

}
