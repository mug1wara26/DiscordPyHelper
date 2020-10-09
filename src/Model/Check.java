package Model;

import java.util.ArrayList;

public class Check extends abstractClass {
    private ArrayList<String> params = new ArrayList<>();
    private boolean hasParams;
    String type;

    public Check(String name, ArrayList<String> params) {
        super(name);
        this.params = params;
        hasParams = true;
    }

    public Check(String name, String type) {
        super(name);
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

    @Override
    String getName() {
        return null;
    }
}
