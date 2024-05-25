package org.example.embAsp;

import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;

public class WondevWomanHandler extends MyHandler{

    public WondevWomanHandler(){
        super();
        try {
            mapToEmb(cell.class);
            mapToEmb(unitASP.class);
            mapToEmb(floor.class);
        } catch (ObjectNotValidException | IllegalAnnotationException e) {
            throw new RuntimeException(e);
        }
    }
}
