package type;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bduong
 */
public enum TestType {
    
    ALL("all"),
    
    DATA("data");
    
    private String type;
    
    TestType(String type) {
        this.type = type;
    }
    
    public String getType(){
        return type;
    }
}
