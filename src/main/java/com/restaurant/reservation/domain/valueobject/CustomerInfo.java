package com.restaurant.reservation.domain.valueobject;

import lombok.Value;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object que representa as informações do cliente.
 * Contém validações para email e telefone.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Value
public class CustomerInfo {
    
    String name;
    String email;
    String phone;
    String specialRequests;
    
    private CustomerInfo(String name, String email, String phone, String specialRequests) {
        validateCustomerInfo(name, email, phone);
        this.name = name.trim();
        this.email = email.trim().toLowerCase();
        this.phone = phone.trim();
        this.specialRequests = specialRequests != null ? specialRequests.trim() : "";
    }
    
    /**
     * Cria CustomerInfo com todos os campos obrigatórios.
     */
    public static CustomerInfo of(String name, String email, String phone) {
        return new CustomerInfo(name, email, phone, "");
    }
    
    /**
     * Cria CustomerInfo com pedidos especiais.
     */
    public static CustomerInfo of(String name, String email, String phone, String specialRequests) {
        return new CustomerInfo(name, email, phone, specialRequests);
    }
    
    /**
     * Valida as informações do cliente.
     */
    private void validateCustomerInfo(String name, String email, String phone) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty");
        }
        if (name.trim().length() < 2) {
            throw new IllegalArgumentException("Customer name must be at least 2 characters");
        }
        
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer email cannot be null or empty");
        }
        
        String emailPattern = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
        if (!Pattern.matches(emailPattern, email.trim())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer phone cannot be null or empty");
        }
        
        String phonePattern = "^\\(?([0-9]{2})\\)?[-. ]?([0-9]{4,5})[-. ]?([0-9]{4})$";
        if (!Pattern.matches(phonePattern, phone.trim())) {
            throw new IllegalArgumentException("Invalid phone format");
        }
    }
    
    /**
     * Verifica se há pedidos especiais.
     */
    public boolean hasSpecialRequests() {
        return specialRequests != null && !specialRequests.trim().isEmpty();
    }
    
    /**
     * Retorna o nome formatado (primeira letra maiúscula).
     */
    public String getFormattedName() {
        if (name == null || name.isEmpty()) return name;
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
    
    @Override
    public String toString() {
        return String.format("CustomerInfo{name='%s', email='%s', phone='%s'}", 
            name, email, phone);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerInfo that = (CustomerInfo) o;
        return Objects.equals(name, that.name) && 
               Objects.equals(email, that.email) && 
               Objects.equals(phone, that.phone);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, email, phone);
    }
}
