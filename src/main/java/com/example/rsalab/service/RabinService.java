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
import com.example.rsalab.model.RabinPrivateKey;
import com.example.rsalab.util.math.rabin.MathUtil;
import com.example.rsalab.util.math.rabin.NumberGenerator;
import java.math.BigInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RabinService {
    private RabinPrivateKey privateKey;
    private final MathUtil mathUtil;
    private final NumberGenerator generator;

    public ServerKeyResponse serverKey(Long keySize) {
        ServerKeyResponse response = new ServerKeyResponse();
        privateKey = new RabinPrivateKey();

        var p = generator.getPrimeNumber(keySize);
        var q = generator.getPrimeNumber(keySize);
        privateKey.setQ(q);
        privateKey.setP(p);

        var n = p.multiply(q);
        BigInteger b = generator.generateNumber(BigInteger.valueOf(2).pow(Math.toIntExact(keySize)));

        response.setModulus(n.toString(16));
        response.setModulus(b.toString(16));

        return response;
    }

    // (y, c1, c2)
    public EncryptResponse encrypt(EncryptRequest request) {
        EncryptResponse response = new EncryptResponse();

        BigInteger m = new BigInteger(request.getText(), 16);
        BigInteger n = new BigInteger(request.getModulus(), 16);
        BigInteger b = new BigInteger(request.getB(), 16);

        var x = formatMessage(m, request.getModulus().length());
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

    private BigInteger formatMessage(BigInteger message, int n) {
        int l = (int) Math.ceil((double) n / 8);

        BigInteger r = generator.get64BitNumber();

        var x = BigInteger.valueOf(255).multiply(BigInteger.valueOf(2).pow(8 * (l - 2)));
        x = x.add(message.multiply(BigInteger.valueOf(2).pow(64)));
        x = x.add(r);

        return x;
    }

    // (y, c1, c2)
    public DecryptResponse decrypt(DecryptRequest request) {
        DecryptResponse response = new DecryptResponse();



        return response;
    }

    public SignResponse sign(SignRequest request) {
        throw new UnsupportedOperationException();
    }

    public VerifyResponse verify(VerifyRequest request) {
        throw new UnsupportedOperationException();
    }

}
