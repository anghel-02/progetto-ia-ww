package main.java.org.example.EmbAsp;

import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.OptionDescriptor;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.platforms.desktop.DesktopService;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * Creata da me velocemente per gestire EmbASP
 */
public class MyHandler {
    private DesktopService service;
    private Handler handler;

    private InputProgram facts;
    private InputProgram enconding;

    
    private Output output;
    private OptionDescriptor option;
    private Integer OPTION_ID_n0 = null ;

//-----------------CONSTRUCTOR----------------------------------------

    public MyHandler(){
        initEmbAsp();
        option = new OptionDescriptor("-n 0");
    }


    private void initEmbAsp() {
        service = new DLV2DesktopService("lib/dlv2.exe");
        handler = new DesktopHandler(service);
        facts = new ASPInputProgram();
        enconding = new ASPInputProgram();
    }

//-----------------METHODS----------------------------------------

    public void clearAll(){
        handler.removeAll();
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
     * Add a fact accepting as parameter a string. <p>
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


    public void addEncoding(String  encodingPath) {
        if (encodingPath == null || encodingPath.isEmpty())
            throw new RuntimeException("Encoding path is null or empty");
        enconding.addFilesPath(encodingPath);
    }

//-----------------SOLVE----------------------------------------
    public void startSync() {
        if (enconding.getFilesPaths().isEmpty())
            throw new RuntimeException("Encoding not found, please add encoding file with addEncoding method");

        handler.addProgram(enconding);

        if (! facts.getPrograms().isEmpty())
            handler.addProgram(facts);

        output = handler.startSync();
    }


    public Output getOutput(){
        if(output == null)
            throw new RuntimeException("Output not found, maybe startSync methods was never launched");
        return output;
    }






}
