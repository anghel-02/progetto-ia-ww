package org.example;

import org.example.Game.Board;
import org.example.embAsp.MyHandler;
import org.example.Game.GameHandler;


import java.util.Scanner;


public class Main {
    private static final Scanner sc = new Scanner(System.in);



    public static void main(String[] args) {
    //--SET PATH TO DLV2
        MyHandler.setRelPathToDLV2(Settings.PATH_TO_DLV2);



    //--
        System.out.println("Inserire 0 per eseguire il codice in manuale, 1 per eseguire il codice con AI");


        String  choice =  sc.next();

        switch (choice) {
            case "0":
                GameHandler.runManual();
                break;
            case "1":
                String folderPath1 , folderPath2;

                System.out.println("Scegliere quali gruppi si scontreranno tra {1-2-3-4}");
                //TODO: implementare check su scelta gruppi
                System.out.print("Gruppo: ");
                String group= sc.next();
                switch (group){
                    case "1":
                        folderPath1 = Settings.PATH_TO_GROUP1;
                        break;
                    case "2":
                        folderPath1 = Settings.PATH_TO_GROUP2;
                        break;
                    case "3":
                        folderPath1 = Settings.PATH_TO_GROUP3;
                        break;
                    case "4":
                        folderPath1 = Settings.PATH_TO_GROUP4;
                        break;
                    default:
                        System.out.println("Scelta non valida");
                        main(args);
                        return;
                }
                System.out.print("vs Gruppo: ");
                group= sc.next();
                switch (group){
                    case "1":
                        folderPath2 = Settings.PATH_TO_GROUP1;
                        break;
                    case "2":
                        folderPath2 = Settings.PATH_TO_GROUP2;
                        break;
                    case "3":
                        folderPath2 = Settings.PATH_TO_GROUP3;
                        break;
                    case "4":
                        folderPath2 = Settings.PATH_TO_GROUP4;
                        break;
                    default:
                        System.out.println("Scelta non valida");
                        main(args);
                        return;
                }





                GameHandler.runAI(folderPath1, folderPath2);
                break;
            default:
                System.out.println("Scelta non valida");
                main(args);
        }


    }
}
