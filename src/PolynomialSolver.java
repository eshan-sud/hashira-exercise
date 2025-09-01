public static long[] getCoefficients(long[] roots, int k) {
    long[] coeffs = new long[k+1];
    coeffs[0] = 1;
    for (int i = 0; i < k; ++i) {
        for (int j = i+1; j >= 1; --j) {
            coeffs[j] -= roots[i] * coeffs[j - 1];
        }
    }
    return coeffs;
}
