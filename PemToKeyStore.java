import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Base64;

public class PemToKeyStore {

    public static KeyStore convertPemToKeyStore(String pemString, char[] keystorePassword) throws Exception {
        // Extract certificate from PEM string
        Certificate[] certificates = extractCertificatesFromPem(pemString);

        // Create and load KeyStore with certificates
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(null, keystorePassword);

        // No private key, so we use a placeholder for alias
        keyStore.setCertificateEntry("certificate-alias", certificates[0]);

        return keyStore;
    }

    private static Certificate[] extractCertificatesFromPem(String pemString) throws Exception {
        String beginMarker = "-----BEGIN CERTIFICATE-----";
        String endMarker = "-----END CERTIFICATE-----";

        int beginIndex = pemString.indexOf(beginMarker);
        int endIndex = pemString.indexOf(endMarker);

        if (beginIndex == -1 || endIndex == -1) {
            throw new IllegalArgumentException("Certificate block not found in PEM string.");
        }

        String certPem = pemString.substring(beginIndex + beginMarker.length(), endIndex).replaceAll("\\s+", "");
        byte[] certBytes = Base64.getDecoder().decode(certPem);

        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(certBytes);
        Certificate cert = certFactory.generateCertificate(inputStream);

        return new Certificate[] { cert };
    }
}
