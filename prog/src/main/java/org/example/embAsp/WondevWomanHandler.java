package org.example.embAsp;

import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;

public class WondevWomanHandler extends MyHandler{

    public WondevWomanHandler(){
        super();
        try {
            mapToEmb(cell.class);
            mapToEmb(buildIn.class);
            mapToEmb(moveIn.class);
        } catch (ObjectNotValidException | IllegalAnnotationException e) {
            throw new RuntimeException(e);
        }
    }
    public WondevWomanHandler(WondevWomanHandler handler){
        super(handler);
    }

    @Override
    public void startSync() {
        super.startSync();

    }
}
