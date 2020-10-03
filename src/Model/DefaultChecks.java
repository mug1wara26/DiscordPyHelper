package Model;

import java.util.Arrays;

public class DefaultChecks {
    private final String[] defaultChecks = new String[]{"has_role", "has_permissions", "has_guild_permissions", "has_any_role", "bot_has_role", "bot_has_permissions", "bot_has_guild_permissions", "bot_has_any_role", "cooldown", "max_concurrency", "guild_only", "dm_only", "is_owner", "is_nsfw"};
    private final String[] defaultChecksParamsType = new String[]{"Union[str,int]", "kwargs[Permissions]", "kwargs[Permissions]", "List[Union[str,int]]", "Union[str,int]", "kwargs[Permissions]", "kwargs[Permissions]", "List[Union[str,int]]", "args[rate:int,per:float,type:BucketType]", "args[number:int,per:BucketType,wait:bool]", null, null, null, null};
    private final String defaultCheckURLFormat = "https://discordpy.readthedocs.io/en/latest/ext/commands/api.html#discord.ext.commands.%s";

    private final String FLOAT_REGEX = "[-+]?[0-9]*\\.?[0-9]+";
    private final String INT_REGEX = "\\d+";


    public String getDefaultParams(String checkName) {
        return defaultChecksParamsType[Arrays.asList(defaultChecks).indexOf(checkName)];
    }

    public String getURL(String checkName) {
        return String.format(defaultCheckURLFormat, checkName);
    }

    public String[] getDefaultChecks() {
        return defaultChecks;
    }
}