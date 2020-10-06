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
    private boolean checkAny;
    private boolean varArg;
    private boolean keywordArg;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
        args = new ArrayList<>();
        converters = new ArrayList<>();
    }

    public void setVarArg(boolean varArg) {
        this.varArg = varArg;
    }

    public void setKeywordArg(boolean keywordArg) {
        this.keywordArg = keywordArg;
    }

    public void addAllArgs(ObservableList<String> args) {
        this.args.addAll(args);
    }

    public void setCheckAny(boolean checkAny) {
        this.checkAny = checkAny;
    }

    public void setChecks(ArrayList<Check> checks) {
        this.checks = checks;
    }

    public void addAllConverters(ObservableList<String> converters) {
        this.converters.addAll(converters);
    }

    public ArrayList<String> getParams() {
        ArrayList<String> params = new ArrayList<>(args);
        params.addAll(converters);

        return params;
    }

    public void moveParams(int index1, int index2) {

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

        for(String arg : args) {
            commandDef.append(", ").append(arg);
        }


        for(String converter: converters) {
            commandDef.append(", ").append(converter).append(":discord.").append(converter.substring(0, 1).toUpperCase());
            if(converter.matches("\\d")) {
                Pattern pattern = Pattern.compile("\\d");
                Matcher matcher = pattern.matcher(converter);
                commandDef.append(converter, 1, matcher.start() - 2);
            }
            else commandDef.append(converter.substring(1));
        }


        if(varArg) commandDef.append(", *varArg");
        if(keywordArg) commandDef.append(", *, keywordArg");

        commandDef.append("):");

        return commandDef.toString();
    }
}
