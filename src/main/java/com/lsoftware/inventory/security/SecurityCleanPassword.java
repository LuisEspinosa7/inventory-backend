package com.lsoftware.inventory.security;

public interface SecurityCleanPassword<T> {
	
	void cleanSensitiveData(T obj);

}
