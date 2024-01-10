import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.AESEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.gen.OctetSequenceKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EncryptAndSignJwtWithClaimsExample {

    public static void main(String[] args) throws JOSEException, java.text.ParseException {
        // Step 1: Generate a random AES key for encryption
        OctetSequenceKeyGenerator keyGenerator = new OctetSequenceKeyGenerator(256);
        OctetSequenceKey encryptionKey = keyGenerator.generate();

        // Step 2: Create claims as a HashMap
        Map<String, Object> claims = new HashMap<>();
        claims.put("subject", "subject");
        claims.put("data", "This is a confidential message.");
        claims.put("expirationTime", new Date(System.currentTimeMillis() + 3600000)); // 1 hour from now

        // Step 3: Convert HashMap claims to JWTClaimsSet
        JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet.Builder();
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            claimsSetBuilder.claim(entry.getKey(), entry.getValue());
        }
        JWTClaimsSet jwtClaimsSet = claimsSetBuilder.build();

        // Step 4: Encrypt the entire JWT
        JWEHeader header = new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A256GCM)
                .contentType("JWT")  // Required to indicate nested JWT
                .build();

        JWEObject jweObject = new JWEObject(header, new Payload(jwtClaimsSet.toJSONObject()));
        jweObject.encrypt(new AESEncrypter(encryptionKey));

        // Step 5: Sign the encrypted JWT
        SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.HS256).build(), jweObject.serialize());
        signedJWT.sign(new MACSigner(encryptionKey));

        // Print the final JWT
        String finalJwt = signedJWT.serialize();
        System.out.println("Final JWT: " + finalJwt);

        // Verification and Decryption
        SignedJWT verifiedJWT = SignedJWT.parse(finalJwt);
        if (verifiedJWT.verify(new MACVerifier(encryptionKey))) {
            System.out.println("Signature verification successful.");

            // Decryption
            JWEObject decryptedObject = JWEObject.parse(verifiedJWT.getPayload().toString());
            decryptedObject.decrypt(new AESEncrypter(encryptionKey));

            // Print the decrypted payload as a map
            String decryptedPayloadString = decryptedObject.getPayload().toString();
            Map<String, Object> decryptedPayloadMap = parseJsonToMap(decryptedPayloadString);
            System.out.println("Decrypted Payload as Map: " + decryptedPayloadMap);
        } else {
            System.out.println("Signature verification failed.");
        }
    }

    // Helper method to parse JSON string to Map
    private static Map<String, Object> parseJsonToMap(String jsonString) throws org.json.simple.parser.ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
        return new HashMap<>(jsonObject);
    }
}
