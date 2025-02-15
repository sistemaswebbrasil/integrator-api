package br.com.siswbrasil.integrator.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return "system";
        }

        Object principal = authentication.getPrincipal();
        String username = null;

        try {
            java.lang.reflect.Method getClaimsMethod = principal.getClass().getMethod("getClaims");
            Object claims = getClaimsMethod.invoke(principal);
            if (claims instanceof java.util.Map) {
                java.util.Map<?, ?> claimsMap = (java.util.Map<?, ?>) claims;
                // First try to get the name field
                Object nameClaim = claimsMap.get("name");
                if (nameClaim != null) {
                    username = nameClaim.toString();
                } else {
                    // If name is not present, try to get the sub field
                    Object subClaim = claimsMap.get("sub");
                    if (subClaim != null) {
                        username = subClaim.toString();
                    }
                }
            }
        } catch (Exception e) {
            // Ignore exception and leave username as null
        }

        if (username != null) {
            return username;
        } else if (authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }

        return "system";
    }
}