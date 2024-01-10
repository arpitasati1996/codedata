import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.AESEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.gen.OctetSequenceKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.util.Date;

public class EncryptAndSignJwtExample {

    public static void main(String[] args) throws JOSEException {
        // Step 1: Generate a random AES key for encryption
        OctetSequenceKeyGenerator keyGenerator = new OctetSequenceKeyGenerator(256);
        OctetSequenceKey encryptionKey = keyGenerator.generate();

        // Step 2: Create a JWT with the payload
        // Current time
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // Set expiration time to 1 hour from now
        long expMillis = nowMillis + 3600000; // 1 hour
        Date exp = new Date(expMillis);

        // Your payload to be signed and encrypted
        String payload = "This is a confidential message.";

        // Build the JWT
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("subject")
                .claim("data", payload)
                .expirationTime(exp)
                .build();

        // Step 3: Encrypt the entire JWT
        JWEHeader header = new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A256GCM)
                .contentType("JWT")  // Required to indicate nested JWT
                .build();

        JWEObject jweObject = new JWEObject(header, new Payload(claimsSet.toJSONObject()));
        jweObject.encrypt(new AESEncrypter(encryptionKey));

        // Step 4: Sign the encrypted JWT
        SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.HS256).build(), jweObject.serialize());
        signedJWT.sign(new MACSigner(encryptionKey));

        // Print the final JWT
        String finalJwt = signedJWT.serialize();
        System.out.println("Final JWT: " + finalJwt);

        // Verification
        SignedJWT verifiedJWT = SignedJWT.parse(finalJwt);
        if (verifiedJWT.verify(new MACVerifier(encryptionKey))) {
            System.out.println("Signature verification successful.");
            // Decryption
            JWEObject decryptedObject = JWEObject.parse(verifiedJWT.getPayload().toString());
            decryptedObject.decrypt(new AESEncrypter(encryptionKey));
            // Print the decrypted payload
            System.out.println("Decrypted Payload: " + decryptedObject.getPayload().toString());
        } else {
            System.out.println("Signature verification failed.");
        }
    }
}
