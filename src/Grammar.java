public class Grammar {
    private String veriable;
    private String rule;
    public Grammar(String veriable, String rule){
        this.veriable = veriable;
        this.rule = rule;
    }
    public String getVeriable(){return veriable;}
    public String getRule(){return rule;}
    public void setRule(String rule){
        this.rule = rule;
    }
    public void setVeriable(String veriable){this.veriable=veriable;}

}
