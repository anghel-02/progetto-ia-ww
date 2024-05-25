package org.example.Game;


import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import org.example.embAsp.WondevWomanHandler;
import org.example.embAsp.cell;
import org.example.embAsp.floor;
import org.example.embAsp.unitASP;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Stream;


public class PlayerAi extends Player implements Callable<actionSet> {
    private final String dirPath;
    private final ArrayList<InputProgram> encodings;
    private  WondevWomanHandler handler;


    PlayerAi(char symbol, int playerCode, String dirPath) {
        super(symbol, playerCode);

        this.dirPath = dirPath;
        handler = new WondevWomanHandler();
        encodings = new ArrayList<>();
        addEncodingsFromFolder();

    }

//--EMB ASP METHODS-----------------------------------------------------------------------------------------------------
    void addInputProgram(InputProgram inputProg){
        handler.addInputProgram(inputProg);
    }

//--GAME METHODS--------------------------------------------------------------------------------------------------------
    @Override
    public actionSet call() throws Exception {
        unit myUnit = units.getFirst();
        handler= new WondevWomanHandler();
        handler.addInputProgram(encodings.getFirst());

        for (Point coord: GameHandler.board.playableArea(myUnit.unitCode()) ){
            handler.addFactAsObject(new cell(coord.x, coord.y));
        }

    //--ADD FLOORS
        int [][] grid = GameHandler.board.getGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                handler.addFactAsObject(new floor(i, j, grid[i][j]));
    //--ADD UNITS
                if (GameHandler.board.isOccupied(i, j)){
                    handler.addFactAsObject(new unitASP(i, j));
                }
            }
        }

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
