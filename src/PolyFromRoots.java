import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PolyFromRoots {
    public static void main(String[] args) throws Exception {
        String jsonFilePath = args[0];
        String jsonString = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonString);

        int k = root.path("keys").path("k").asInt();

        BigInteger[] roots = new BigInteger[k];
        int index = 0;
        for (int i = 1; i <= k; ++i) {
            JsonNode rootNode = root.path(String.valueOf(i));
            int base = rootNode.path("base").asInt();
            String value = rootNode.path("value").asText();
            roots[index++] = baseToDecimal(value, base);
        }

        BigInteger[] coeffs = getCoefficients(roots, k);
        // Output: highest degree down to constant term
        System.out.println("Polynomial Coefficients (from degree " + k + " to 0):");
        for (int i = 0; i < coeffs.length; i++) {
            System.out.println("a[" + (k - i) + "] = " + coeffs[i]);
        }
        System.out.println("a: " + coeffs[0]);
        System.out.println("b: " + coeffs[1]);
        System.out.println("c: " + coeffs[2]);
    }

    public static BigInteger baseToDecimal(String value, int base) {
        return new BigInteger(value, base);
    }

    public static BigInteger[] getCoefficients(BigInteger[] roots, int k) {
        BigInteger[] coeffs = new BigInteger[k + 1];
        coeffs[0] = BigInteger.ONE;
        for (int i = 1; i <= k; i++) {
            coeffs[i] = BigInteger.ZERO;
        }
        for (int i = 0; i < k; ++i) {
            for (int j = i + 1; j >= 1; --j) {
                coeffs[j] = coeffs[j].subtract(roots[i].multiply(coeffs[j - 1]));
            }
        }
        return coeffs;
    }
}
