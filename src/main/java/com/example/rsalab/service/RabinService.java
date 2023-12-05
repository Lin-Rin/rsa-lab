package com.example.rsalab.service;

import com.example.rsalab.dto.rabin.decrypt.DecryptRequest;
import com.example.rsalab.dto.rabin.decrypt.DecryptResponse;
import com.example.rsalab.dto.rabin.encrypt.EncryptRequest;
import com.example.rsalab.dto.rabin.encrypt.EncryptResponse;
import com.example.rsalab.dto.rabin.generate.ServerKeyResponse;
import com.example.rsalab.dto.rabin.sign.SignRequest;
import com.example.rsalab.dto.rabin.sign.SignResponse;
import com.example.rsalab.dto.rabin.verify.VerifyRequest;
import com.example.rsalab.dto.rabin.verify.VerifyResponse;
import com.example.rsalab.model.RabinPublicKey;
import com.example.rsalab.model.RabinPrivateKey;
import com.example.rsalab.util.math.rabin.MathUtil;
import com.example.rsalab.util.math.rabin.NumberGenerator;
import java.math.BigInteger;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RabinService {
    private RabinPrivateKey privateKey;
    private RabinPublicKey publicKey;
    private final MathUtil mathUtil;
    private final NumberGenerator generator;

    public ServerKeyResponse serverKey(Long keySize) {
        ServerKeyResponse response = new ServerKeyResponse();
        privateKey = new RabinPrivateKey();
        publicKey = new RabinPublicKey();

        var p = generator.getPrimeNumber(keySize);
        var q = generator.getPrimeNumber(keySize);
        privateKey.setQ(q);
        privateKey.setP(p);

        var n = p.multiply(q);
        BigInteger b = generator.generateNumber(BigInteger.valueOf(2).pow(Math.toIntExact(keySize)));
        publicKey.setN(n);
        publicKey.setB(b);

        response.setModulus(n.toString(16));
        response.setB(b.toString(16));

        return response;
    }

    // (y, c1, c2)
    public EncryptResponse encrypt(EncryptRequest request) {
        EncryptResponse response = new EncryptResponse();

        BigInteger m = new BigInteger(request.getText(), 16);
        BigInteger n = new BigInteger(request.getModulus(), 16);
        BigInteger b = new BigInteger(request.getB(), 16);

        var x = formatMessage(m, n.bitLength());

        if (Math.ceil((double) m.bitLength() / 8) > Math.ceil((double) n.bitLength() / 8) - 10) {
            throw new RuntimeException("Message is too big");
        }

        var temp = x.add(b.multiply(BigInteger.valueOf(2).modInverse(n)));

        var y = x.multiply((x.add(b))).mod(n);
        var c1 = temp.mod(n).mod(BigInteger.TWO);
        var c2 = mathUtil.getJacobiSymbol(temp, n);

        if (!c2.equals(BigInteger.ONE)) {
            c2 = BigInteger.ZERO;
        }

        response.setCiphertext(y.toString(16));
        response.setParity(c1.toString(16));
        response.setJacobiSymbol(c2.toString(16));

        return response;
    }

    // (y, c1, c2)
    public DecryptResponse decrypt(DecryptRequest request) {
        DecryptResponse response = new DecryptResponse();

        BigInteger y = new BigInteger(request.getCiphertext(), 16);
        BigInteger c1 = new BigInteger(request.getParity(), 16);
        BigInteger c2 = new BigInteger(request.getJacobiSymbol(), 16);
        var b = publicKey.getB();
        var n = publicKey.getN();
        var p = privateKey.getP();
        var q = privateKey.getQ();

        BigInteger fourInverse = BigInteger.valueOf(4).modInverse(n);
        BigInteger twoInverse = BigInteger.TWO.modInverse(n);

        List<BigInteger> sqrt = mathUtil.sqrt(y.add(b.multiply(b).multiply(fourInverse)), p, q);

        BigInteger result = BigInteger.ZERO;
        for (var res : sqrt) {
            var yi = res.subtract(b.multiply(twoInverse)).mod(n);

            var temp = yi.add(b.multiply(twoInverse)).modInverse(n);

            var cc1 = temp.mod(n).mod(BigInteger.TWO);
            var cc2 = mathUtil.getJacobiSymbol(temp, n);
            if (!cc2.equals(BigInteger.ONE)) {
                cc2 = BigInteger.ZERO;
            }

            if (c1.equals(cc1) && c2.equals(cc2)) {
                result = yi;

                // need refactor
                break;
            }

        }

        response.setMessage(result.toString(16));

        return response;
    }

    public SignResponse sign(SignRequest request) {
        SignResponse response = new SignResponse();



        return response;
    }

    public VerifyResponse verify(VerifyRequest request) {
        VerifyResponse response = new VerifyResponse();

        final var message = new BigInteger(request.getMessage(), 16);
        final var sign = new BigInteger(request.getSignature(), 16);
        final var n = new BigInteger(request.getModulus(), 16);

        var x = sign.modPow(BigInteger.TWO, n);
        var formatted = formatMessage(message, n.bitLength());

        Boolean result = Boolean.FALSE;
        if (x.shiftRight(64).equals(formatted.shiftRight(64))) {
            result = Boolean.TRUE;
        }

        response.setVerified(result);

        return response;
    }

    private BigInteger formatMessage(BigInteger message, int n) {
        int l = (int) Math.ceil((double) n / 8);

        BigInteger r = generator.get64BitNumber();

        var x = BigInteger.valueOf(255).multiply(BigInteger.valueOf(2).pow(8 * (l - 2)));
        x = x.add(message.multiply(BigInteger.valueOf(2).pow(64)));
        x = x.add(r);

        return x;
    }
}
