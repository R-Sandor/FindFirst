package dev.findfirst.core.model;

import java.util.Arrays;

import jakarta.validation.constraints.NotEmpty;

public record SearchBkmkByTagReq(@NotEmpty String[]tags){@Override public boolean equals(Object obj){if(obj instanceof SearchBkmkByTagReq tagSearch){return Arrays.equals(this.tags(),tagSearch.tags());}return false;}

@Override public int hashCode(){return Arrays.hashCode(this.tags());}

@Override public String toString(){return Arrays.toString(this.tags());}

}
