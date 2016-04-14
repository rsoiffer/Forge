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
    private CommRun comm;
    
    public Command(String c, CommRun cr){
        
        code = c;
        comm = cr;
    }
    
    public String run(List<String> args){
        
        return comm.runCommand(args);
    }
    
    public String getCode(){
        
        return code;
    }
    
    public void setCode(String c){
        
        code = c;
    }
}
