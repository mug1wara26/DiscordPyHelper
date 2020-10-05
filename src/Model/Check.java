package Model;

import java.util.ArrayList;

public class Check {
    private String name;
    private ArrayList<String> params = new ArrayList<>();
    private boolean hasParams;
    String type;

    public Check(String name, ArrayList<String> params) {
        this.name = name;
        this.params = params;
        hasParams = true;
    }

    public Check(String name, String type) {
        this.name = name;
        this.type = type;
        hasParams = false;
    }

    public void addParams(String param) {
        params.add(param);
        hasParams = true;
    }

    public boolean isHasParams() {
        return hasParams;
    }

    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder("@commands." + name + "(");

        if(hasParams) {
            if(type.equals("List")) returnString.append("[");
            for (String param : params) {
                returnString.append(param).append(", ");
            }
            returnString.delete(returnString.length() - 2, returnString.length());
            if(type.equals("List")) returnString.append("]");
        }

        returnString.append(")");

        return returnString.toString();
    }
}
