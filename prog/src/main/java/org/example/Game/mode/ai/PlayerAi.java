package org.example.Game.mode.ai;


import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import org.example.Game.mode.Player;
import org.example.Settings;
import org.example.embAsp.Group;
import org.example.embAsp.WondevWomanHandler;
import org.example.embAsp.cell;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Stream;


public class PlayerAi extends Player implements Callable<actionSet> {
    private final ArrayList<ASPInputProgram> encodings;
    private final WondevWomanHandler handler;
    private static final ASPInputProgram gridState = new ASPInputProgram();

    private final String encPath;
    private final String embAspPath;
    private final  String embAspPackage;
    private Group myGroup;
    public static final int GROUP_1 = 1;
    public static final int GROUP_2 = 2;
    public static final int GROUP_3 = 3;
    public static final int GROUP_4 = 4;

    public PlayerAi(char symbol, int playerCode, int GROUP_ID) {
        super(symbol, playerCode);
        switch (GROUP_ID){
            case GROUP_1 ->{
                encPath = Settings.PATH_ENCOD_GROUP1;
                embAspPath = Settings.PATH_EMBASP_GROUP1;
                embAspPackage = Settings.PACKAGE_EMBASP_GROUP1;
            }
            case GROUP_2 -> {
                encPath = Settings.PATH_ENCOD_GROUP2;
                embAspPath = Settings.PATH_EMBASP_GROUP2;
                embAspPackage = Settings.PACKAGE_EMBASP_GROUP2;
            }
            case GROUP_3 -> {
                encPath = Settings.PATH_ENCOD_GROUP3;
                embAspPath = Settings.PATH_EMBASP_GROUP3;
                embAspPackage = Settings.PACKAGE_EMBASP_GROUP3;
            }
            case GROUP_4 -> {
                encPath = Settings.PATH_ENCOD_GROUP4;
                embAspPath = Settings.PATH_EMBASP_GROUP4;
                embAspPackage = Settings.PACKAGE_EMBASP_GROUP4;
            }
            default -> throw new IllegalArgumentException("Group not found");
        }

        handler = new WondevWomanHandler();
        encodings = new ArrayList<>();

        addFilesFromFolder();

    }

    public PlayerAi(PlayerAi player){
        //MAKING A COPY OF THE PLAYER
        super(player);
        encPath = player.encPath;
        embAspPath = player.embAspPath;
        embAspPackage = player.embAspPackage;
        handler = new WondevWomanHandler(player.getHandler());
        encodings = player.getEncodings();
        myGroup = player.myGroup;

    }


    @Override
    public PlayerAi copy() {
        return new PlayerAi(this);
    }


    //--GETTERS AND SETTERS--------------------------------------------------------------------------------------------------
    public ArrayList<ASPInputProgram> getEncodings() {
        return encodings;
    }

//    public void setHandler(WondevWomanHandler handler) {
//        this.handler = handler;
//    }

    public WondevWomanHandler getHandler() {
        return handler;
    }




//--EMB ASP METHODS-----------------------------------------------------------------------------------------------------

    /**
     * Set the handler encoding to <p>
     * {@code MyPlayerAi.encodings.get(index}
     * @param index
     */
    public void chooseEncoding(int index){
        handler.setEncoding(encodings.get(index));
    }

    /**
     * Set the handler encoding to the input program passed as parameter
     * @param encoding
     */
    public void setEncoding(ASPInputProgram encoding){
        handler.setEncoding(encoding);
    }

    public static synchronized void refreshGridState(ArrayList<cell> cells) throws Exception {
        gridState.clearAll();
        for (cell c : cells) {
            gridState.addObjectInput(c);
        }
    }

//--GAME METHODS--------------------------------------------------------------------------------------------------------
    //TODO:trovare un modo per non inizializzare ogni volta handler in Group.java
    //TODO: implementare per  più di una pedina per giocatore
    @Override
    public actionSet call() throws Exception {
        handler.setFactProgram(gridState);
        return myGroup.callEmbAsp(this);
    }


    //Actually works only for single unit per player
//    actionSet makeAction(Unit myUnit) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
//
//        handler.startSync();
//
//
//    //--
//        Point move = null;
//        Point build= null;
//        for (AnswerSet answerSet: handler.getOptimalAnswerSets()) {
//        //--IF CAN'T PERFORM ANY ACTION
//            if (answerSet.getAnswerSet().isEmpty())
//                return new actionSet(myUnit); //nullAction = true
//
//        //--ELSE
//            for (Object obj :answerSet.getAtoms()){
//                if(obj instanceof moveIn)
//                    move = new Point(((moveIn) obj).getX(), ((moveIn) obj).getY());
//                else if(obj instanceof buildIn)
//                    build = new Point(((buildIn) obj).getX(), ((buildIn) obj).getY());
//
//             }
//        }
//
//
//
//        return new actionSet( myUnit, move, build);
//    }

//--UTILITY METHODS-----------------------------------------------------------------------------------------------------

    /**
     * Add all the encodings in the folder to the encodings list
     * and set the first encoding as the input program.<p>
     * Set the group of the player.
     */
    private void addFilesFromFolder(){
    //--ADD ENCODINGS
        Path dir = Paths.get(encPath);

        try (Stream<Path> paths = Files.list(dir)) {
            paths.forEachOrdered(enc ->{
                ASPInputProgram inputProg = new ASPInputProgram();
                inputProg.addFilesPath(enc.toString());
                encodings.add(inputProg);
                }
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    //TODO: attualmente prende sempre il primo encoding di base
       chooseEncoding(0);

    //--SET myGroup

        dir = Paths.get(embAspPath);

        try (Stream<Path> paths = Files.list(dir)) {

           paths.forEach(path -> {

                try {
                    URL[] urls = {path.toUri().toURL()};
                    URLClassLoader loader = new URLClassLoader(urls);
                    String className = path.getFileName().toString().replace(".java", "");

                    Class<?> clazz = Class.forName(embAspPackage +className, true, loader);
                    if (Group.class.isAssignableFrom(clazz)){
                        myGroup = (Group) clazz.getDeclaredConstructor().newInstance();
                        paths.close();
                    }

                } catch (IOException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                         InstantiationException | IllegalAccessException e) {
                    throw  new RuntimeException(e);
                }
           });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    //--JAVA UTILITY--------------------------------------------------------------------------------------------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerAi playerAi)) return false;

        if (playerCode != playerAi.playerCode) return false;
        if (symbol != playerAi.symbol) return false;
        if (!encodings.equals(playerAi.encodings)) return false;
        if (!handler.equals(playerAi.handler)) return false;
        if (!encPath.equals(playerAi.encPath)) return false;
        if (!embAspPath.equals(playerAi.embAspPath)) return false;
        if (!embAspPackage.equals(playerAi.embAspPackage)) return false;
        return myGroup.equals(playerAi.myGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(encodings, handler, encPath, embAspPath, embAspPackage, myGroup);
    }
}
