package com.tresende.catalog.admin.application;

public abstract class UnitUseCase<IN> {
    public abstract void execute(IN anIn);
}