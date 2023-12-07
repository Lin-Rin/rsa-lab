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
import java.util.Random;
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
        final var response = new ServerKeyResponse();
        privateKey = new RabinPrivateKey();
        publicKey = new RabinPublicKey();

        final var p = generator.getPrimeNumber(keySize);
        final var q = generator.getPrimeNumber(keySize);
        privateKey.setQ(q);
        privateKey.setP(p);

        final var n = p.multiply(q);
        final var b = generator.generateNumber(BigInteger.valueOf(2).pow(Math.toIntExact(keySize)));
        publicKey.setN(n);
        publicKey.setB(b);

        response.setModulus(n.toString(16));
        response.setB(b.toString(16));

        return response;
    }

    public EncryptResponse encrypt(EncryptRequest request) {
        final var response = new EncryptResponse();

        final var m = new BigInteger(request.getText(), 16);
        final var n = new BigInteger(request.getModulus(), 16);
        final var b = new BigInteger(request.getB(), 16);

        final var x = formatMessage(m, n.bitLength());

        if (Math.ceil((double) m.bitLength() / 8) > Math.ceil((double) n.bitLength() / 8) - 10) {
            throw new RuntimeException("Message is too big");
        }

        final var y = x.multiply((x.add(b))).mod(n);
        final var c1 = getC1(x, b, n); // x.mod(BigInteger.TWO) ??
        final var c2 = getC2Indicator(x, b, n); // getC2(x, b, n) ??

        response.setCiphertext(y.toString(16));
        response.setParity(c1.toString(16));
        response.setJacobiSymbol(c2.toString(16));

        return response;
    }

    public DecryptResponse decrypt(DecryptRequest request) {
        final var response = new DecryptResponse();

        final var y = new BigInteger(request.getCiphertext(), 16);
        final var c1 = new BigInteger(request.getParity(), 16);
        final var c2 = new BigInteger(request.getJacobiSymbol(), 16);
        final var b = publicKey.getB();
        final var n = publicKey.getN();
        final var p = privateKey.getP();
        final var q = privateKey.getQ();

        final var fourInverse = BigInteger.valueOf(4).modInverse(n);
        final var sqrtList = mathUtil.sqrt(y.add(b.multiply(b).multiply(fourInverse)), p, q);
        var result = BigInteger.ZERO;

        final var x1 = getX(sqrtList.get(0), b, n);
        final var x2 = getX(sqrtList.get(1), b, n);
        final var x3 = getX(sqrtList.get(2), b, n);
        final var x4 = getX(sqrtList.get(3), b, n);

        final var x1c1 = getC1(x1, b, n);
        final var x2c1 = getC1(x2, b, n);
        final var x3c1 = getC1(x3, b, n);
        final var x4c1 = getC1(x4, b, n);

        final var x1c2 = getC2(x1, b, n);
        final var x2c2 = getC2(x2, b, n);
        final var x3c2 = getC2(x3, b, n);
        final var x4c2 = getC2(x4, b, n);

        // to remove
        System.out.println("x1 -- c1: " + x1c1 + ", c2: " + x1c2);
        System.out.println("x2 -- c1: " + x2c1 + ", c2: " + x2c2);
        System.out.println("x3 -- c1: " + x3c1 + ", c2: " + x3c2);
        System.out.println("x4 -- c1: " + x4c1 + ", c2: " + x4c2);

        if (x1c1.equals(c1) && x1c2.equals(c2)) {
            result = x1;
        } else if (x2c1.equals(c1) && x2c2.equals(c2)) {
            result = x2;
        } else if (x3c1.equals(c1) && x3c2.equals(c2)) {
            result = x3;
        } else if (x4c1.equals(c1) && x4c2.equals(c2)) {
            result = x4;
        } else {
            response.setMessage("decrypt error");
            return response;
        }

        final var l = (int) Math.ceil((double) n.bitLength() / 8);
        BigInteger bigInteger = result.subtract(BigInteger.valueOf(255).multiply(BigInteger.valueOf(2).pow(8 * (l - 2)))).shiftRight(64);

        System.out.println("Chosen x: " + result.toString(16));
        showIn(x1, l, " x1: ");
        showIn(x2, l, " x2: ");
        showIn(x3, l, " x3: ");
        showIn(x4, l, " x4: ");

        response.setMessage(bigInteger.toString(16));

        return response;
    }

    @Deprecated  // to remove
    private void showIn(BigInteger x, int l, String var) {
        BigInteger bigInteger = x.subtract(BigInteger.valueOf(255).multiply(BigInteger.valueOf(2).pow(8 * (l - 2)))).shiftRight(64);

        System.out.println(x.toString(16) + var + bigInteger.toString(16));
    }

    private BigInteger getX(final BigInteger x, final BigInteger b, final BigInteger n) {
        final var twoInverse = BigInteger.valueOf(2).modInverse(n);

        return x.subtract(b.multiply(twoInverse)).mod(n);
    }

    private BigInteger getC1(final BigInteger x, final BigInteger b, final BigInteger n) {
        return x.add(b.multiply(BigInteger.TWO.modPow(BigInteger.ONE.negate(), n)))
                .mod(n)
                .mod(BigInteger.TWO);
    }

    private BigInteger getC2(final BigInteger x, final BigInteger b, final BigInteger n) {
        final var temp = x.add(b.multiply(BigInteger.TWO.modInverse(n)));

        return mathUtil.getJacobiSymbol(temp, n);
    }

    private BigInteger getC2Indicator(final BigInteger x, final BigInteger b, final BigInteger n) {
        final var temp = x.add(b.multiply(BigInteger.TWO.modInverse(n)));

        return !mathUtil.getJacobiSymbol(temp, n).equals(BigInteger.ONE) ? BigInteger.ZERO : BigInteger.ONE;
    }

    public SignResponse sign(SignRequest request) {
        final var response = new SignResponse();

        final var message = new BigInteger(request.getMessage(), 16);
        final var p = privateKey.getP();
        final var q = privateKey.getQ();
        final var n = publicKey.getN();
        var result = BigInteger.ONE;

        while (true) {
            var formatted = formatMessage(message, n.bitLength());

            while (!mathUtil.getJacobiSymbol(formatted, p).equals(BigInteger.ONE) && !mathUtil.getJacobiSymbol(formatted, q).equals(BigInteger.ONE)) {
                formatted = formatMessage(message, n.bitLength());
            }

            final var sqrtRoot = mathUtil.sqrt(formatted, p, q).get(new Random().nextInt(4));
            if (sqrtRoot.modPow(BigInteger.TWO, n).equals(formatted.mod(n))) {
                result = sqrtRoot;
                break;
            }
        }

        response.setSignature(result.toString(16));

        return response;
    }

    public VerifyResponse verify(VerifyRequest request) {
        final var response = new VerifyResponse();

        final var message = new BigInteger(request.getMessage(), 16);
        final var sign = new BigInteger(request.getSignature(), 16);
        final var n = new BigInteger(request.getModulus(), 16);

        final var x = sign.modPow(BigInteger.TWO, n);
        final var formatted = formatMessage(message, n.bitLength());

        var result = Boolean.FALSE;
        if (x.shiftRight(64).equals(formatted.shiftRight(64))) {
            result = Boolean.TRUE;
        }

        response.setVerified(result);

        return response;
    }

    private BigInteger formatMessage(BigInteger message, int n) {
        final var l = (int) Math.ceil((double) n / 8);

        final var r = generator.get64BitNumber();

        return BigInteger.valueOf(255)
                .multiply(BigInteger.valueOf(2).pow(8 * (l - 2)))
                .add(message.multiply(BigInteger.valueOf(2).pow(64)))
                .add(r);
    }
}
