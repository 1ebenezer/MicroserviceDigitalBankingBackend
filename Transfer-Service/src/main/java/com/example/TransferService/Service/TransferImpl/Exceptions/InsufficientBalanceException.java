package com.example.TransferService.Service.TransferImpl.Exceptions;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
            super(message);
        }
}

