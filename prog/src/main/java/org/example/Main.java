package org.example;

import org.example.Game.gameManager.GameHandler;
import org.example.Game.mode.ai.PlayerAi;
import org.example.embAsp.MyHandler;

import java.util.Scanner;


public class Main {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
    //--SET PATH TO DLV2
        MyHandler.setRelPathToDLV2(Settings.PATH_TO_DLV2);

//COMMENTARE SE NON SI VUOLE ESEGUIRE TEST
//        GameHandler.testAiBruteForce(PlayerAi.GROUP_3, PlayerAi.GROUP_1);



    //--
        System.out.println("Inserire 0 per eseguire il codice in manuale, 1 per eseguire il codice con AI");


        String  choice =  sc.next();

        if (choice.equals("1") ) {
            int[] groupID = input();
            GameHandler.runAI(groupID[0],groupID[1]);
        }

        else if (choice.equals("0"))
            GameHandler.runManual();
        else{
            System.out.println("Scelta non valida");
            main(args);
        }

    }

    private static int[] input(){
        int[] groupID = new int[2];

        System.out.println("Scegliere quali gruppi si scontreranno tra {1-2-3-4}");

        System.out.print("Gruppo: ");
        String group= sc.next();
        switch (group) {
            case "1" -> groupID[0] = PlayerAi.GROUP_1;
            case "2" -> groupID[0] = PlayerAi.GROUP_2;
            case "3" -> groupID[0] = PlayerAi.GROUP_3;
            case "4" -> groupID[0] = PlayerAi.GROUP_4;
            default -> {
                System.out.println("\nScelta non valida");
                return input();
            }
        }

        System.out.print("vs Gruppo: ");
        group= sc.next();
        switch (group) {
            case "1" -> groupID[1] = PlayerAi.GROUP_1;
            case "2" -> groupID[1] = PlayerAi.GROUP_2;
            case "3" -> groupID[1] = PlayerAi.GROUP_3;
            case "4" -> groupID[1] = PlayerAi.GROUP_4;
            default -> {
                System.out.println("\nScelta non valida\n");
                return input();
            }
        }

        return groupID;
    }
}
