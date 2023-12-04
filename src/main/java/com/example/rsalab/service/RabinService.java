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

        List<BigInteger> sqrt = mathUtil.sqrt(y.add(b.pow(2).multiply(BigInteger.valueOf(4).modInverse(n))), p, q);

        BigInteger result = null;
        var x1 = getX(sqrt.get(0), b, n);
        var x2 = getX(sqrt.get(1), b, n);
        var x3 = getX(sqrt.get(2), b, n);
        var x4 = getX(sqrt.get(3), b, n);

        var val = b.multiply(BigInteger.valueOf(2).modInverse(n));
        var temp1 = x1.add(val);
        var temp2 = x2.add(val);
        var temp3 = x3.add(val);
        var temp4 = x4.add(val);

        var x1c1 = temp1.mod(n).mod(BigInteger.TWO).mod(n).mod(BigInteger.TWO);
        var x2c1 = temp2.mod(n).mod(BigInteger.TWO).mod(n).mod(BigInteger.TWO);
        var x3c1 = temp3.mod(n).mod(BigInteger.TWO).mod(n).mod(BigInteger.TWO);
        var x4c1 = temp4.mod(n).mod(BigInteger.TWO).mod(n).mod(BigInteger.TWO);

        var x1c2 = mathUtil.getJacobiSymbol(temp1, n);
        var x2c2 = mathUtil.getJacobiSymbol(temp2, n);
        var x3c2 = mathUtil.getJacobiSymbol(temp3, n);
        var x4c2 = mathUtil.getJacobiSymbol(temp4, n);

        if (c1.equals(x1c1) && c2.equals(x1c2)) {
            result = x1;
        }
        if (c1.equals(x2c1) && c2.equals(x2c2)) {
            result = x2;
        }
        if (c1.equals(x3c1) && c2.equals(x3c2)) {
            result = x3;
        }
        if (c1.equals(x4c1) && c2.equals(x4c2)) {
            result = x4;
        }

        if (result == null) {
            throw new RuntimeException("Cannot be decrypted");
        }

        response.setMessage(result.toString(16));

        return response;
    }

    private BigInteger getX(BigInteger x, BigInteger b, BigInteger n) {
        BigInteger inv2 = BigInteger.valueOf(2).modInverse(n);
        return x.subtract(b.multiply(inv2)).mod(n);
    }

    public SignResponse sign(SignRequest request) {
        SignResponse response = new SignResponse();



        return response;
    }

    public VerifyResponse verify(VerifyRequest request) {
        VerifyResponse response = new VerifyResponse();



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
