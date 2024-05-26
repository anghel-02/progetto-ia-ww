package org.example.Game.embASP;


import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import org.example.Game.Player;
import org.example.Game.actionSet;
import org.example.embAsp.WondevWomanHandler;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.stream.Stream;


public class PlayerAi extends Player implements Callable<actionSet> {
    private final String dirPath;
    private final ArrayList<InputProgram> encodings;
    private WondevWomanHandler handler;
    private final int groupID;


    public PlayerAi(char symbol, int playerCode, String dirPath, int groupID) {
        super(symbol, playerCode);
        this.dirPath = dirPath;
        this.groupID = groupID;

        handler = new WondevWomanHandler();
        encodings = new ArrayList<>();
        addEncodingsFromFolder();

    }

//--GETTERS AND SETTERS--------------------------------------------------------------------------------------------------

    int getGroupID()
    {
        return groupID;
    }

    ArrayList<InputProgram> getEncodings() {
        return encodings;
    }

    WondevWomanHandler getHandler() {
        return handler;
    }

    void setHandler(WondevWomanHandler handler) {
        this.handler = handler;
    }

    ArrayList<unit> getUnits(){
        return units;
    }

//--EMB ASP METHODS-----------------------------------------------------------------------------------------------------
    void addInputProgram(InputProgram inputProg){
        handler.addInputProgram(inputProg);
    }

//--GAME METHODS--------------------------------------------------------------------------------------------------------
    @Override
    public actionSet call() throws Exception {
        //TODO: actually works only for single unit per player
        //TODO : trovare un modo per non inizializzare ogni volta handler in Gruppi.java
        Gruppi.call(this);
        unit myUnit = units.getFirst();
        return makeAction(myUnit);
    }


    //Actually works only for single unit per player
    actionSet makeAction(unit myUnit) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {


        handler.startSync();
        AnswerSets as= (AnswerSets) handler.getOutput();
        String a = "";
        for (AnswerSet answerSet: as.getAnswersets() ) {
             a = answerSet.toString();
        }

        //prova
        Point move = new Point(Integer.parseInt(a.substring(4,5)) , Integer.parseInt(a.substring(6,7)));
        Point build = new Point(myUnit.coord());


        return new actionSet(this, myUnit, move, build);
    }

//--UTILITY METHODS-----------------------------------------------------------------------------------------------------

    /**
     * Add all the encodings in the folder to the encodings list,
     * then add the first encoding to the handler
     */
    private void addEncodingsFromFolder(){
        Path dir = Paths.get(dirPath);

        try (Stream<Path> paths = Files.list(dir)) {
            paths.forEach(enc ->{
                InputProgram inputProg = new InputProgram();
                inputProg.addFilesPath(enc.toString());
                encodings.add(inputProg);
                }
            );

            addInputProgram(encodings.getFirst());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }



}
