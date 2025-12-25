package com.example.adoptapet.model;

import com.example.adoptapet.model.exceptions.AlreadyAdoptedException;

public interface Adoptable {
    void adoptar(String adopterName) throws AlreadyAdoptedException;
    boolean isAdopted();
}