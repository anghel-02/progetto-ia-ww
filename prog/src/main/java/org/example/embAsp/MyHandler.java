package org.example.embAsp;

import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.OptionDescriptor;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.platforms.desktop.DesktopService;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;

import java.util.Set;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Creata da me velocemente per gestire EmbASP
 * Settare il path a DLV2 in Settings
 * */
public class MyHandler {
    private static String REL_PATH_TO_DLV2 ="";

    private DesktopService service;
    private Handler handler;

    private final ASPInputProgram facts =  new ASPInputProgram();
    private ASPInputProgram enconding = new ASPInputProgram();

    
    private Output output=null;
    private OptionDescriptor option;
    private Integer OPTION_ID_n0 = null ;

//-----------------CONSTRUCTOR----------------------------------------

    public MyHandler(){
        initEmbAsp();
        option = new OptionDescriptor("-n 0");
    }

    public MyHandler(String encodingPath){
        initEmbAsp();
        option = new OptionDescriptor("-n 0");
        addEncodingPath(encodingPath);
    }


    private void initEmbAsp() {
        if (REL_PATH_TO_DLV2.isEmpty()){
            throw new RuntimeException("Path to DLV2 not set, please set it calling setRelPathToDLV2 method");
        }
        service = new DLV2DesktopService(REL_PATH_TO_DLV2);
        handler = new DesktopHandler(service);
        handler.addProgram(facts);
        handler.addProgram(enconding);
    }

//--GETTERS & SETTERS---------------------------------------------------------------------------------------------------

    /**
     * Set the relative path to DLV2. <p>
     * @param path
     */
    public static void setRelPathToDLV2(String path){
        REL_PATH_TO_DLV2 = path;
    }

    public void setFactProgram(ASPInputProgram program){
        facts.clearAll();
        for (String path: program.getFilesPaths())
            facts.addFilesPath(path);
        facts.setPrograms(program.getPrograms());
    }

    public void setEncoding(ASPInputProgram program){
        enconding.clearAll();
        for (String path: program.getFilesPaths())
            enconding.addFilesPath(path);
        enconding.setPrograms(program.getPrograms());
    }



//-----------------METHODS----------------------------------------

    public void clearFacts(){
        facts.clearAll();
    }

    public void showAllAnswerSet(boolean flag){
        if (flag)
            OPTION_ID_n0 = (Integer) handler.addOption(option);
        else if (OPTION_ID_n0 != null)
            handler.removeOption(OPTION_ID_n0);
    }

    public void setNumberOfAnswerSet(int n){
        assert (n>=0);
        String optString= " -n"+n;
        OptionDescriptor opt = new OptionDescriptor(optString);
        handler.addOption(opt);
    }


    public void mapToEmb(Class<?> c  ) throws ObjectNotValidException, IllegalAnnotationException {
        ASPMapper.getInstance().registerClass(c);
    }
//-----------------ADDs-------------------------------------------

    public void addOption(String option){
        OptionDescriptor opt = new OptionDescriptor(option);
        handler.addOption(opt);
    }

    /**
     * Check if a string is a valid fact using regular expression.
     */
    private boolean checkFactString(String s){
        // fact
        String symConst = "[a-zA-Z]+";
        Pattern predicate = Pattern.compile(symConst);
        // fact(1)
        String intOrSymConst = "(\\d|[a-zA-Z]+)";
        Pattern atom1 = Pattern.compile(predicate.pattern() + "\\("+ intOrSymConst + "\\)" );
        // fact(1,...)  
        String intOrSymConst_comma = "((\\d|[a-zA-Z]+),)+";
        Pattern atomN = Pattern.compile(predicate.pattern() + 
                        "\\(" + intOrSymConst_comma + intOrSymConst + "\\)" );
        

        if( s.matches(atomN.pattern()) || s.matches(atom1.pattern()) || s.matches(predicate.pattern()) )
            return true;
        
        return false;
    }

    /**
     * Add a fact accepting as parameter a {@code String}. <p>
     * The string must be in the form of a fact without '.' , for example: 
     * "fact" or "fact(1)" or "fact(1,a)". <p>
     * Actually string constants are not accepted, only integer and SymbolicConstant;  
     * so fact("s") isn't valid.<p>
     * 
     * @param s - fact to add
     * @throws Exception if string is not valid
     */
    public void addFactAsString(String s) throws Exception {
        if (! checkFactString(s))
            throw new Exception("String is not valid");
   
        facts.addProgram(s+".");
        
    }

    /**
     * Add a fact accepting as parameter an {@code Object}.<p>
     * Call method {@code mapToEmb} before adding an {@code Object}.
     * @param o
     * @throws Exception
     */
    public void addFactAsObject(Object o) throws Exception {
        if (o == null)
            throw new Exception("Object is null");

        facts.addObjectInput(o);
        
    }

    public void addFactsAsObjectSet(Set<Object> objects) throws Exception {
        if (objects == null)
            throw new Exception("Object is null");

        facts.addObjectsInput(objects);
        
    }

    /**
     * Add an encoding file (ASP program). <p>
     * @param encodingPath path relative to the project root
     */
    public void addEncodingPath(String encodingPath) {
        if (encodingPath == null || encodingPath.isEmpty())
            throw new RuntimeException("Encoding path is null or empty");
        enconding.addFilesPath(encodingPath);
    }

//    public void addFactProgram(ASPInputProgram program){
//        handler.addProgram(program);
//    }


//--SOLVE-------------------------------------------------------------------------------------------------------------


    /**
     * Start the solving process. <p>
     * Call this method before {@code getOutput}.
     */
    public void startSync() {
        if (enconding.getFilesPaths().isEmpty() && enconding.getPrograms().isEmpty())
            throw new RuntimeException("Encoding not found, please add encoding");

        output = handler.startSync();
        System.out.println("\n");

        if (! output.getErrors().isEmpty())
            throw new RuntimeException("Errors in output: "+ output.getErrors());
        if (isIncoherent())
            throw new RuntimeException("Incoherent output");
        if (isSafetyError())
            throw new RuntimeException("Safety error " + output.getOutput());

        facts.clearAll();
    }


    /**
     * Get the output of the solving process. <p>
     * Always call this method after {@code startSync} method. <p>
     * To manage the output as answersets cast to {@code Answersets} type -> {@code (Answersets) output} .
     * @return Output
     */
    public Output getOutput(){
        if(output == null)
            throw new RuntimeException("Output is null, maybe startSync methods was never launched");

        return output;
    }

    public AnswerSets getAnswerSets(){
        if (output == null)
            throw new RuntimeException("Output is null, maybe startSync methods was never launched");
        return (AnswerSets) output;
    }

    public List<AnswerSet> getAnswerSetsList(){
        if(getAnswerSets().getAnswersets().isEmpty())
            throw new RuntimeException("AnswerSets list is empty: " );

        return ((AnswerSets) output).getAnswersets();
    }

    private boolean isIncoherent(){
        if (output == null)
            throw new RuntimeException("Output is null, maybe startSync methods was never launched");
        return output.getOutput().matches("DLV 2.1.2\n" +
                "\n" +
                "INCOHERENT\n");
    }

    private boolean isSafetyError(){
        if (output == null)
            throw new RuntimeException("Output is null, maybe startSync methods was never launched");
        return output.getOutput().contains("--> Safety Error");
    }

    private boolean isEmpty(){
        if (output == null)
            throw new RuntimeException("Output is null, maybe startSync methods was never launched");
        return ((AnswerSets)output).getAnswersets().isEmpty();
    }










}
