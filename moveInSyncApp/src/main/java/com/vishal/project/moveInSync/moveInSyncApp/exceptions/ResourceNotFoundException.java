package com.vishal.project.moveInSync.moveInSyncApp.exceptions;



public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException() {
    }
}
