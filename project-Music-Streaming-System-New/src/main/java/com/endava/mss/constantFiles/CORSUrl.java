package com.endava.mss.constantFiles;

public enum CORSUrl {
PORT("http://localhost:5173");
private final String value;

CORSUrl(String value) {
	
    this.value = value;
}

public String getMessage() {
    return value;
}
}
