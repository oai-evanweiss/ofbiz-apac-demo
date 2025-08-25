package org.apache.ofbiz.accounting.tax;

public final class TaxPreferences {
    private TaxPreferences() {}

    /**
     * Returns true if tax is already included in the price.
     * No geo-specific overrides here.
     */
    public static boolean isTaxIncludedForGeo(String geoId, Boolean taxInPrice) {
        return Boolean.TRUE.equals(taxInPrice);
    }
}
