package Model;

import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command {
    private String name;
    private String description;
    private ArrayList<Check> checks;
    private ArrayList<String> args;
    private ArrayList<String> converters;
    private ArrayList<String> params;
    private boolean checkAny;
    private boolean varArg;
    private boolean keywordArg;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
        args = new ArrayList<>();
        converters = new ArrayList<>();
        params = new ArrayList<>();
    }

    public void setVarArg(boolean varArg) {
        this.varArg = varArg;
    }

    public void setKeywordArg(boolean keywordArg) {
        this.keywordArg = keywordArg;
    }

    public void addAllArgs(ObservableList<String> args) {
        this.args.addAll(args);
        params.addAll(args);
    }

    public void addAllConverters(ObservableList<String> converters) {
        String converterName;
        for(String converter : converters) {
            converterName = converter + ":discord." + converter.substring(0, 1).toUpperCase();
            Pattern pattern = Pattern.compile("\\d");
            Matcher matcher = pattern.matcher(converter);
            if(matcher.find()) {
                converterName += converter.substring( 1, matcher.start());
            }
            else converterName += converter.substring(1);

            params.add(converterName);
        }
    }

    public void setCheckAny(boolean checkAny) {
        this.checkAny = checkAny;
    }

    public void setChecks(ArrayList<Check> checks) {
        this.checks = checks;
    }

    public ArrayList<String> getParams() {
        return params;
    }

    public void moveParams(int beforeIndex, int afterIndex) {
        params.add(afterIndex, params.get(beforeIndex));
        params.remove(beforeIndex + 1);
    }

    public String getCommandDef() {
        StringBuilder commandDef;

        commandDef = new StringBuilder("@bot.command(\"" + description + "\")\n");

        if(checkAny) {
            commandDef.append("@commands.check_any(");
            for(Check check: checks) {
                commandDef.append(check.toString()).append(", ");
            }
            if(!checks.isEmpty()) commandDef.delete(commandDef.length() - 2, commandDef.length());
            commandDef.append("\n");
        }
        else {
            for(Check check : checks) {
                commandDef.append(check.toString()).append("\n");
            }
        }

        commandDef.append("async def ").append(name).append("(ctx");


        for (String param : params) {
            commandDef.append(", ").append(param);
        }

        if(varArg) commandDef.append(", *varArg");
        if(keywordArg) commandDef.append(", *, keywordArg");

        commandDef.append("):");

        return commandDef.toString();
    }
}
