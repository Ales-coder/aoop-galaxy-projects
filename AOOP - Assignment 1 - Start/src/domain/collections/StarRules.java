package domain.collections;

import domain.Star;

import java.util.Set;

final class StarRules {

    private StarRules() {}

    static final class ValidationResult {
        final boolean ok;
        final Star partner;

        ValidationResult(boolean ok, Star partner) {
            this.ok = ok;
            this.partner = partner;
        }
    }


    static ValidationResult validateCandidate(Star candidate, Set<Star> existing) {

        Star partner = null;
        int partnerCount = 0;


        for (Star s : existing) {
            if (s == null) continue;

            double d = candidate.calculateDistance(s);


            if (d < 0.01) {
                return new ValidationResult(false, null);
            }


            if (isDoubleStar(s) && d < 0.3) {
                return new ValidationResult(false, null);
            }


            if (d <= 0.1 && !isDoubleStar(s)) {
                partner = s;
                partnerCount++;
                if (partnerCount > 1) {
                    // must be exactly ONE
                    return new ValidationResult(false, null);
                }
            }
        }


        if (partnerCount == 1) {


            for (Star s : existing) {
                if (s == null) continue;
                if (s == partner) continue;

                double d = candidate.calculateDistance(s);


                if (d >= 0.03 && d <= 0.3) {
                    return new ValidationResult(false, null);
                }
            }

            return new ValidationResult(true, partner);
        }


        for (Star s : existing) {
            if (s == null) continue;

            double d = candidate.calculateDistance(s);

            if (d >= 0.03 && d <= 0.3) {
                return new ValidationResult(false, null);
            }
        }

        return new ValidationResult(true, null);
    }

    static boolean isDoubleStar(Star s) {
        return s.getSister() != null;
    }
}