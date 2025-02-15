package br.com.siswbrasil.integrator.entity;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
            updatedBy = username;
        } else if (authentication != null && authentication.isAuthenticated() 
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            updatedBy = authentication.getName();
        } else {
            updatedBy = "system";
        }
    
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
    
        try {
            java.lang.reflect.Method getClaimsMethod = principal.getClass().getMethod("getClaims");
            Object claims = getClaimsMethod.invoke(principal);
            if (claims instanceof java.util.Map) {
                java.util.Map<?, ?> claimsMap = (java.util.Map<?, ?>) claims;
                Object nameClaim = claimsMap.get("name");
                if (nameClaim != null) {
                    username = nameClaim.toString();
                } else {
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
            createdBy = username;
        } else if (authentication != null && authentication.isAuthenticated() 
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            createdBy = authentication.getName();
        } else {
            createdBy = "system";
        }
    }

}

