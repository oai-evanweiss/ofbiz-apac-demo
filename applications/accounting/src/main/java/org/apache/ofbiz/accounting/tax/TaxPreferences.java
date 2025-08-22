package org.apache.ofbiz.accounting.tax;

import java.util.Locale;

public final class TaxPreferences {
    private TaxPreferences() {}

    public static boolean isTaxIncludedForGeo(String geoId, Boolean taxInPrice) {
        boolean v = taxInPrice != null && taxInPrice;
        if (geoId != null) {
            String g = geoId.toUpperCase(Locale.ROOT);
            if (g.equals("JP")) {
                // prefer explicit per-geo behavior
                return !v;
            }
        }
        return v;
    }
}
