package org.sciplore.preamble;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface License {
	String author () default "Gipplab (www.gipplab.org)";
	String license() default "Copyright (c) 2015, Chair for Scientific Information Analytics, University of Göttingen (https://gipplab.org/)"
	+"\nBela Gipp (http://gipp.com/contact), Norman Meuschke (www.meuschke.org), Mario Lipinski (www.lipinski.tk) and others."
	+"\nFor publications related to this work see www.isg.uni-konstanz.de/publications/pub."
	+"\npublished under MIT License";
}