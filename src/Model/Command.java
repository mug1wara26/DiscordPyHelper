package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Command {
    private String name;
    private String description;
    private boolean checkAny;
    private ArrayList<Check> checks;
    private ArrayList<String> args;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setCheckAny(boolean checkAny) {
        this.checkAny = checkAny;
    }

    public void setChecks(ArrayList<Check> checks) {
        this.checks = checks;
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

        commandDef.append("async def ").append(name).append("(ctx):");

        return commandDef.toString();
    }
}
