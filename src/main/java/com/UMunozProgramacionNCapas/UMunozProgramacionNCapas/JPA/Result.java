package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

public class Result {

    public boolean correct;
    public String errorMessage;
    public Exception ex;
    public Object Object;
    public List<Object> Objects;
    
    @JsonIgnore
    public int status;
    
}
