package com.tresende.catalog.admin.infrastructure;

import com.tresende.catalog.admin.application.UseCase;
import com.tresende.catalog.admin.domain.Cagerory;

public class Main {
    public static void main(String[] args) {
        Cagerory execute = new UseCase().execute();
        System.out.println(execute);
    }
}