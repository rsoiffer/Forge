/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commands;

import java.util.List;

/**
 *
 * @author Cruz
 */
public class Command {
    
    private String code;
    private String help;
    private CommRun comm;
    
    public Command(String c, String h, CommRun cr){
        
        code = c;
        help = h;
        comm = cr;
    }
    
    public String run(List<String> args){
        
        return comm.runCommand(args) ? "Successful" : help;
    }
    
    public String getCode(){
        
        return code;
    }
    
    public void setCode(String c){
        
        code = c;
    }
    
    public String getHelp(){
        
        return help;
    }
    
    public void setHelp(String h){
        
        help = h;
    }
}
